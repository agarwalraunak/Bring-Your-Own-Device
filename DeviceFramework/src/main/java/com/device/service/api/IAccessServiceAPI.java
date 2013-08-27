package com.device.service.api;

import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

import com.device.service.representation.ServiceAccessRequest;

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
	 */
	Date decryptAndValidateServiceAccessResponseAuthenticator(
			String encResponseAuthenticator, Date requestAuthenticator,
			SecretKey sessionKey);

}
