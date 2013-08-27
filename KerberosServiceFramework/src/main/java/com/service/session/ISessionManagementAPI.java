/**
 * 
 */
package com.service.session;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;

import com.service.app.rest.representation.ServiceAccessRequest;
import com.service.exception.RestClientException;
import com.service.exception.RestException;
import com.service.exception.common.AuthenticatorValidationException;
import com.service.exception.common.InternalSystemException;
import com.service.exception.common.SessionExpiredException;
import com.service.exception.common.UnauthenticatedException;
import com.service.model.Session;

/**
 * This interface provides the required methods to perform session management
 * 
 * @author raunak
 *
 */
public interface ISessionManagementAPI {
	
	/**
	 * Takes the <code>InputStream</code> of the <strong>Entity</strong> in the <code>ContainerRequest</code> and returns a <code>String</code>
	 * @param inputStream
	 * <code>InputStream</code>
	 * @return
	 * <code>String</code>
	 * @throws IOException
	 * In case there are some errors encountered while retrieving information 
	 */
	public String getRequestEntityString(InputStream inputStream) throws IOException;
	

	/**
	 * This method processes the json <code>String</code> representation of the <strong>Rest Service Request</strong>
	 * and returns the Object 
	 * @param restServiceRequest
	 * <code>String</code> JSON Representation of the Object
	 * @param clazz
	 * <code>Class</code> Class to which the object has to be converted
	 * @return
	 * <code>T</code> or null if the Json String doesn't represent the Class Object passed in
	 */
	<T> T identifyRequest(String restServiceRequest, Class<T> clazz);

	/**
	 * @param request
	 * @return
	 * @throws AuthenticatorValidationException 
	 * @throws UnauthenticatedUserException 
	 * @throws UnauthenticatedException 
	 * @throws  
	 * @throws IOException 
	 * @throws InternalSystemException 
	 */
	ServiceAccessRequest validateUserAccessServiceRequest(
			ServiceAccessRequest request) throws UnauthenticatedException, AuthenticatorValidationException, InternalSystemException, IOException, RestClientException;


	/**
	 * @param httpRequest
	 * @param entity
	 * @return
	 */
	HttpServletRequest addAttributesToRequest(HttpServletRequest httpRequest,
			Object entity);


	/**
	 * @param session
	 * @param clientIP
	 * @return
	 * @throws UserSessionExpiredException 
	 * @throws SessionExpiredException 
	 */
	boolean validateSession(Session session, String clientIP) throws SessionExpiredException;

	/**
	 * @param exception
	 * @return
	 */
	WebApplicationException createWebApplicationException(
			RestException exception);


	/**
	 * @param restServiceResponse
	 * @param clazz
	 * @return
	 */
	<T> T identifyResponse(String restServiceResponse, Class<T> clazz);


	/**
	 * @param session
	 * @param responseCode
	 * @param responseAuthenticator
	 */
	void addResponseToRequest(Session session, int responseCode,
			Date responseAuthenticator);







}
