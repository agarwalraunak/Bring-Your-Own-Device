/**
 * 
 */
package com.service.session;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.service.app.rest.representation.ServiceAccessRequest;
import com.service.app.rest.representation.ServiceLoginRequest;
import com.service.exception.RestClientException;
import com.service.exception.common.AuthenticatorValidationException;
import com.service.exception.common.InternalSystemException;
import com.service.exception.common.InvalidRequestException;
import com.service.exception.common.SessionExpiredException;
import com.service.exception.common.UnauthenticatedException;
import com.service.model.Session;
import com.service.model.SessionManager;
import com.service.model.app.Request;
import com.service.util.connectionmanager.IConnectionManager;
import com.service.util.dateutil.IDateUtil;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;

/**
 * @author raunak
 *
 */
@Component
public class SessionManagementRequestFilter implements  ContainerRequestFilter {
	
	private @Autowired ISessionManagementAPI iSessionManagementAPI;
	private @Autowired IConnectionManager iConnectionManager;
	private @Autowired SessionManager sessionDirectory;
	private @Autowired IDateUtil iDateUtil;
	@Context HttpServletRequest httpRequest;
	@Context UriInfo uriInfo;
	
	@Override
	public ContainerRequest filter(ContainerRequest request) {
		if (request.getPath().equalsIgnoreCase("sessionManagementTest")){
			return request;
		}
		
		//Extract the Entity String from the ContainerRequest
		InputStream inputStream = request.getEntityInputStream();
		String entityString = null;
		if (inputStream != null){
			try {
				entityString = iSessionManagementAPI.getRequestEntityString(inputStream);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		String validatedRequestString = null;
		Object unknownRestRequest;
		
		if ((unknownRestRequest = iSessionManagementAPI.identifyRequest(entityString, ServiceLoginRequest.class)) != null){
			validatedRequestString = entityString;
		}
		else if ((unknownRestRequest = iSessionManagementAPI.identifyRequest(entityString, ServiceAccessRequest.class)) != null){
			
			ServiceAccessRequest accessRequest = (ServiceAccessRequest) unknownRestRequest;
			ServiceAccessRequest validatedRequest = null;
			try {
				/**
				 * Decrypt the attributes in ServiceAccessRequest
				 */
				try {
					validatedRequest = iSessionManagementAPI.validateUserAccessServiceRequest(accessRequest);
				} catch (InternalSystemException| IOException| RestClientException e) {
					e.printStackTrace();
					throw iSessionManagementAPI.createWebApplicationException(new InternalSystemException());
				}
			} 
			catch (UnauthenticatedException | AuthenticatorValidationException e) {
				throw iSessionManagementAPI.createWebApplicationException(e);
			}
			
			Session session = sessionDirectory.findActiveAppSessionByAppID(validatedRequest.getEncAppLoginName());
			
			try {
				iSessionManagementAPI.validateSession(session, httpRequest.getRemoteAddr());
			} 
			catch (SessionExpiredException e) {
				throw iSessionManagementAPI.createWebApplicationException(e);
			}
			
			//Add Request to the Session
			session.addRequest(new Request(request.getPath(), iDateUtil.generateDateFromString(validatedRequest.getEncRequestAuthenticator())));

			validatedRequestString = iConnectionManager.generateJSONStringForObject(validatedRequest);
			
			//Set Http Request Attributes
			iSessionManagementAPI.addAttributesToRequest(httpRequest, validatedRequest);
		}
		else{
			iSessionManagementAPI.createWebApplicationException(new InvalidRequestException());
		}
		
		
		//If the Validated Requset String is null or empty throw Exception
		if (validatedRequestString == null || validatedRequestString.isEmpty()){
			throw iSessionManagementAPI.createWebApplicationException(new InternalSystemException());
		}
		
		try {
			request.setEntityInputStream(new ByteArrayInputStream(validatedRequestString.getBytes("UTF-8")));
		} catch (UnsupportedEncodingException e) {
			throw iSessionManagementAPI.createWebApplicationException(new InternalSystemException());
		}
		
		
		return request;
	}
}