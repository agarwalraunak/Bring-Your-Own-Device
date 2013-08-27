/**
 * 
 */
package com.service.kerberos.client;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.service.config.KerberosURLConfig;
import com.service.exception.RestClientException;
import com.service.kerberos.api.IKerberosServiceTicketRequestAPI;
import com.service.kerberos.api.KerberosServiceTicketRequestAPIImpl.ServiceTicketResponseAttributes;
import com.service.kerberos.representation.ServiceTicketRequest;
import com.service.kerberos.representation.ServiceTicketResponse;
import com.service.model.SessionManager;
import com.service.model.kerberos.KerberosSession;
import com.service.model.kerberos.ServiceTicket;
import com.service.util.connectionmanager.ConnectionManagerImpl.RequestMethod;
import com.service.util.connectionmanager.IConnectionManager;
import com.service.util.dateutil.IDateUtil;
import com.service.util.encryption.IEncryptionUtil;

/**
 * @author raunak
 *
 */
@Component
public class KerberosRequestServiceTicketClientImpl implements IKerberosServiceTicketClient {
	
	private static Logger log = Logger.getLogger(KerberosRequestServiceTicketClientImpl.class);
	
	private @Autowired SessionManager sessionManager;
	private @Autowired IKerberosServiceTicketRequestAPI iKerberosServiceTicketRequestAPI;
	private @Autowired KerberosURLConfig kerberosURLConfig;
	private @Autowired IDateUtil iDateUtil;
	private @Autowired IEncryptionUtil iEncryptionUtil;
	private @Autowired IConnectionManager iConnectionManager;
	
	@Override
	public ServiceTicket requestServiceTicketFromKerberos(String serviceName, KerberosSession kerberosSession) throws IOException, RestClientException  {
		
		log.info("Entering requestServiceTicketFromKerberos method");
		
		if (serviceName == null || serviceName.isEmpty() || kerberosSession == null){
			return null;
		}
		
		//Generating Session Key
		log.debug("Generating Session Key from kerberos session ID");
		SecretKey sessionKey = iEncryptionUtil.generateSecretKey(kerberosSession.getSessionID());
		String requestAuthenticatorStr = iDateUtil.createAuthenticator();
		Date requestAuthenticator = iDateUtil.generateDateFromString(requestAuthenticatorStr);
		
		//Creating Service Ticket Request
		log.debug("Creating Service Ticket Request");
		ServiceTicketRequest request = iKerberosServiceTicketRequestAPI.createServiceTicketRequest(serviceName, kerberosSession.getTgt(), requestAuthenticatorStr, sessionKey);
		
		//Requesting Kerberos Server for Service Ticket
		log.debug("Requesting Kerberos Server for Service Ticket");
		ServiceTicketResponse response = iConnectionManager.generateRequest(kerberosURLConfig.getKERBEROS_APP_SERVICE_TICKET_REQUEST_URL(),
				RequestMethod.POST_REQUEST_METHOD, ServiceTicketResponse.class, iConnectionManager.generateJSONStringForObject(request));
		
		
		//Decrypting and validating the Service Ticket Response Attributes
		log.debug("Decrypting and validating the Service Ticket Response Attributes");
		Map<ServiceTicketResponseAttributes, String> decryptedServiceTicketResponseAttributes = iKerberosServiceTicketRequestAPI.decryptAndValidateServiceTicketResponseAttributes(response, requestAuthenticator, sessionKey);
		
		if (decryptedServiceTicketResponseAttributes == null){
			return null;
		}
		
		//Creating Service Ticket
		log.debug("Creating Service Ticket");
		ServiceTicket serviceTicket = kerberosSession.getTgt().createServiceTicket(serviceName,
																	decryptedServiceTicketResponseAttributes.get(ServiceTicketResponseAttributes.SERVICE_SESSION_ID),
																	decryptedServiceTicketResponseAttributes.get(ServiceTicketResponseAttributes.SERVICE_TICKET_PACKET_APP_LOGIN_NAME),
																	decryptedServiceTicketResponseAttributes.get(ServiceTicketResponseAttributes.SERVICE_TICKET_PACKET_EXPIRY_TIME),
																	decryptedServiceTicketResponseAttributes.get(ServiceTicketResponseAttributes.SERVICE_TICKET_PACKET_SERVICE_SESSION_ID),
																	decryptedServiceTicketResponseAttributes.get(ServiceTicketResponseAttributes.SERVICE_TICKET_PACKET_USERNAME));
		
		log.info("Returning from requestServiceTicketFromKerberos");
		return serviceTicket;
	}
}
