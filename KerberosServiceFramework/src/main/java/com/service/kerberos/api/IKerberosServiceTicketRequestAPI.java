/**
 * 
 */
package com.service.kerberos.api;

import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

import com.service.kerberos.api.KerberosServiceTicketRequestAPIImpl.ServiceTicketResponseAttributes;
import com.service.kerberos.representation.ServiceTicketRequest;
import com.service.kerberos.representation.ServiceTicketResponse;
import com.service.model.kerberos.TGT;

/**
 * This interface is used by <code>IKerberosRequestServiceTicketClient</code> to get the 
 * <code>ServiceTicket</code> from the <strong>Kerberos Server</strong> 
 * 
 * @author raunak
 *
 */
public interface IKerberosServiceTicketRequestAPI {


	/**
	 * Creates <code>ServiceTicketRequest</code>
	 * @param <code>String</code> serviceName
	 * @param <code>TGT</code> tgt
	 * @param <code>String</code> requestAuthenticator
	 * @param <code>Secretkey</code> sessionKey
	 * @return
	 * <code>ServiceTicketRequest</code>
	 */
	ServiceTicketRequest createServiceTicketRequest(String serviceName,
			TGT tgt, String requestAuthenticator, SecretKey sessionKey);

	/**
	 * Decrypts the <code>ServiceTicketResponse</code> Attributes using the Session Key
	 * @param <code>ServiceTicketResponse</code> response
	 * @param <code>Date</code> requestAuthenticator
	 * @param <code>SecretKey</code> sessionKey
	 * @return
	 * <code>Map<ServiceTicketResponseAttributes, String></code>
	 */
	Map<ServiceTicketResponseAttributes, String> decryptAndValidateServiceTicketResponseAttributes(
			ServiceTicketResponse response, Date requestAuthenticator,
			SecretKey sessionKey);

}
