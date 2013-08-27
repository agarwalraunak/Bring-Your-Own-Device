/**
 * 
 */
package com.service.kerberos.api;

import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.service.app.rest.representation.ServiceAccessRequest;
import com.service.exception.common.InternalSystemException;
import com.service.util.dateutil.IDateUtil;
import com.service.util.encryption.IEncryptionUtil;

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
	public Date decryptAndValidateServiceAccessResponseAuthenticator(String encResponseAuthenticator, Date requestAuthenticator, SecretKey sessionKey) throws InternalSystemException{
		
		String responseAuthenticatorStr = iEncryptionUtil.decrypt(sessionKey, encResponseAuthenticator)[0];
		if (responseAuthenticatorStr == null || responseAuthenticatorStr.isEmpty()){
			throw new InternalSystemException();
		}
		Date responseAuthenticator = iDateUtil.generateDateFromString(responseAuthenticatorStr);
		if (!iDateUtil.validateAuthenticator(responseAuthenticator, requestAuthenticator)){
			throw new InternalSystemException();
		}
		
		return responseAuthenticator;
	}
	
}
