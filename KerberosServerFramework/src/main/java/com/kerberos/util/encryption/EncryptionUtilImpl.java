/**
 * 
 */
package com.kerberos.util.encryption;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Component;

/**
 * @author raunak
 *
 */
@Component
public class EncryptionUtilImpl implements IEncryptionUtil {
	
	
	private static final String UNICODE_FORMAT = "UTF-8";
	private static final String AES_ENCRYPTION_SCHEME = "AES/CBC/PKCS5Padding";
	private byte[] IV = new byte[] { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07,
            0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f};
	private final byte[] SALT = new byte[]{72, 34, 1, -98, 41, 68, -55, 34};
	private final int KEY_LENGTH = 128;
	private final int ITERATION_COUNT = 65536;
	
	
	@Override
	public SecretKey generateSecretKey(String encryptionKey) {
		
		if(encryptionKey == null || encryptionKey.isEmpty()){
			return null;
		}
		SecretKey secretKey = null;
		try {
			
			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			
			KeySpec spec = new PBEKeySpec(encryptionKey.toCharArray(), SALT, ITERATION_COUNT, KEY_LENGTH);
			SecretKey tmp = factory.generateSecret(spec);
			secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");

		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			e.printStackTrace();
		}
		
		return secretKey;
	}
	
	@Override
	public SecretKey generateSecretKeyFromBytes(byte[] keyBytes){
		SecretKey secretKey = new SecretKeySpec(keyBytes, "AES");
		return secretKey;
	}
	
	/**
	 * Method To Encrypt The String
	 */
	@Override
	public String[] encrypt(SecretKey key, String...input) {
		if (input == null || key == null) {
			return null;
		}
		if (input.length == 0){
			return null;
		}
		
		String[] encryptedInput = new String[input.length];
		try {
			Cipher cipher = Cipher.getInstance(AES_ENCRYPTION_SCHEME);
			cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(IV));
			
			String data = null;
			for (int i = 0; i<input.length; i++){
				data = input[i];
				if (data == null || data.isEmpty()){
					encryptedInput[i] = data;
				}
				else{
					byte[] plainText = data.getBytes(UNICODE_FORMAT);
					byte[] cipherBytes = cipher.doFinal(plainText);
					encryptedInput[i] = Base64.encodeBase64String(cipherBytes);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return encryptedInput;
	}
	
	@Override
	public Map<String, String> encrypt(SecretKey key, Map<String, String> dataMap){
		if (key == null || dataMap == null){
			return null;
		}
		
		Map<String, String> encData = null;
		if (dataMap.size() > 0){
			try {
				Cipher cipher = Cipher.getInstance(AES_ENCRYPTION_SCHEME);
				cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(IV));
				
				Iterator<String> iterator = dataMap.keySet().iterator();
				encData = new HashMap<>();
				String mapKey = null;
				String data = null;
				while(iterator.hasNext()){
					mapKey = iterator.next();
					data = dataMap.get(mapKey);
					if (data != null && !data.isEmpty()){
						byte[] plainText = data.getBytes(UNICODE_FORMAT);
						byte[] cipherBytes = cipher.doFinal(plainText);
						encData.put(mapKey, Base64.encodeBase64String(cipherBytes));
					}
					else{
						encData.put(mapKey, data);
					}
				}
			} catch (InvalidKeyException | InvalidAlgorithmParameterException | NoSuchAlgorithmException | NoSuchPaddingException | UnsupportedEncodingException | IllegalBlockSizeException | BadPaddingException e) {
				e.printStackTrace();
				return null;
			}
		}
		
		return encData;
	}

	/**
	 * Method To Decrypt An Ecrypted String
	 */
	@Override
	public String[] decrypt(SecretKey key, String...input) {
		if (input == null || key == null){
			return null;
		}
		if (input.length == 0){
			return null;
		}
		
		String[] decryptedInput = new String[input.length];
		try {
			Cipher cipher = Cipher.getInstance(AES_ENCRYPTION_SCHEME);
			cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(IV));
			
			String data = null;
			byte[] encryptedText = null;
			byte[] plainText = null;
			for (int i = 0; i<input.length; i++){
				data = input[i];
				if (data != null && !data.isEmpty()){
					encryptedText = Base64.decodeBase64(data);
					plainText = cipher.doFinal(encryptedText);
					decryptedInput[i] = new String(plainText);
				}
				else{
					decryptedInput[i] = data;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return decryptedInput;
	}
	
	@Override
	public Map<String, String> decrypt(SecretKey key, Map<String, String> encData){
		
		if (key == null || encData == null){
			return null;
		}
		
		if (encData.size() > 0){
			Map<String, String> decData = null;
			try {
				Cipher cipher = Cipher.getInstance(AES_ENCRYPTION_SCHEME);
				cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(IV));

				Iterator<String> iterator = encData.keySet().iterator();
				String mapKey = null;
				String value = null;
				byte[] encryptedText = null;
				byte[] plainText = null;
				decData = new HashMap<>();
				while(iterator.hasNext()){
					mapKey = iterator.next();
					value = encData.get(mapKey);
					if (value != null && !value.isEmpty()){
						encryptedText = Base64.decodeBase64(value);
						plainText = cipher.doFinal(encryptedText);
						decData.put(mapKey, new String(plainText));
					}
					else{
						decData.put(mapKey, value);
					}
				}
			} catch (NoSuchAlgorithmException | NoSuchPaddingException | 
					InvalidKeyException | InvalidAlgorithmParameterException | 
					IllegalBlockSizeException | BadPaddingException e) {
				e.printStackTrace();
			}
			return decData;
		}
		return null;
	}
	
	@Override
	public boolean validateDecryptedAttributes(String... attributes) {
		if (attributes == null){
			return false;
		}
		
		for (String attribute : attributes){
			if (attribute == null || attribute.isEmpty()){
				return false;
			}
		}
		
		return true;
	}
}
