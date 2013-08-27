/**
 * 
 */
package com.kerberos.keyserver.rest.api;

import java.util.Date;

import javax.crypto.SecretKey;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kerberos.keyserver.model.app.Session;
import com.kerberos.keyserver.rest.representation.ServiceLoginResponse;
import com.kerberos.util.dateutil.IDateUtil;
import com.kerberos.util.encryption.IEncryptionUtil;

/**
 * @author raunak
 *
 */
@Component
public class AuthenticationAPIImpl  implements IAuthenticationAPI{
	
	private static Logger log = Logger.getLogger(AuthenticationAPIImpl.class);
		
	private @Autowired IEncryptionUtil iEncryptionUtil;
	private @Autowired IDateUtil iDateUtil;
	
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