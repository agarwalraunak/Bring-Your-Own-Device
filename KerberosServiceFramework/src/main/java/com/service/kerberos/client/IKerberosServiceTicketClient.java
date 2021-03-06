package com.service.kerberos.client;

import java.io.IOException;

import com.service.exception.RestClientException;
import com.service.model.kerberos.KerberosSession;
import com.service.model.kerberos.ServiceTicket;

/**
 * This interface allows to get the <code>ServiceTicket</code> from the Kerberos Server
 * for the service to be invoked by the Application
 * 
 * @author raunak
 *
 */
public interface IKerberosServiceTicketClient {

	/**
	 * This method retrieves the <code>ServiceTicket</code> from the Kerberos Server.
	 * It requires <code>IKerberosAuthenticationClient kerberosAuthentication()</code> method to be
	 * called before
	 * @param <code>String</code> serviceName
	 * @param <code>KerberosSession</code> kerberosSession
	 * @return
	 * <code>ServiceTicket</code>
	 * In case there are some errors encountered while retrieving information
	 * @throws RestClientException
	 * If the status of the response is not <strong>200</strong>. The server side error message and error 
	 * response code can be accessed using <code>getMessage</code> and <code>getErrorCode</code> methods respectively
	 */
	ServiceTicket requestServiceTicketFromKerberos(String serviceName,
			KerberosSession kerberosSession) throws IOException,
			RestClientException;

}
