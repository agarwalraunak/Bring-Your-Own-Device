/**
 * 
 */
package com.service.app.rest.resource;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Component;

import com.service.app.rest.representation.ServiceAccessRequest;
import com.service.app.rest.representation.ServiceAccessResponse;
import com.service.exception.ApplicationDetailServiceInitializationException;
import com.service.exception.ResponseDecryptionException;
import com.service.exception.RestClientException;
import com.service.exception.common.AuthenticatorValidationException;
import com.service.exception.common.InternalSystemException;
import com.service.exception.common.UnauthenticatedException;
import com.service.model.Session;
import com.service.session.SessionManagementAPIImpl.RequestParam;

/**
 * @author raunak
 *
 */

@Component
@Path("/test123/")
public class TestRestService {

	
	@Path("/restservice/")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ServiceAccessResponse test(ServiceAccessRequest request, @Context HttpServletRequest httpRequest) throws 
			UnauthenticatedException, AuthenticatorValidationException, IOException, 
			RestClientException, ResponseDecryptionException, ApplicationDetailServiceInitializationException, InternalSystemException{
		
		Map<String, String> decData = request.getEncData();
		
		Session session = (Session)httpRequest.getAttribute(RequestParam.SESSION.getValue());
		System.out.println("Service Side Session " + session.getSessionID());
		
		Iterator<String> iterator = decData.keySet().iterator();
		String key = null;
		while(iterator.hasNext()){
			key = iterator.next();
			System.out.println("Key: " + key + " :: Value: " + decData.get(key));
		}
		
		Map<String, String> responseData = new HashMap<String, String>();
		responseData.put("raunak", "agarwal");
		
		ServiceAccessResponse response = new ServiceAccessResponse(responseData);
		return response;
	}	
	
}
