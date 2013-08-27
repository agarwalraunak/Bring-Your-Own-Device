/**
 * 
 */
package com.kerberos.rest.api;

import javax.crypto.SecretKey;

import com.kerberos.rest.representation.AuthenticationResponse;

/**
 * @author raunak
 *
 */
public interface IKDCAPI {

	/**
	 * @param appLoginName
	 * @param appPassword
	 * @param username
	 * @param userPassword
	 * @return
	 */
	SecretKey generatePasswordSymmetricKey(String appLoginName,
			String appPassword, String username, String userPassword);

	/**
	 * @param appLoginName
	 * @param username
	 * @param expiryTime
	 * @param sessionID
	 * @param passwordSecretKey
	 * @param kerberosMasterKey
	 * @return
	 */
	AuthenticationResponse createAuthenticationResponse(String appLoginName,
			String username, String expiryTime, String sessionID,
			SecretKey passwordSecretKey, SecretKey kerberosMasterKey);

}
