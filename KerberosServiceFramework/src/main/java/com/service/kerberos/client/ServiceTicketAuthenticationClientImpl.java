package com.service.kerberos.client;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.service.app.rest.representation.ServiceLoginRequest;
import com.service.app.rest.representation.ServiceLoginResponse;
import com.service.exception.RestClientException;
import com.service.kerberos.api.IServiceTicketAuthenticationAPI;
import com.service.kerberos.api.ServiceTicketAuthenticationAPIImpl.ServiceLoginResponseAttributes;
import com.service.model.kerberos.ServiceSession;
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
public class ServiceTicketAuthenticationClientImpl implements IServiceTicketAuthenticationClient{

	private static Logger log = Logger.getLogger(ServiceTicketAuthenticationClientImpl.class);
	
	private @Autowired IServiceTicketAuthenticationAPI iServiceTicketAuthenticationAPI;
	
	private @Autowired IDateUtil iDateUtil;
	private @Autowired IEncryptionUtil iEncryptionUtil;
	private @Autowired IConnectionManager iConnectionManager;
	
	@Override
	public ServiceSession serviceUserAuthentication(String authenticationURL, ServiceTicket ticket, String username) throws IOException, RestClientException{
		
		log.info("Entering serviceUserAuthentication");
		
		if (authenticationURL == null || authenticationURL.isEmpty() || ticket == null){
			log.error("Invalid Input parameters to serviceUserAuthentication");
			return null;
		}
		
		//Creating the Request Authenticator
		String requestAuthenticatorStr = iDateUtil.createAuthenticator();
		Date requestAuthenticator = iDateUtil.generateDateFromString(requestAuthenticatorStr);
		
		//creating the service session key
		SecretKey serviceSessionKey = iEncryptionUtil.generateSecretKey(ticket.getServiceSessionID());
		
		//Creating the Service Login Request
		log.debug("Creating the Service Login Request");
		ServiceLoginRequest request = iServiceTicketAuthenticationAPI.createServiceLoginRequest(requestAuthenticatorStr, ticket.getServiceTicketPacketAppLoginName(), 
																													   ticket.getServiceTicketPacketExpiryTime(),
																													   ticket.getServiceTicketPacketSessionID(),
																													   ticket.getServiceTicketPacketUsername(), 
																													   serviceSessionKey);
		
		//Requesting Service Login Request
		log.debug("Requesting Service Login Request");
		ServiceLoginResponse response = iConnectionManager.generateRequest(authenticationURL, RequestMethod.POST_REQUEST_METHOD, 
										ServiceLoginResponse.class,	iConnectionManager.generateJSONStringForObject(request));
		
		//Decrypting and validating Service Login Response
		log.debug("Decrypting and validating Service Login Response");
		Map<ServiceLoginResponseAttributes, String> responseAttributes = iServiceTicketAuthenticationAPI.decryptAndValidateServiceLoginResponseAttributes(response, 
				requestAuthenticator, serviceSessionKey);
		
		if (responseAttributes == null){
			return null;
		}
		
		//Creating the Service Session
		log.debug("Creating the Service Session");
		String latestAuthenticatorStr = responseAttributes.get(ServiceLoginResponseAttributes.RESPONSE_AUTHENTICATOR);
		Date latestAuthenticator = iDateUtil.generateDateFromString(latestAuthenticatorStr);
		String sessionID = responseAttributes.get(ServiceLoginResponseAttributes.SESSIONID);
		ServiceSession session = new ServiceSession(sessionID, ticket, username, latestAuthenticator);
		
		
		return session;
		
	}
	
	
}
