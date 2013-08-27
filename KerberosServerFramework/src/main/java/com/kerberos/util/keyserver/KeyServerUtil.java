/**
 * 
 */
package com.kerberos.util.keyserver;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.crypto.SecretKey;
import javax.management.InvalidAttributeValueException;
import javax.naming.NamingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kerberos.util.ActiveDirectory.ActiveDirectoryImpl.SecretKeyType;
import com.kerberos.util.ActiveDirectory.IActiveDirectory;
import com.kerberos.util.encryption.IEncryptionUtil;
import com.kerberos.util.keystoreutil.JavaKeyStoreUtil;

/**
 * @author raunak
 * 
 */
@Component
public class KeyServerUtil {

	private @Autowired IEncryptionUtil encryptionUtil;
	private @Autowired IActiveDirectory activeDirectory;
	private @Autowired JavaKeyStoreUtil keyStoreUtil;
	
	/**
	 * @param uid
	 * @param keyType
	 */
	public void saveKeyToKeyStore(String uid, SecretKeyType keyType) throws InvalidAttributeValueException{
		/* To store keys in the key store following steps needs to be followed:
		 * 1) Create an entry for the key in Apache DS using the Application UID and SecretKeyType as the Common Name
		 * 2) Retreive the Secret Key password from Apache DS using the Common Name for SecretKey 
		 * 3) Use the retreived password to store the key inside the keystore
		 * 4) Use the Common Name as key alias and to generate the secret key to be stored in the key store
		 */
		if (keyType == null){
			throw new InvalidAttributeValueException(this.getClass().getName()+": Input parameter can not be null in saveKeyToKeyStore");
		}
		else if (keyType.getValue().equalsIgnoreCase(SecretKeyType.KDC_MASTER_KEY.getValue()) || 
				keyType.getValue().equalsIgnoreCase(SecretKeyType.KEY_SERVER.getValue())){
			try {
				activeDirectory.addSecretKey("", keyType);
				String secretKeyPassword = activeDirectory.findSecretKeyPassword(keyType.getValue());
				if (secretKeyPassword != null)
					keyStoreUtil.storeKey(encryptionUtil.generateSecretKey(keyType.getValue()), keyType.getValue(), secretKeyPassword.toCharArray());
				return;
			} catch (IOException | NamingException | KeyStoreException | NoSuchAlgorithmException | CertificateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if (uid == null || uid.isEmpty()){
			throw new InvalidAttributeValueException(this.getClass().getName()+": Input parameter can not be null in saveKeyToKeyStore");
		}
		try {

			activeDirectory.addSecretKey(uid, keyType);
			String keyAlias = activeDirectory.createSecretKeyCommonName(uid, keyType);
			String secretKeyPassword = activeDirectory.findSecretKeyPassword(keyAlias);
			keyStoreUtil.storeKey(encryptionUtil.generateSecretKey(keyAlias), keyAlias, secretKeyPassword.toCharArray());

		} catch (IOException | NamingException | KeyStoreException
				| NoSuchAlgorithmException | CertificateException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param alias
	 * @return
	 */
	public SecretKey getKeyFromKeyStore(String uid, SecretKeyType keyType){
		
		if (keyType == null){
			return null;
		}
		if (keyType.getValue().equals(SecretKeyType.KDC_MASTER_KEY.getValue()) || keyType.getValue().equals(SecretKeyType.KEY_SERVER.getValue())){
			String keyAlias = keyType.getValue();
			String secretKeyPassword = activeDirectory.findSecretKeyPassword(keyAlias);
			SecretKey secretKey = keyStoreUtil.getKey(keyAlias, secretKeyPassword.toCharArray());
			return secretKey;
		}
		
		if (uid == null || uid.isEmpty()){
			return null;
		}
		SecretKey secretKey = null;
		String commonName = activeDirectory.createSecretKeyCommonName(uid, keyType);
		String secretKeyPassword = activeDirectory.findSecretKeyPassword(commonName);
		if (secretKeyPassword != null)
			secretKey = keyStoreUtil.getKey(commonName, secretKeyPassword.toCharArray());
		return secretKey;
	}
}
