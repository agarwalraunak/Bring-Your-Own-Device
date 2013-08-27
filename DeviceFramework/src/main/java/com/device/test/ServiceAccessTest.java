/**
 * 
 */
package com.device.test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.device.exception.RestClientException;
import com.device.model.KerberosSession;
import com.device.model.ServiceSession;
import com.device.model.ServiceTicket;
import com.device.model.SessionManager;
import com.device.service.client.IAccessServiceClient;
import com.device.service.client.IServiceTicketAuthenticationClient;

/**
 * @author raunak
 *
 */
@Controller
public class ServiceAccessTest {
	
	private @Autowired SessionManager manager;
	private @Autowired IServiceTicketAuthenticationClient iServiceTicketAuthenticationClient;
	private @Autowired IAccessServiceClient iAccessServiceClient;
	
	@RequestMapping("/serviceAccessTest")
	public @ResponseBody String test(){
		
		String serviceName = "ServiceSecurity";
		String username = "Sam.Bolt@gmail.com";
		String serviceTicketAuthenticationURL = "http://localhost:8080/service/orange/authenticate/serviceTicket";
		String accessServiceURL = "http://localhost:8080/service/orange/test123/restservice";
		
		KerberosSession kerberosSession = manager.getKerberosSession();
		ServiceTicket ticket = kerberosSession.getTgt().findServiceTicketByServiceName(serviceName);
		
		ServiceSession serviceSession = null;
		try {
			 serviceSession = iServiceTicketAuthenticationClient.serviceUserAuthentication(serviceTicketAuthenticationURL, ticket, username);
			 
			 Map<String, String> requestData = new HashMap<>();
			 requestData.put("Test Request Data Key", "Test Request Data Value");
			 Map<String, String> responseData = iAccessServiceClient.accessService(accessServiceURL, serviceSession, requestData);
			 
			 Iterator<String> iterator = responseData.keySet().iterator();
			 String key = null;
			 while(iterator.hasNext()){
				 key = iterator.next();
				 System.out.println("Service Access Response Data KEY: "+key +" :: Service Access Response Data VALUE: "+responseData.get(key));
			 }
			 
			 ServiceSession serviceAppSession = iServiceTicketAuthenticationClient.serviceUserAuthentication(serviceTicketAuthenticationURL, ticket, null);
			 
			 Map<String, String> appRequestData = new HashMap<>();
			 appRequestData.put("Test App Request Data Key", "Test App Request Data Value");
			 Map<String, String> appResponseData = iAccessServiceClient.accessService(accessServiceURL, serviceAppSession, requestData);
			 
			 Iterator<String> iteratorApp = appResponseData.keySet().iterator();
			 String appKey = null;
			 while(iteratorApp.hasNext()){
				 appKey = iteratorApp.next();
				 System.out.println("Service Access Response Data KEY: "+appKey +" :: Service Access Response Data VALUE: "+appResponseData.get(appKey));
			 }
		} catch (IOException e) {
			e.printStackTrace();
		} catch (RestClientException e) {
			e.printStackTrace();
			return "Status Code: "+e.getErrorCode() + ":: Message:"+e.getMessage();
		}

		
		return "SERVICE_SESSION: "+serviceSession.getSessionID();
	}

}
