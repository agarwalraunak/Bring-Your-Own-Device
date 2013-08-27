/**
 * 
 */
package com.kerberos.keyserver.rest.api;

import java.util.Date;

import javax.crypto.SecretKey;

import com.kerberos.keyserver.model.app.Session;
import com.kerberos.keyserver.rest.representation.ServiceLoginResponse;

/**
 * @author raunak
 *
 */
public interface IAuthenticationAPI {


	/**
	 * @param appLoginName
	 * @param serviceSessionID
	 * @param serviceTicketExpirationString
	 * @return boolean true if validation was successfull else false
	 */
	boolean validateServiceTicket(String appLoginName, String serviceSessionID,
			String serviceTicketExpirationString);

	/**
	 * @param session
	 * @param requestAuthenticator
	 * @param serviceSessionKey
	 * @return
	 */
	ServiceLoginResponse createAppAuthenticationResponse(Session session,
			Date requestAuthenticator, SecretKey serviceSessionKey);

}
