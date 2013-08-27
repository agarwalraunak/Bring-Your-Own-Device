/**
 * 
 */
package com.kerberos.rest.api;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kerberos.rest.representation.AuthenticationResponse;
import com.kerberos.util.encryption.IEncryptionUtil;
import com.kerberos.util.hashing.HashUtilImpl.HashingTechqniue;
import com.kerberos.util.hashing.IHashUtil;

/**
 * @author raunak
 *
 */
@Component
public class KDCAPIImpl implements IKDCAPI{

	private @Autowired IEncryptionUtil iEncryptionUtil;
	private @Autowired IHashUtil iHashUtil;
	
	@Override
	public SecretKey generatePasswordSymmetricKey(String appLoginName, String appPassword, String username, String userPassword){
		
		if (appLoginName == null || appLoginName.isEmpty()){
			return null;
		}
//		
//		StringBuilder keyBuilder = new StringBuilder(appPassword);
//		if (userPassword != null && !userPassword.isEmpty()){
//			keyBuilder.append(userPassword);
//		}
//		
//		String keyString = keyBuilder.toString();
//		
//		StringBuilder saltBuilder = new StringBuilder(appLoginName);
//		if (username != null && !username.isEmpty()){
//			saltBuilder.append(username);
//		}
//		
//		String saltString = saltBuilder.toString();
		
		byte[] hashedKeyBytes = null;
		if (userPassword != null && !userPassword.isEmpty()){
			hashedKeyBytes = iHashUtil.getHashWithSalt(appPassword, HashingTechqniue.SSHA256, iHashUtil.stringToByte(userPassword));
		}
		else{
			hashedKeyBytes = iHashUtil.getHash(appPassword, HashingTechqniue.SSHA256);
		}
		
		
		String hashedKeyString = iHashUtil.bytetoBase64String(hashedKeyBytes);
		
		return iEncryptionUtil.generateSecretKey(hashedKeyString);
	}
	
	@Override
	public AuthenticationResponse createAuthenticationResponse(String appLoginName, String username, String expiryTime, 
		String sessionID, SecretKey passwordSecretKey, SecretKey kerberosMasterKey){
		
		if (appLoginName == null || appLoginName.isEmpty() || sessionID == null || sessionID.isEmpty() || expiryTime == null
				|| expiryTime.isEmpty() || passwordSecretKey == null || kerberosMasterKey == null){
			return null;
		}
	
		String[] masterKeyEncryptedData = iEncryptionUtil.encrypt(kerberosMasterKey, appLoginName, username, expiryTime, sessionID);
		String[] encryptedTGTPacket = iEncryptionUtil.encrypt(passwordSecretKey, masterKeyEncryptedData);
		
		String encSessionID = iEncryptionUtil.encrypt(passwordSecretKey, sessionID)[0];
		
		AuthenticationResponse response = new AuthenticationResponse();
		response.setEncSessionID(encSessionID);
		response.setTgtPacketAppLoginName(encryptedTGTPacket[0]);
		response.setTgtPacketUsername(encryptedTGTPacket[1]);
		response.setTgtPacketExpiryTime(encryptedTGTPacket[2]);
		response.setTgtPacketSessionID(encryptedTGTPacket[3]);
		
		return response;
	}
}
