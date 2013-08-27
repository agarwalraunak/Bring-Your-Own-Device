package com.service.session;

import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.service.app.rest.representation.ServiceLoginResponse;
import com.service.app.rest.representation.ServiceAccessResponse;
import com.service.model.Session;
import com.service.session.SessionManagementAPIImpl.RequestParam;
import com.service.util.dateutil.IDateUtil;
import com.service.util.encryption.IEncryptionUtil;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseFilter;

/**
 * @author raunak
 *
 */

@Component
public class SessionManagementResponseFilter implements ContainerResponseFilter {
	
	private @Autowired ISessionManagementAPI iSessionManagementAPI;
	private @Autowired IEncryptionUtil iEncryptionUtil;
	private @Autowired IDateUtil iDateUtil;
	
	@Context HttpServletRequest httpRequest;
	

	@Override
	public ContainerResponse filter(ContainerRequest request, ContainerResponse response) {
		
		Object unknownRestResponse = response.getEntity();
		if (unknownRestResponse instanceof ServiceLoginResponse){
			ServiceLoginResponse appAuthResponse = (ServiceLoginResponse) unknownRestResponse;
			response.setEntity(appAuthResponse);
		}
		else if (unknownRestResponse instanceof ServiceAccessResponse){
		
			ServiceAccessResponse accessResponse = (ServiceAccessResponse) response.getEntity();
			
			//Get the Session and Request Authenticator from the HttpServletRequest
			Session session = (Session)httpRequest.getAttribute(RequestParam.SESSION.getValue());
			String requestAuthenticatorStr = (String)httpRequest.getAttribute(RequestParam.REQUEST_AUTHENTICATOR.getValue());
			
			//Creating the Response Authenticator
			Date requestAuthenticator = iDateUtil.generateDateFromString(requestAuthenticatorStr);
			Date responseAuthenticator = iDateUtil.createResponseAuthenticator(requestAuthenticator);
			String responseAuthenticatorStr = iDateUtil.generateStringFromDate(responseAuthenticator);
			
			//Creating the Session Key
			SecretKey sessionKey = iEncryptionUtil.generateSecretKey(session.getSessionID());

			//Encrypting the Response Attributes
			String encResponseAuthenticator = iEncryptionUtil.encrypt(sessionKey, responseAuthenticatorStr)[0];
			Map<String, String> encData = iEncryptionUtil.encrypt(sessionKey, accessResponse.getEncResponseData());
			
			//Adding Response to the request
			iSessionManagementAPI.addResponseToRequest(session, response.getStatus(), responseAuthenticator);
			
			//Set the latest authenticator to Response Authenticator
			session.setLatestAuthenticator(responseAuthenticator);
			
			response.setEntity(new ServiceAccessResponse(encData, encResponseAuthenticator));
		}	
		return response;
	}

}
