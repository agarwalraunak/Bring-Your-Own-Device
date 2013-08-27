package com.device.service.api;

import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

import com.device.rest.representation.ServiceLoginRequest;
import com.device.rest.representation.ServiceLoginResponse;
import com.device.service.api.ServiceTicketAuthenticationAPIImpl.ServiceLoginResponseAttributes;

public interface IServiceTicketAuthenticationAPI {

	/**
	 * @param requestAuthenticator
	 * @param serviceTicketPacketAppLoginName
	 * @param serviceTicketPacketExpiryTime
	 * @param serviceTicketPacketServiceSessionID
	 * @param serviceTicketPacketUsername
	 * @param serviceSessionKey
	 * @return
	 */
	ServiceLoginRequest createServiceLoginRequest(String requestAuthenticator,
			String serviceTicketPacketAppLoginName,
			String serviceTicketPacketExpiryTime,
			String serviceTicketPacketServiceSessionID,
			String serviceTicketPacketUsername, SecretKey serviceSessionKey);

	/**
	 * @param response
	 * @param requestAuthenticator
	 * @param serviceSessionKey
	 * @return
	 */
	Map<ServiceLoginResponseAttributes, String> decryptAndValidateServiceLoginResponseAttributes(
			ServiceLoginResponse response, Date requestAuthenticator,
			SecretKey serviceSessionKey);

}
