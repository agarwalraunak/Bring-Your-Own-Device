package com.service.kerberos.client;

import java.io.IOException;

import com.service.exception.RestClientException;
import com.service.model.kerberos.ServiceSession;
import com.service.model.kerberos.ServiceTicket;

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
