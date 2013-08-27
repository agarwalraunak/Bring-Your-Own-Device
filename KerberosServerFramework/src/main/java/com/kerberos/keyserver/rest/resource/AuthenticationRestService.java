/**
 * 
 */
package com.kerberos.keyserver.rest.resource;

import java.io.IOException;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kerberos.exceptions.AuthenticatorValidationException;
import com.kerberos.exceptions.InternalSystemException;
import com.kerberos.exceptions.InvalidRequestException;
import com.kerberos.exceptions.RestClientException;
import com.kerberos.keyserver.model.SessionManager;
import com.kerberos.keyserver.model.app.Session;
import com.kerberos.keyserver.rest.api.IAuthenticationAPI;
import com.kerberos.keyserver.rest.representation.ServiceLoginRequest;
import com.kerberos.keyserver.rest.representation.ServiceLoginResponse;
import com.kerberos.util.ActiveDirectory.ActiveDirectoryImpl.SecretKeyType;
import com.kerberos.util.dateutil.IDateUtil;
import com.kerberos.util.encryption.IEncryptionUtil;
import com.kerberos.util.keyserver.KeyServerUtil;

/**
 * @author raunak
 *
 */
@Component
@Path("/authenticate")
public class AuthenticationRestService {
	
	private static Logger log = Logger.getLogger(AuthenticationRestService.class);
	
	
	private @Autowired SessionManager sessionDirectory;
	
	private @Autowired IDateUtil iDateUtil;
	private @Autowired IEncryptionUtil iEncryptionUtil; 
	private @Autowired KeyServerUtil keyServerUtil;
	
	private @Autowired IAuthenticationAPI iAuthenticationAPI;
	/**
	 * Authenticates the App 
	 * @param request: AppAuthenticationRequest
	 * @return
	 * @throws InternalSystemException 
	 * @throws RestClientException 
	 * @throws IOException 
	 * @throws InvalidRequestException 
	 * @throws AuthenticatorValidationException 
	 */
	@Path("/serviceTicket")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ServiceLoginResponse authenticate(ServiceLoginRequest request, @Context HttpServletRequest httpRequest) throws InternalSystemException, 
			 IOException, RestClientException, InvalidRequestException {
		
		log.debug("Entering authenticateApp method");
		
		SecretKey serviceKey = keyServerUtil.getKeyFromKeyStore(null, SecretKeyType.KEY_SERVER);
				
		String appLoginName = iEncryptionUtil.decrypt(serviceKey, request.getServiceTicketPacketAppLoginName())[0];
		String username = iEncryptionUtil.decrypt(serviceKey, request.getServiceTicketPacketUsername())[0];
		String serviceSessionID = iEncryptionUtil.decrypt(serviceKey, request.getServiceTicketPacketServiceSessionID())[0];
		String expireTime = iEncryptionUtil.decrypt(serviceKey, request.getServiceTicketPacketExpiryTime())[0];
		
		//Validating the Decrypted Service Ticket Packet
		if (!iAuthenticationAPI.validateServiceTicket(appLoginName, serviceSessionID, expireTime)){
			log.error("Validation of Decrypted Service Ticket Packet failed!");
			throw new InvalidRequestException();
		}
		
		String encAuthenticator = request.getEncRequestAuthenticator();
		//Decrypt the Authenticator
		SecretKey serviceSessionKey = iEncryptionUtil.generateSecretKey(serviceSessionID);
		String requestAuthenticatorStr = iEncryptionUtil.decrypt(serviceSessionKey, encAuthenticator)[0];
		Date requestAuthenticator = iDateUtil.generateDateFromString(requestAuthenticatorStr);
		
		//Check if the session for the application already exists else create
		Session session = sessionDirectory.findActiveSessionByAppID(appLoginName);
		if (session == null){
			session = sessionDirectory.createSession(appLoginName, username, serviceSessionID, httpRequest.getRemoteAddr(), requestAuthenticator);
		}
		//Add the authenticator to the App Session
		session.setLatestAuthenticator(requestAuthenticator);
				
		//Creating AppAuthenticationResponse
		ServiceLoginResponse response = iAuthenticationAPI.createAppAuthenticationResponse(session, requestAuthenticator, serviceSessionKey);

		return response;
	}
	
}