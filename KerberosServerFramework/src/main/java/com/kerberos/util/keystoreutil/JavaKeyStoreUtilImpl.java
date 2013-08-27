/**
 * 
 */
package com.kerberos.util.keystoreutil;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStore.PasswordProtection;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.util.Enumeration;

import javax.crypto.SecretKey;

/**
 * @author HIE Prototype Dev Team
 * 
 */
public class JavaKeyStoreUtilImpl implements JavaKeyStoreUtil {
	
	private String KEY_STORE_PASSWORD;
	private String KEY_STORE_FILE;
	
	/**
	 * @param KEY_STORE_PASSWORD
	 * @param KEY_STORE_FILE
	 */
	public JavaKeyStoreUtilImpl(String KEY_STORE_PASSWORD, String KEY_STORE_FILE) {
		super();
		this.KEY_STORE_PASSWORD = KEY_STORE_PASSWORD;
		this.KEY_STORE_FILE = KEY_STORE_FILE;
	}

	public KeyStore loadKeyStore() {
		
		KeyStore keyStore;
		try {
			keyStore = KeyStore.getInstance("JCEKS");
		} catch (KeyStoreException e) {
			e.printStackTrace();
			return null;
		}

		//get user password and file input stream
		char[] password = KEY_STORE_PASSWORD.toCharArray();
		FileInputStream fileInputStream;
		try {
			fileInputStream = new FileInputStream(KEY_STORE_FILE);
			keyStore.load(fileInputStream, password);
			fileInputStream.close();
			return keyStore;
		} catch (NoSuchAlgorithmException | CertificateException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}

	@Override
	public void listAllKeys(KeyStore keyStore) {
		try {
			Enumeration<String> aliases = keyStore.aliases();

			while (aliases.hasMoreElements()) {
				String alias = (String) aliases.nextElement();
				System.out.println("Alias Name:" + alias);
			}
		} catch (KeyStoreException kse) {
			kse.printStackTrace();
		}
	}

	@Override
	public void storeKey(SecretKey mySecretKey, String Alias, char[] password) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
		
		KeyStore keyStore = loadKeyStore();
		PasswordProtection pp = new PasswordProtection(password);
		KeyStore.SecretKeyEntry skEntry = new KeyStore.SecretKeyEntry(mySecretKey);
		keyStore.setEntry(Alias, skEntry, pp);
		// store away the keystore
		FileOutputStream fos = new FileOutputStream(KEY_STORE_FILE);
		keyStore.store(fos, KEY_STORE_PASSWORD.toCharArray());
		fos.close();
		System.out.print("Key inserted successfully");
	}

	@Override
	public SecretKey getKey(String Alias, char[] password){
		
		KeyStore keyStore = loadKeyStore();
		
		KeyStore.SecretKeyEntry skEntry = null;
		try {
			skEntry = (KeyStore.SecretKeyEntry) keyStore.getEntry(Alias, new PasswordProtection(password));
		} catch (NoSuchAlgorithmException | UnrecoverableEntryException
				| KeyStoreException e) {
			e.printStackTrace();
			return null;
		}
		SecretKey mySecretKey = null;
		if (skEntry != null)
			mySecretKey = skEntry.getSecretKey();
		return mySecretKey;
	}
}