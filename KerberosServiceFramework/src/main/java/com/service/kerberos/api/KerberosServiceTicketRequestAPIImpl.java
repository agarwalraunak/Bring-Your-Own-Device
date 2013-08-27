package com.service.kerberos.api;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.service.kerberos.representation.ServiceTicketRequest;
import com.service.kerberos.representation.ServiceTicketResponse;
import com.service.model.kerberos.TGT;
import com.service.util.connectionmanager.IConnectionManager;
import com.service.util.dateutil.IDateUtil;
import com.service.util.encryption.IEncryptionUtil;
import com.service.util.hashing.IHashUtil;

/**
 * @author raunak
 *
 */
@Component
public class KerberosServiceTicketRequestAPIImpl implements IKerberosServiceTicketRequestAPI {
	
	private @Autowired IDateUtil iDateUtil;
	private @Autowired IEncryptionUtil iEncryptionUtil;
	private @Autowired IHashUtil iHashUtil;
	private @Autowired IConnectionManager iConnectionManager;
	
	public enum ServiceTicketResponseAttributes{
		SERVICE_TICKET_PACKET_APP_LOGIN_NAME, SERVICE_TICKET_PACKET_USERNAME,
		SERVICE_TICKET_PACKET_EXPIRY_TIME, SERVICE_TICKET_PACKET_SERVICE_SESSION_ID, SERVICE_SESSION_ID, AUTHENTICATOR;
	}
	
	@Override
	public ServiceTicketRequest createServiceTicketRequest(String serviceName, TGT tgt, String requestAuthenticator, SecretKey sessionKey){
		
		if (serviceName == null || serviceName.isEmpty() || tgt == null || sessionKey == null || 
				requestAuthenticator == null || requestAuthenticator.isEmpty()){
			return null;
		}
		
		String[] encryptedData = iEncryptionUtil.encrypt(sessionKey, serviceName, requestAuthenticator);
		
		ServiceTicketRequest request = new ServiceTicketRequest();
		request.setEncServiceName(encryptedData[0]);
		request.setTgtPacketAppLoginName(tgt.getTgtPacketAppLoginName());
		request.setTgtPacketExpiryTime(tgt.getTgtPacketExpiryTime());
		request.setTgtPacketSessionID(tgt.getTgtPacketSessionID());
		request.setTgtPacketUsername(tgt.getTgtPacketUsername());
		request.setEncRequestAuthenticator(encryptedData[1]);
		
		return request;
	}
	
	@Override
	public Map<ServiceTicketResponseAttributes, String> decryptAndValidateServiceTicketResponseAttributes(ServiceTicketResponse response, 
			Date requestAuthenticator, SecretKey sessionKey){
		
		if (response == null || sessionKey == null){
			return null;
		}
		
		//Decrypting the Attributes
		String[] decryptedData = iEncryptionUtil.decrypt(sessionKey, response.getEncServiceSessionID(), 
														 response.getServiceTicketPacketAppLoginName(), 
														 response.getServiceTicketPacketExpiryTime(),
														 response.getServiceTicketPacketServiceSessionID(),
														 response.getEncResponseAuthenticator());
		
		String decryptedUsername = iEncryptionUtil.decrypt(sessionKey, response.getServiceTicketPacketUsername())[0];
		
		//Validating the Decrypted Attributes
		if (!iEncryptionUtil.validateDecryptedAttributes(decryptedData)){
			return null;
		}
		
		//Validating the Authenticator
		String responseAuthenticatorStr = decryptedData[4];
		Date responseAuthenticator = iDateUtil.generateDateFromString(responseAuthenticatorStr);
		if (!iDateUtil.validateAuthenticator(responseAuthenticator, requestAuthenticator)){
			return null;
		}
		
		Map<ServiceTicketResponseAttributes, String> decryptedDataMap = new HashMap<>();
		
		decryptedDataMap.put(ServiceTicketResponseAttributes.SERVICE_SESSION_ID, decryptedData[0]);
		decryptedDataMap.put(ServiceTicketResponseAttributes.SERVICE_TICKET_PACKET_APP_LOGIN_NAME, decryptedData[1]);
		decryptedDataMap.put(ServiceTicketResponseAttributes.SERVICE_TICKET_PACKET_EXPIRY_TIME, decryptedData[2]);
		decryptedDataMap.put(ServiceTicketResponseAttributes.SERVICE_TICKET_PACKET_SERVICE_SESSION_ID, decryptedData[3]);
		decryptedDataMap.put(ServiceTicketResponseAttributes.AUTHENTICATOR, decryptedData[4]);
		decryptedDataMap.put(ServiceTicketResponseAttributes.SERVICE_TICKET_PACKET_USERNAME, decryptedUsername);
		
		return decryptedDataMap;
	}	
}