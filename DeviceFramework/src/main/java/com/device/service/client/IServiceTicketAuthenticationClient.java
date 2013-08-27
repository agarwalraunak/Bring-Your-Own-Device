package com.device.service.client;

import java.io.IOException;

import com.device.exception.RestClientException;
import com.device.model.ServiceSession;
import com.device.model.ServiceTicket;

/**
 * @author raunak
 *
 */
public interface IServiceTicketAuthenticationClient {

	/**
	 * @param authenticationURL
	 * @param ticket
	 * @param username
	 * @return
	 * @throws IOException
	 * @throws RestClientException
	 */
	ServiceSession serviceUserAuthentication(String authenticationURL,
			ServiceTicket ticket, String username) throws IOException,
			RestClientException;

	
	
}
