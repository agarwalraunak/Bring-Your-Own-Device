/**
 * 
 */
package com.device.service.api;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.device.rest.representation.ServiceLoginRequest;
import com.device.rest.representation.ServiceLoginResponse;
import com.device.util.dateutil.IDateUtil;
import com.device.util.encryption.IEncryptionUtil;

/**
 * @author raunak
 *
 */
@Component
public class ServiceTicketAuthenticationAPIImpl implements IServiceTicketAuthenticationAPI{

	private @Autowired IEncryptionUtil iEncryptionUtil;
	private @Autowired IDateUtil iDateUtil;
	
	public enum ServiceLoginResponseAttributes{
		RESPONSE_AUTHENTICATOR, SESSIONID;
	}
	
	@Override
	public ServiceLoginRequest createServiceLoginRequest(String requestAuthenticator, String serviceTicketPacketAppLoginName, 
			String serviceTicketPacketExpiryTime, String serviceTicketPacketServiceSessionID, String serviceTicketPacketUsername, 
			SecretKey serviceSessionKey){
		
		String encRequestAuthenticator = iEncryptionUtil.encrypt(serviceSessionKey, requestAuthenticator)[0];
		
		ServiceLoginRequest request = new ServiceLoginRequest();
		request.setEncRequestAuthenticator(encRequestAuthenticator);
		request.setServiceTicketPacketAppLoginName(serviceTicketPacketAppLoginName);
		request.setServiceTicketPacketExpiryTime(serviceTicketPacketExpiryTime);
		request.setServiceTicketPacketServiceSessionID(serviceTicketPacketServiceSessionID);
		request.setServiceTicketPacketUsername(serviceTicketPacketUsername);
		
		return request;
	}
	
	@Override
	public Map<ServiceLoginResponseAttributes, String> decryptAndValidateServiceLoginResponseAttributes(ServiceLoginResponse response,
			Date requestAuthenticator, SecretKey serviceSessionKey){
		
		String[] decryptedData = iEncryptionUtil.decrypt(serviceSessionKey, response.getEncResponseAuthenticator(), 
																			response.getEncSessionID());
		
		if (!iEncryptionUtil.validateDecryptedAttributes(decryptedData)){
			return null;
		}
		
		String responseAuthenticatorStr = decryptedData[0];
		Date responseAuthenticator = iDateUtil.generateDateFromString(responseAuthenticatorStr);
		if (!iDateUtil.validateAuthenticator(responseAuthenticator, requestAuthenticator)){
			return null;
		}
		
		
		Map<ServiceLoginResponseAttributes, String> serviceLoginResponseAttributes = new HashMap<>();
		
		serviceLoginResponseAttributes.put(ServiceLoginResponseAttributes.RESPONSE_AUTHENTICATOR, responseAuthenticatorStr);
		serviceLoginResponseAttributes.put(ServiceLoginResponseAttributes.SESSIONID, decryptedData[1]);
		
		return serviceLoginResponseAttributes;
	}
	
	
}
