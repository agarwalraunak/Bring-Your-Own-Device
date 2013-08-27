/**
 * 
 */
package com.device.test;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.device.configuration.ApplicationDetailService;
import com.device.exception.ApplicationDetailServiceInitializationException;
import com.device.exception.RestClientException;
import com.device.model.KerberosSession;
import com.device.model.ServiceTicket;
import com.device.rest.api.IKerberosAuthenticationAPI;
import com.device.rest.client.IKerberosAuthenticationClient;
import com.device.rest.client.IKerberosRequestServiceTicketClient;

/**
 * @author raunak
 *
 */
@Controller
public class KerberosAuthenticationTest {
	
	private @Autowired IKerberosAuthenticationAPI authenticationAPI;
	private @Autowired ApplicationDetailService appDetails;
	private @Autowired IKerberosAuthenticationClient iKerberosAuthenticationClient;
	private @Autowired IKerberosRequestServiceTicketClient iKerberosRequestServiceTicketClient;
	
	
	@RequestMapping("/authenticationAPITest")
	public @ResponseBody String test(){

		String username = "Sam.Bolt@gmail.com";
		String userPassword = "testPassword";
		String serviceName = "ServiceSecurity";
		KerberosSession session  = null;
		ServiceTicket ticket = null;
		try {
			session = iKerberosAuthenticationClient.kerberosAuthentication(username, userPassword);
			System.out.println(session.getSessionID());
			ticket = iKerberosRequestServiceTicketClient.requestServiceTicketFromKerberos(serviceName, session);
			System.out.println(ticket.getServiceSessionID());
		} catch (ApplicationDetailServiceInitializationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RestClientException e) {
			e.printStackTrace();
			return "Status Code: "+e.getErrorCode() + ":: Message:"+e.getMessage();
		}
		
		return "Kerberos Session: "+session.getSessionID() + ":: Service Session ID: "+ticket.getServiceSessionID();
	}

}
