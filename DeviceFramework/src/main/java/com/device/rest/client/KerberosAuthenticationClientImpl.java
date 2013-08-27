/**
 * 
 */
package com.device.rest.client;

import java.io.IOException;
import java.util.Map;

import javax.crypto.SecretKey;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.device.configuration.ApplicationDetailService;
import com.device.configuration.KerberosURLConfig;
import com.device.exception.ApplicationDetailServiceInitializationException;
import com.device.exception.RestClientException;
import com.device.model.KerberosSession;
import com.device.model.SessionManager;
import com.device.rest.api.IKerberosAuthenticationAPI;
import com.device.rest.api.KerberosAuthenticationAPIImpl.AuthenticationResponseAttributes;
import com.device.rest.representation.AuthenticationRequest;
import com.device.rest.representation.AuthenticationResponse;
import com.device.util.connectionmanager.IConnectionManager;
import com.device.util.connectionmanager.ConnectionManagerImpl.RequestMethod;
import com.device.util.encryption.IEncryptionUtil;

/**
 * @author raunak
 *
 */

@Component
public class KerberosAuthenticationClientImpl implements IKerberosAuthenticationClient{
	
	private static Logger log = Logger.getLogger(KerberosAuthenticationClientImpl.class);
	
	private @Autowired IKerberosAuthenticationAPI iKerberosAuthenticationAPI;
	private @Autowired IConnectionManager iConnectionManager;
	private @Autowired IEncryptionUtil iEncryptionUtil;
	private @Autowired ApplicationDetailService applicationDetailService;
	private @Autowired SessionManager sessionManager;
	private @Autowired KerberosURLConfig kerberosURLConfig;
	
	@Override
	public KerberosSession kerberosAuthentication(String username, String userPassword) throws ApplicationDetailServiceInitializationException, IOException, RestClientException{

		log.info("Entering authenticateApp method");
		
		//Get the Configured App Credentials 
		String appLoginName = applicationDetailService.getAppLoginName();
		String appPassword = applicationDetailService.getAppPassword();
		
		if (appLoginName == null || appLoginName.isEmpty() || appPassword == null || appPassword.isEmpty()){
			log.error("Application Detail Service not initialized properly!");
			throw new ApplicationDetailServiceInitializationException();
		}
		
		//Creating the AuthenticationRequest
		log.debug("Creating the Authentication Request");
		AuthenticationRequest request = iKerberosAuthenticationAPI.createAuthenticationRequest(appLoginName, username);
		
		//Requesting the Kerberos Server for Authentication
		log.debug("Request the Kerberos Server for Authentication");
		AuthenticationResponse response = iConnectionManager.generateRequest(kerberosURLConfig.getKERBEROS_APP_AUTHENTICATION_URL(), 
				RequestMethod.POST_REQUEST_METHOD, AuthenticationResponse.class, iConnectionManager.generateJSONStringForObject(request));
		
		//Generating Password Key to decrypt Response
		log.debug("Generating Password Key to decrypt Authentication Response");
		SecretKey passwordKey = iKerberosAuthenticationAPI.generatePasswordSymmetricKey(appLoginName, appPassword, username, userPassword);
		
		//Decrypting Response Attributes
		log.debug("Decrypting Authentication Response Attributes");
		Map<AuthenticationResponseAttributes, String> decryptedResponseAttributes = iKerberosAuthenticationAPI.decryptAuthenticationResponseAttributes(response, passwordKey);
		
		//Validating Decrypted Response Attributes
		log.debug("Validating Decrypted Authentication Response Attributes");
		if  (decryptedResponseAttributes == null){
			return null;
		}
		
		//Creating Kerberos Session and TGT
		log.debug("Creating kerberos session and TGT");
		KerberosSession session = sessionManager.createKerberosSession(decryptedResponseAttributes.get(AuthenticationResponseAttributes.SESSION_KEY), 
									decryptedResponseAttributes.get(AuthenticationResponseAttributes.TGT_PACKET_APP_LOGIN_NAME), 
									decryptedResponseAttributes.get(AuthenticationResponseAttributes.TGT_PACKET_EXPIRY_TIME), 
									decryptedResponseAttributes.get(AuthenticationResponseAttributes.TGT_PACKET_SESSIONID), 
									decryptedResponseAttributes.get(AuthenticationResponseAttributes.TGT_PACKET_USERNAME));
		
		
		log.info("Returning from authenticateApp method");
		
		return session;
	}
	
}
