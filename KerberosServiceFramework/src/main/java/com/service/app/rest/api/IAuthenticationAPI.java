/**
 * 
 */
package com.service.app.rest.api;

import java.io.IOException;
import java.util.Date;

import javax.crypto.SecretKey;

import com.service.app.rest.representation.ServiceLoginResponse;
import com.service.exception.RestClientException;
import com.service.exception.common.InternalSystemException;
import com.service.model.Session;

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

	/**
	 * @return
	 * @throws InternalSystemException 
	 * @throws RestClientException 
	 * @throws IOException 
	 */
	SecretKey callKeyServerForSecretKeyUtility() throws InternalSystemException, IOException, RestClientException;

}
