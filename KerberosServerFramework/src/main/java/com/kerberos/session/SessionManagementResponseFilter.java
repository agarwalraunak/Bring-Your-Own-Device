package com.kerberos.session;

import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kerberos.keyserver.model.app.Session;
import com.kerberos.keyserver.rest.representation.ServiceAccessResponse;
import com.kerberos.keyserver.rest.representation.ServiceLoginResponse;
import com.kerberos.session.SessionManagementAPIImpl.RequestParam;
import com.kerberos.util.dateutil.IDateUtil;
import com.kerberos.util.encryption.IEncryptionUtil;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseFilter;

/**
 * @author raunak
 *
 */

@Component
public class SessionManagementResponseFilter implements ContainerResponseFilter {
	
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
			
			Session session = (Session)httpRequest.getAttribute(RequestParam.SESSION.getValue());
			String requestAuthenticatorStr = (String)httpRequest.getAttribute(RequestParam.REQUEST_AUTHENTICATOR.getValue());
			
			Date requestAuthenticator = iDateUtil.generateDateFromString(requestAuthenticatorStr);
			Date responseAuthenticator = iDateUtil.createResponseAuthenticator(requestAuthenticator);
			String responseAuthenticatorStr = iDateUtil.generateStringFromDate(responseAuthenticator);
			
			SecretKey appSessionKey = iEncryptionUtil.generateSecretKey(session.getSessionID());

			String encResponseAuthenticator = iEncryptionUtil.encrypt(appSessionKey, responseAuthenticatorStr)[0];
			Map<String, String> encData = iEncryptionUtil.encrypt(appSessionKey, accessResponse.getEncResponseData());
			
			response.setEntity(new ServiceAccessResponse(encData, encResponseAuthenticator));
		}	
		return response;
	}

}
