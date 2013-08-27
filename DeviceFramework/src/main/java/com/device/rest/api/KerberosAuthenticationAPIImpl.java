/**
 * 
 */
package com.device.rest.api;

import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.device.rest.representation.AuthenticationRequest;
import com.device.rest.representation.AuthenticationResponse;
import com.device.util.connectionmanager.IConnectionManager;
import com.device.util.dateutil.IDateUtil;
import com.device.util.encryption.IEncryptionUtil;
import com.device.util.hashing.IHashUtil;
import com.device.util.hashing.HashUtilImpl.HashingTechqniue;

/**
 * @author raunak
 *
 */
@Component
public class KerberosAuthenticationAPIImpl implements IKerberosAuthenticationAPI{
	
	private @Autowired IEncryptionUtil iEncryptionUtil;
	private @Autowired IHashUtil iHashUtil;
	private @Autowired IConnectionManager iConnectionManager;
	private @Autowired IDateUtil iDateUtil;
	
	public enum AuthenticationResponseAttributes{
		TGT_PACKET_APP_LOGIN_NAME, TGT_PACKET_USERNAME, TGT_PACKET_EXPIRY_TIME, TGT_PACKET_SESSIONID, SESSION_KEY;
	}
	
	@Override
	public AuthenticationRequest createAuthenticationRequest(String appLoginName, String username){
		if (appLoginName == null || appLoginName.isEmpty()){
			return null;
		}
		AuthenticationRequest request = new AuthenticationRequest();
		request.setAppLoginName(appLoginName);
		request.setUsername(username);
		return request;
	}
	
	@Override
	public SecretKey generatePasswordSymmetricKey(String appLoginName, String appPassword, String username, String userPassword){
		
		if (appLoginName == null || appLoginName.isEmpty()){
			return null;
		}
		
		
		byte[] hashedAppCredentialBytes = iHashUtil.getHashWithSalt(appPassword, HashingTechqniue.SSHA256, 
				iHashUtil.stringToByte(appLoginName));
		String hashedAppCredentialPhrase = iHashUtil.bytetoBase64String(hashedAppCredentialBytes);

		String hashedUserCredentialPhrase = null;
		if (appPassword != null && !appPassword.isEmpty() && username != null || !username.isEmpty()){
			byte[] hashedUserCredentialBytes = iHashUtil.getHashWithSalt(userPassword, HashingTechqniue.SSHA256, 
					iHashUtil.stringToByte(username));
			hashedUserCredentialPhrase = iHashUtil.bytetoBase64String(hashedUserCredentialBytes);
		}
		
		byte[] hashedKeyBytes;
		if (hashedUserCredentialPhrase != null){
			hashedKeyBytes = iHashUtil.getHashWithSalt(hashedAppCredentialPhrase, HashingTechqniue.SSHA256,
										iHashUtil.stringToByte(hashedUserCredentialPhrase));
		}
		else{
			hashedKeyBytes = iHashUtil.getHash(hashedAppCredentialPhrase, HashingTechqniue.SSHA256);
		}
			
		
		String hashedKeyString = iHashUtil.bytetoBase64String(hashedKeyBytes);
		return iEncryptionUtil.generateSecretKey(hashedKeyString);
	}
	
	@Override
	public Map<AuthenticationResponseAttributes, String> decryptAuthenticationResponseAttributes(AuthenticationResponse response, SecretKey passwordKey){
		
		if (response == null || passwordKey == null){
			return null;
		}
		
		String[] decryptedData = iEncryptionUtil.decrypt(passwordKey, response.getEncSessionID(), response.getTgtPacketAppLoginName(), 
				response.getTgtPacketExpiryTime(), response.getTgtPacketSessionID());
		
		String decUsername = null;
		if (response.getTgtPacketUsername() != null && !response.getTgtPacketUsername().isEmpty()){
			decUsername = iEncryptionUtil.decrypt(passwordKey, response.getTgtPacketUsername())[0];
		}
		
		if (!iEncryptionUtil.validateDecryptedAttributes(decryptedData)){
			return null;
		}
		
		Map<AuthenticationResponseAttributes, String> decryptedDataMap = new HashMap<>();
		
		decryptedDataMap.put(AuthenticationResponseAttributes.SESSION_KEY, decryptedData[0]);
		decryptedDataMap.put(AuthenticationResponseAttributes.TGT_PACKET_APP_LOGIN_NAME, decryptedData[1]);
		decryptedDataMap.put(AuthenticationResponseAttributes.TGT_PACKET_EXPIRY_TIME, decryptedData[2]);
		decryptedDataMap.put(AuthenticationResponseAttributes.TGT_PACKET_SESSIONID, decryptedData[3]);
		decryptedDataMap.put(AuthenticationResponseAttributes.TGT_PACKET_USERNAME, decUsername);
		
		return decryptedDataMap;
	}
}