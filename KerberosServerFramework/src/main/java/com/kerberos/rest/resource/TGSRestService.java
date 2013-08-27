/**
 * 
 */
package com.kerberos.rest.resource;

import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kerberos.configuration.KerberosConfigurationManager;
import com.kerberos.db.model.TGT;
import com.kerberos.db.service.IServiceTicketService;
import com.kerberos.db.service.ITGTService;
import com.kerberos.exceptions.InternalSystemException;
import com.kerberos.exceptions.UnauthorizedServiceTicketRequestException;
import com.kerberos.rest.api.ITGSAPI;
import com.kerberos.rest.api.TGSAPIImpl.ServiceTicketRequestAttributes;
import com.kerberos.rest.representation.ServiceTicketRequest;
import com.kerberos.rest.representation.ServiceTicketResponse;
import com.kerberos.util.ActiveDirectory.ActiveDirectoryImpl.SecretKeyType;
import com.kerberos.util.dateutil.IDateUtil;
import com.kerberos.util.encryption.IEncryptionUtil;
import com.kerberos.util.hashing.IHashUtil;
import com.kerberos.util.keyserver.KeyServerUtil;

/**
 * @author raunak
 *
 */
@Component
@Path("/TGS")
public class TGSRestService {

	private static Logger log = Logger.getLogger(TGSRestService.class);

	private @Autowired ITGSAPI iTGSAPI;
	
	private @Autowired ITGTService iTGTService;
	private @Autowired IServiceTicketService iServiceTicketService;
	
	private @Autowired IHashUtil iHashUtil;
	private @Autowired IDateUtil iDateUtil;
	private @Autowired IEncryptionUtil iEncryptionUtil;
	private @Autowired KeyServerUtil keyServerUtil;
	
	private @Autowired KerberosConfigurationManager configurationManager;
	
	@Path("/serviceTicket/request")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ServiceTicketResponse processServiceTicketRequest(ServiceTicketRequest request) throws InternalSystemException, UnauthorizedServiceTicketRequestException{
		
		log.info("Entering processServiceTicketRequest");
		
		//Get the Kerberos Master Key
		log.debug("Fetching the Kerberos Master Key from Key Server");
		SecretKey kerberosMasterKey = keyServerUtil.getKeyFromKeyStore(null, SecretKeyType.KDC_MASTER_KEY);
		if (kerberosMasterKey == null){
			log.error("Error fetching Kerberos Master Key");
			throw new InternalSystemException();
		}
		
		//Decrypt and validate Service Ticket Request Attributes
		log.debug("Decrypt and validate Service Ticket Request Attributes");
		Map<ServiceTicketRequestAttributes, String> serviceTicketRequestAttributes = 
				iTGSAPI.decryptAndValidateServiceTicketRequestAttributes(request, kerberosMasterKey);
		if (serviceTicketRequestAttributes == null){
			log.error("Invalid Service Ticket Request Attributes. Request Denied!");
			throw new UnauthorizedServiceTicketRequestException();
		}
		
		//Validate if TGT exists for the Session ID
		log.debug("Validate if TGT exists for the Session ID");
		String sessionID = serviceTicketRequestAttributes.get(ServiceTicketRequestAttributes.TGT_PACKET_SESSIONID);
		TGT tgt = iTGTService.findTGTForSessionKey(sessionID);
		if (tgt == null){
			log.error("Invalid TGT Packet Session ID. Request Denied!");
			throw new UnauthorizedServiceTicketRequestException();
		}
		
		//Create the Service Ticket
		String serviceSessionID = iHashUtil.getSessionKey();
		Date serviceTicketTimeOut = iDateUtil.generateDateWithDelay(configurationManager.getSERVICE_TICKET_TIME_OUT());
		String serviceName = serviceTicketRequestAttributes.get(ServiceTicketRequestAttributes.SERVICE_NAME);
		iServiceTicketService.saveServiceTicket(serviceSessionID, tgt, serviceName, serviceTicketTimeOut);
		
		//Retrieve the Service Key for the request Service
		log.debug("Retrieving the Service Key for the request Service");
		SecretKey serviceKey;
		if (serviceName.equals("KEY_SERVER")){
			serviceKey = keyServerUtil.getKeyFromKeyStore(null, SecretKeyType.KEY_SERVER);
		}
		else{
			serviceKey = keyServerUtil.getKeyFromKeyStore(serviceName, SecretKeyType.SERVICE_KEY);
		}
		if (serviceKey == null){
			throw new InternalSystemException();
		}
		
		//Create the Service Ticket Response
		log.debug("Creating the Service Ticket Response");
		String appLoginName = serviceTicketRequestAttributes.get(ServiceTicketRequestAttributes.TGT_PACKET_APP_LOGIN_NAME);
		String username = serviceTicketRequestAttributes.get(ServiceTicketRequestAttributes.TGT_PACKET_USERNAME);

		String requestAuthenticatorStr = serviceTicketRequestAttributes.get(ServiceTicketRequestAttributes.REQUEST_AUTHENTICATOR);
		Date requestAuthenticator = iDateUtil.generateDateFromString(requestAuthenticatorStr);
		Date responseAuthenticator = iDateUtil.createResponseAuthenticator(requestAuthenticator);
		String responseAuthenticatorStr = iDateUtil.generateStringFromDate(responseAuthenticator);

		String serviceTicketExpiryTimeStr = iDateUtil.generateStringFromDate(serviceTicketTimeOut); 
		
		SecretKey sessionKey = iEncryptionUtil.generateSecretKey(tgt.getSessionID());
		
		log.info("Returning from processServiceTicketRequest method");
		
		return iTGSAPI.createServiceTicketResponse(serviceSessionID, appLoginName, username, serviceTicketExpiryTimeStr, responseAuthenticatorStr, serviceKey, sessionKey);
		
	}
	
}
