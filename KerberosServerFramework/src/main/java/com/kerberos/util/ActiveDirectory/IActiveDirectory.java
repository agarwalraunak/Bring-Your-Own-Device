/**
 * 
 */
package com.kerberos.util.ActiveDirectory;

import java.io.IOException;
import java.util.Map;

import javax.naming.NamingException;

import com.kerberos.util.ActiveDirectory.ActiveDirectoryImpl.SecretKeyType;

/**
 * @author raunak
 *
 */
public interface IActiveDirectory {
	
	/**
	 * @param userId
	 * @return
	 */
	String findPasswordForUser(String userId);

	/**
	 * @param serviceName
	 * @return
	 */
	String findSecretKeyPassword(String serviceName);

	/**
	 * @param bindContext
	 * @throws NamingException
	 * @throws IOException
	 */
	void listAllUsers(String bindContext) throws NamingException, IOException;

	/**
	 * @param details
	 * @return
	 * @throws IOException
	 * @throws NamingException
	 */
	boolean registerUser(EntryDetails details) throws IOException,
			NamingException;

	/**
	 * @param details
	 * @return
	 * @throws IOException
	 * @throws NamingException
	 */
	boolean registerApp(EntryDetails details) throws IOException,
			NamingException;

	/**
	 * @param loginName
	 * @return
	 */
	String findPasswordForApp(String loginName);

	/**
	 * @param uid
	 * @param keyType
	 * @throws IOException
	 * @throws NamingException
	 */
	void addSecretKey(String uid, SecretKeyType keyType) throws IOException,
			NamingException;

	/**
	 * @param appUID
	 * @param keyType
	 * @return
	 */
	String createSecretKeyCommonName(String appUID, SecretKeyType keyType);

	/**
	 * @param uid
	 * @param directoryContext
	 * @param retrieveAttributes
	 * @return
	 * @throws IOException
	 * @throws NamingException
	 */
	Map<String, String> getDataByUID(String uid, String directoryContext,
			String[] retrieveAttributes) throws IOException, NamingException;

	/**
	 * @param userID
	 * @param cn
	 * @param sn
	 * @param appID
	 * @param data
	 * @throws IOException
	 * @throws NamingException
	 */
	void addUserToApplication(String userID, String cn, String sn,
			String appID, Map<String, String> data) throws IOException,
			NamingException;

	String getKEYS_DIRECTORY_CONTEXT();

	String getUSER_DIRECTORY_CONTEXT();

	String getAPPLICATION_DIRECTORY_CONTEXT();

}
