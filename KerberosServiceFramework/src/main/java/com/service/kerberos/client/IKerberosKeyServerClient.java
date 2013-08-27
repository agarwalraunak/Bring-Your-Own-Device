/**
 * 
 */
package com.service.kerberos.client;

import java.io.IOException;

import javax.crypto.SecretKey;

import com.service.exception.RestClientException;
import com.service.exception.common.InternalSystemException;
import com.service.model.kerberos.ServiceSession;

/**
 * @author raunak
 *
 */
public interface IKerberosKeyServerClient {

	/**
	 * @param serviceSession
	 * @return
	 * @throws IOException
	 * @throws RestClientException
	 * @throws InternalSystemException
	 */
	SecretKey getKeyFromKeyServer(ServiceSession serviceSession)
			throws IOException, RestClientException, InternalSystemException;

}
