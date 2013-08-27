/**
 * 
 */
package com.device.rest.api;

import java.util.Map;

import javax.crypto.SecretKey;

import com.device.rest.api.KerberosAuthenticationAPIImpl.AuthenticationResponseAttributes;
import com.device.rest.representation.AuthenticationRequest;
import com.device.rest.representation.AuthenticationResponse;

/**
 * It provides the methods necessary for performing <strong>Kerberos Authentication</strong>
 * 
 * @author raunak
 *
 */
public interface IKerberosAuthenticationAPI {


	/**
	 * Creates and returns an instance of <code>AuthenticationRequest</code>
	 * @param <code>String</code> appLoginName
	 * @param <code>String</code> username
	 * @return <code>AuthenticationRequest</code>
	 */
	AuthenticationRequest createAuthenticationRequest(String appLoginName,
			String username);

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
	 * @param response
	 * @param passwordKey
	 * @return
	 */
	Map<AuthenticationResponseAttributes, String> decryptAuthenticationResponseAttributes(
			AuthenticationResponse response, SecretKey passwordKey);
}
