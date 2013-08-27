package com.service.kerberos.api;

import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

import com.service.app.rest.representation.ServiceAccessRequest;
import com.service.exception.common.InternalSystemException;

public interface IAccessServiceAPI {

	/**
	 * @param requestAuthenticatorStr
	 * @param requestData
	 * @param serviceTicketPacketAppLoginName
	 * @param sessionKey
	 * @return
	 */
	ServiceAccessRequest createServiceAccessRequest(
			String requestAuthenticatorStr, Map<String, String> requestData,
			String serviceTicketPacketAppLoginName, SecretKey sessionKey);

	/**
	 * @param encResponseAuthenticator
	 * @param requestAuthenticator
	 * @param sessionKey
	 * @return
	 * @throws InternalSystemException 
	 */
	Date decryptAndValidateServiceAccessResponseAuthenticator(
			String encResponseAuthenticator, Date requestAuthenticator,
			SecretKey sessionKey) throws InternalSystemException;

}
