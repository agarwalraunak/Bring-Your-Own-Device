/**
 * 
 */
package com.kerberos.session;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kerberos.exceptions.AuthenticatorValidationException;
import com.kerberos.exceptions.InternalSystemException;
import com.kerberos.exceptions.InvalidRequestException;
import com.kerberos.exceptions.SessionExpiredException;
import com.kerberos.exceptions.UnauthenticatedException;
import com.kerberos.keyserver.model.SessionManager;
import com.kerberos.keyserver.model.app.Session;
import com.kerberos.keyserver.rest.representation.ServiceAccessRequest;
import com.kerberos.keyserver.rest.representation.ServiceLoginRequest;
import com.kerberos.rest.representation.AuthenticationRequest;
import com.kerberos.rest.representation.ServiceTicketRequest;
import com.kerberos.util.connectionmanager.IConnectionManager;
import com.kerberos.util.dateutil.IDateUtil;
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
		
		if ((unknownRestRequest = iSessionManagementAPI.identifyRequest(entityString, AuthenticationRequest.class)) != null){
			validatedRequestString = entityString;
		}
		else if ((unknownRestRequest = iSessionManagementAPI.identifyRequest(entityString, ServiceTicketRequest.class)) != null){
			validatedRequestString = entityString;
		}
		else if ((unknownRestRequest = iSessionManagementAPI.identifyRequest(entityString, ServiceLoginRequest.class)) != null){
			validatedRequestString = entityString;
		}
		else if ((unknownRestRequest = iSessionManagementAPI.identifyRequest(entityString, ServiceAccessRequest.class)) != null){
			
			ServiceAccessRequest accessRequest = (ServiceAccessRequest) unknownRestRequest;
			ServiceAccessRequest validatedRequest = null;
			try {
				/**
				 * Decrypt the attributes in ServiceAccessRequest
				 */
				validatedRequest = iSessionManagementAPI.validateUserAccessServiceRequest(accessRequest);
			} 
			catch (UnauthenticatedException | AuthenticatorValidationException e) {
				throw iSessionManagementAPI.createWebApplicationException(e);
			}
			
			Session session = sessionDirectory.findActiveSessionByAppID(validatedRequest.getEncAppLoginName());
			
			try {
				iSessionManagementAPI.manageSession(session, request.getPath(), 
						iDateUtil.generateDateFromString(validatedRequest.getEncRequestAuthenticator()), httpRequest.getRemoteAddr());
			} 
			catch (SessionExpiredException e) {
				throw iSessionManagementAPI.createWebApplicationException(e);
			}

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