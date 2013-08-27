/**
 * 
 */
package com.device.service.client;

import java.io.IOException;
import java.util.Map;

import com.device.exception.RestClientException;
import com.device.model.ServiceSession;

/**
 * @author raunak
 *
 */
public interface IAccessServiceClient {

	/**
	 * @param accessServiceURL
	 * @param serviceSession
	 * @param requestData
	 * @return
	 * @throws IOException
	 * @throws RestClientException
	 */
	Map<String, String> accessService(String accessServiceURL,
			ServiceSession serviceSession, Map<String, String> requestData)
			throws IOException, RestClientException;


}
