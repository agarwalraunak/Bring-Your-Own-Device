/**
 * 
 */
package com.service.util.hashing;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author raunak
 *
 */
@Component
@Scope(value=ConfigurableBeanFactory.SCOPE_SINGLETON)
public class HashUtilImpl implements IHashUtil{
	
	public enum HashingTechqniue{
		SSHA256("SHA-256"), MD5("MD5");
		
		private String value;
		
		private HashingTechqniue(String value){
			this.value = value;
		}
		
		public String toString(){
			return value;
		}
	}


	@Override
	public byte[] getHash(String input, HashingTechqniue technique){
		
		if (input == null || input .isEmpty() || technique == null){
			return null;
		}
		
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance(technique.value);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
		digest.reset();
		byte[] hashedBytes = digest.digest(stringToByte(input));
		return hashedBytes;
	}

	@Override
	public byte[] stringToByte(String input) {
		
		if (input == null || input.isEmpty()){
			return null;
		}
		if (Base64.isBase64(input)) {
			return Base64.decodeBase64(input);

		} else {
			return Base64.encodeBase64(input.getBytes());
		}
	}

	@Override
	public String bytetoBase64String(byte[] input) {
		if(input == null){
			return null;
		}
		return Base64.encodeBase64String(input);
	}

	@Override
	public byte[] generateSalt() {
		SecureRandom random = new SecureRandom();
		byte bytes[] = new byte[20];
		random.nextBytes(bytes);
		return bytes;
	}

	@Override
	public byte[] getHashWithSalt(String input, HashingTechqniue technique, byte[] salt) {
		
		if (input == null || input.isEmpty() || technique == null || salt == null){
			return null;
		}
		
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance(technique.value);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
		digest.reset();
		digest.update(salt);
		byte[] hashedBytes = digest.digest(stringToByte(input));
		return hashedBytes;
	}
	
	@Override
	public String getSessionKey() {
		return bytetoBase64String(generateSalt());
	}
}
