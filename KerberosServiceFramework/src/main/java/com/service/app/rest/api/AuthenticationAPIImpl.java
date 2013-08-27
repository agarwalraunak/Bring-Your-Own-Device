/**
 * 
 */
package com.service.app.rest.api;

import java.io.IOException;
import java.util.Date;

import javax.crypto.SecretKey;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.service.app.rest.representation.ServiceLoginResponse;
import com.service.config.KerberosURLConfig;
import com.service.config.ServiceListConfig;
import com.service.exception.ApplicationDetailServiceInitializationException;
import com.service.exception.RestClientException;
import com.service.exception.common.InternalSystemException;
import com.service.kerberos.client.IKerberosAuthenticationClient;
import com.service.kerberos.client.IKerberosKeyServerClient;
import com.service.kerberos.client.IKerberosServiceTicketClient;
import com.service.kerberos.client.IServiceTicketAuthenticationClient;
import com.service.model.Session;
import com.service.model.kerberos.KerberosSession;
import com.service.model.kerberos.ServiceSession;
import com.service.model.kerberos.ServiceTicket;
import com.service.util.dateutil.IDateUtil;
import com.service.util.encryption.IEncryptionUtil;

/**
 * @author raunak
 *
 */
@Component
public class AuthenticationAPIImpl  implements IAuthenticationAPI{
	
	private static Logger log = Logger.getLogger(AuthenticationAPIImpl.class);
	
	private @Autowired IKerberosKeyServerClient iKerberosKeyServerClient;
	private @Autowired IKerberosServiceTicketClient iKerberosServiceTicketClient;
	private @Autowired IKerberosAuthenticationClient iKerberosAuthenticationClient;
	private @Autowired IServiceTicketAuthenticationClient iServiceTicketAuthenticationClient;
	
	private @Autowired KerberosURLConfig kerberosURLConfig;
	
	private @Autowired IEncryptionUtil iEncryptionUtil;
	private @Autowired IDateUtil iDateUtil;
	
	@Override
	public SecretKey callKeyServerForSecretKeyUtility() throws InternalSystemException, IOException, RestClientException{
		//Authenticate the Service Application
		KerberosSession kerberosSession;
		try {
			kerberosSession = iKerberosAuthenticationClient.kerberosAuthentication(null, null);
		} catch (IOException | RestClientException
				| ApplicationDetailServiceInitializationException e) {
			log.error("Error performing kerberos authentication\n"+e.getMessage());
			e.printStackTrace();
			throw new InternalSystemException();
		}
		if (kerberosSession == null){
			log.error("Kerberos Authentication of the System failed!");
			throw new InternalSystemException();
		}
		//Get the service ticket for Key server
		ServiceTicket serviceTicket = iKerberosServiceTicketClient.requestServiceTicketFromKerberos(ServiceListConfig.KEY_SERVER.getValue(), kerberosSession);
		if (serviceTicket == null){
			log.error("failed to get the Service Ticket for key server!");
			throw new InternalSystemException();
		}
		
		//Authenticate the service ticket against the key server
		ServiceSession session = iServiceTicketAuthenticationClient.serviceUserAuthentication(kerberosURLConfig.getKERBEROS_KEY_SERVER_LOGIN_REQUEST_URL(), 
				serviceTicket, null);
		
		//Get the service key from key server
		SecretKey serviceKey = iKerberosKeyServerClient.getKeyFromKeyServer(session);
		
		if (serviceKey == null){
			log.error("Failed to get the key from key server!");
			throw new InternalSystemException();
		}
		return serviceKey;
	}
	
	@Override
	public boolean validateServiceTicket(String appLoginName, String serviceSessionID, String serviceTicketExpirationString) {
		
		log.debug("Entering validateServiceTicket method");
		
		if (!iEncryptionUtil.validateDecryptedAttributes(appLoginName, serviceSessionID, serviceTicketExpirationString)){
			log.error("Input parameter can not be null to validateServiceTicket");
			return false;
		}
		
		if (new Date().after(iDateUtil.generateDateFromString(serviceTicketExpirationString))){
			return false;
		}
		
		log.debug("Returning from validateServiceTicket method");
		
		return true;
	}
	
	@Override
	public ServiceLoginResponse createAppAuthenticationResponse(Session session, Date requestAuthenticator, SecretKey serviceSessionKey)  {
		
		log.debug("Entering createKeyRequestResponse");
		
		if (session == null || requestAuthenticator == null || serviceSessionKey == null){
			log.error("Invalid input parameter to createKeyRequestResponse");
			return null;
		}
		
		Date responseAuthenticator = iDateUtil.createResponseAuthenticator(requestAuthenticator);
		session.setLatestAuthenticator(responseAuthenticator);
		String[] encryptedData = iEncryptionUtil.encrypt(serviceSessionKey, session.getSessionID(),  iDateUtil.generateStringFromDate(responseAuthenticator));
		String encAppSessionID = encryptedData[0];
		String encResponseAuthenticator  = encryptedData[1];
		
		//Create the response
		ServiceLoginResponse response = new ServiceLoginResponse();
		response.setEncResponseAuthenticator(encResponseAuthenticator);
		response.setEncSessionID(encAppSessionID);
		
		log.debug("Returning from createKeyRequestResponse method");
		
		return response;
	}
	
}