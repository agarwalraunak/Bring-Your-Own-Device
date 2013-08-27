package com.kerberos.rest.api;

import java.util.Map;

import javax.crypto.SecretKey;

import com.kerberos.rest.api.TGSAPIImpl.ServiceTicketRequestAttributes;
import com.kerberos.rest.representation.ServiceTicketRequest;
import com.kerberos.rest.representation.ServiceTicketResponse;

public interface ITGSAPI {

	/**
	 * @param request
	 * @param kerberosMasterKey
	 * @return
	 */
	Map<ServiceTicketRequestAttributes, String> decryptAndValidateServiceTicketRequestAttributes(
			ServiceTicketRequest request, SecretKey kerberosMasterKey);

	/**
	 * @param serviceSessionID
	 * @param appLoginName
	 * @param username
	 * @param serviceTicketExpiryTime
	 * @param responseAuthenticatorStr
	 * @param serviceKey
	 * @param sessionKey
	 * @return
	 */
	ServiceTicketResponse createServiceTicketResponse(String serviceSessionID,
			String appLoginName, String username,
			String serviceTicketExpiryTime, String responseAuthenticatorStr,
			SecretKey serviceKey, SecretKey sessionKey);

}
