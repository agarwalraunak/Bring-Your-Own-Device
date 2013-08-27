package com.kerberos.rest.api;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kerberos.rest.representation.ServiceTicketRequest;
import com.kerberos.rest.representation.ServiceTicketResponse;
import com.kerberos.util.dateutil.IDateUtil;
import com.kerberos.util.encryption.IEncryptionUtil;

@Component
public class TGSAPIImpl implements ITGSAPI{
	
	private @Autowired IEncryptionUtil iEncryptionUtil;
	private @Autowired IDateUtil iDateUtil;
	public enum ServiceTicketRequestAttributes{
		TGT_PACKET_APP_LOGIN_NAME,
		TGT_PACKET_USERNAME,
		TGT_PACKET_SESSIONID,
		TGT_PACKET_EXPIRY_TIME,
		SERVICE_NAME,
		REQUEST_AUTHENTICATOR;
	}
	
	@Override
	public Map<ServiceTicketRequestAttributes, String>
	decryptAndValidateServiceTicketRequestAttributes(ServiceTicketRequest request, SecretKey kerberosMasterKey){
		
		if (request == null || kerberosMasterKey == null){
			return null;
		}
		
		String encTGTPacketAppLoginName = request.getTgtPacketAppLoginName();
		String encTGTPacketExpiryTime = request.getTgtPacketExpiryTime();
		String encTGTPacketSessionID = request.getTgtPacketSessionID();;
		String encTGTPacketUsername = request.getTgtPacketUsername();
		
		//Decrypting the TGT Packet Attributes with the Kerberos Master Key
		String[] decryptedTGTPacket = iEncryptionUtil.decrypt(kerberosMasterKey, encTGTPacketAppLoginName, 
																				 encTGTPacketExpiryTime, 
																				 encTGTPacketSessionID);

		//Valdiating the TGT Packet Attributes decrypted with the Kerberos Master Key
		if (!iEncryptionUtil.validateDecryptedAttributes(decryptedTGTPacket)){
			return null;
		}
		
		//Decrypting the TGT Packet Username if not null
		String decryptedTGTPacketUsername = null;
		if (encTGTPacketUsername != null && !encTGTPacketUsername.isEmpty())
			decryptedTGTPacketUsername = iEncryptionUtil.decrypt(kerberosMasterKey, encTGTPacketUsername)[0];
		
		//Generating SecretKey from TGT Packet Session ID and Decrypting the Service Ticket Request attributes
		String sessionID = decryptedTGTPacket[2];
		SecretKey sessionSecretKey = iEncryptionUtil.generateSecretKey(sessionID);
		String[] decryptedData = iEncryptionUtil.decrypt(sessionSecretKey, request.getEncServiceName(), request.getEncRequestAuthenticator());
		//Validating the Decrypted Attributes
		if (!iEncryptionUtil.validateDecryptedAttributes(decryptedData))
			return null;
		
		String serviceName = decryptedData[0];
		String requestAuthenticatorStr = decryptedData[1];
		
		//validating the request authenticator
		Date requestAuthenticator = iDateUtil.generateDateFromString(requestAuthenticatorStr);
		if (!iDateUtil.validateAuthenticator(requestAuthenticator)){
			return null;
		}
		
		Map<ServiceTicketRequestAttributes, String> decryptedServiceTicketRequestAttributes = new HashMap<>();
		decryptedServiceTicketRequestAttributes.put(ServiceTicketRequestAttributes.TGT_PACKET_USERNAME, decryptedTGTPacketUsername);
		decryptedServiceTicketRequestAttributes.put(ServiceTicketRequestAttributes.TGT_PACKET_APP_LOGIN_NAME, decryptedTGTPacket[0]);
		decryptedServiceTicketRequestAttributes.put(ServiceTicketRequestAttributes.TGT_PACKET_EXPIRY_TIME, decryptedTGTPacket[1]);
		decryptedServiceTicketRequestAttributes.put(ServiceTicketRequestAttributes.TGT_PACKET_SESSIONID, decryptedTGTPacket[2]);
		decryptedServiceTicketRequestAttributes.put(ServiceTicketRequestAttributes.SERVICE_NAME, serviceName);
		decryptedServiceTicketRequestAttributes.put(ServiceTicketRequestAttributes.REQUEST_AUTHENTICATOR, requestAuthenticatorStr);
		
		return decryptedServiceTicketRequestAttributes;
	}
	
	@Override
	public ServiceTicketResponse createServiceTicketResponse(String serviceSessionID, String appLoginName, String username, 
			String serviceTicketExpiryTime, String responseAuthenticatorStr, SecretKey serviceKey, SecretKey sessionKey){
		
		String[] serviceKeyEncryptedServiceTicketPacket = iEncryptionUtil.encrypt(serviceKey,
																				  serviceSessionID, 
																				  appLoginName, 
																				  username, 
																				  serviceTicketExpiryTime);

		String[] serviceSessionKeyEncryptedServiceTicketPacket = iEncryptionUtil.encrypt(sessionKey, serviceKeyEncryptedServiceTicketPacket);
		
		String[] sessionKeyEncryptedData = iEncryptionUtil.encrypt(sessionKey, serviceSessionID, responseAuthenticatorStr);
		
		ServiceTicketResponse response = new ServiceTicketResponse();
		
		response.setEncServiceSessionID(sessionKeyEncryptedData[0]);
		response.setEncResponseAuthenticator(sessionKeyEncryptedData[1]);
		
		response.setServiceTicketPacketServiceSessionID(serviceSessionKeyEncryptedServiceTicketPacket[0]);
		response.setServiceTicketPacketAppLoginName(serviceSessionKeyEncryptedServiceTicketPacket[1]);
		response.setServiceTicketPacketUsername(serviceSessionKeyEncryptedServiceTicketPacket[2]);
		response.setServiceTicketPacketExpiryTime(serviceSessionKeyEncryptedServiceTicketPacket[3]);
		
		return response;
	}
	
	

}
