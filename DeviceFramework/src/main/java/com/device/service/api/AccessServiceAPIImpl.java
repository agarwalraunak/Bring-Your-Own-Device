/**
 * 
 */
package com.device.service.api;

import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.device.service.representation.ServiceAccessRequest;
import com.device.util.dateutil.IDateUtil;
import com.device.util.encryption.IEncryptionUtil;

/**
 * @author raunak
 *
 */
@Component
public class AccessServiceAPIImpl implements IAccessServiceAPI {
	
	private @Autowired IEncryptionUtil iEncryptionUtil;
	private @Autowired IDateUtil iDateUtil;
	
	public enum ServiceAccessResponseAttributes{
		RESPONSE_AUTHENTICATOR, RESPONSE_DATA;
	}

	@Override
	public ServiceAccessRequest createServiceAccessRequest(String requestAuthenticatorStr, Map<String, String> requestData, 
			String serviceTicketPacketAppLoginName, SecretKey sessionKey){
		
		String encRequestAuthenticator = iEncryptionUtil.encrypt(sessionKey, requestAuthenticatorStr)[0];
		Map<String, String> encData = iEncryptionUtil.encrypt(sessionKey, requestData);
		
		ServiceAccessRequest request = new ServiceAccessRequest();
		request.setEncAppLoginName(serviceTicketPacketAppLoginName);
		request.setEncRequestAuthenticator(encRequestAuthenticator);
		request.setEncData(encData);
		
		return request;
	}
	
	@Override
	public Date decryptAndValidateServiceAccessResponseAuthenticator(String encResponseAuthenticator, Date requestAuthenticator, SecretKey sessionKey) {
		
		String responseAuthenticatorStr = iEncryptionUtil.decrypt(sessionKey, encResponseAuthenticator)[0];
		if (responseAuthenticatorStr == null || responseAuthenticatorStr.isEmpty()){
			return null;
		}
		Date responseAuthenticator = iDateUtil.generateDateFromString(responseAuthenticatorStr);
		if (iDateUtil.validateAuthenticator(responseAuthenticator, requestAuthenticator)){
			return null;
		}
		
		return responseAuthenticator;
	}
	
}
