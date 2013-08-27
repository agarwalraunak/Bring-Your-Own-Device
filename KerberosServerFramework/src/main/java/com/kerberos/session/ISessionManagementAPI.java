/**
 * 
 */
package com.kerberos.session;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;

import com.kerberos.exceptions.AuthenticatorValidationException;
import com.kerberos.exceptions.RestException;
import com.kerberos.exceptions.SessionExpiredException;
import com.kerberos.exceptions.UnauthenticatedException;
import com.kerberos.keyserver.model.app.Session;
import com.kerberos.keyserver.rest.representation.ServiceAccessRequest;

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
	 * Validates the <code>UserServiceAuthenticationRequest</code> to check if the App Session ID and request authenticator are valid 
	 * @param request
	 * <code>UserServiceAuthenticationRequest</code>
	 * @return
	 * <code>UserServiceAuthenticationRequest</code> with the decrypted information or null if the request is not valid
	 * @throws AuthenticatorValidationException 
	 * @throws UnauthenticatedAppException 
	 */
//	UserServiceAuthenticationRequest validateUserServiceAuthenticationRequest(
//			UserServiceAuthenticationRequest request) throws UnauthenticatedAppException, AuthenticatorValidationException;


	/**
	 * @param request
	 * @return
	 * @throws AuthenticatorValidationException 
	 * @throws UnauthenticatedUserException 
	 * @throws UnauthenticatedException 
	 */
	ServiceAccessRequest validateUserAccessServiceRequest(
			ServiceAccessRequest request) throws UnauthenticatedException, AuthenticatorValidationException;


	/**
	 * @param httpRequest
	 * @param entity
	 * @return
	 */
	HttpServletRequest addAttributesToRequest(HttpServletRequest httpRequest,
			Object entity);


	/**
	 * @param session
	 * @param path
	 * @param requestAuthenticator
	 * @param clientIP
	 * @return
	 * @throws UserSessionExpiredException 
	 * @throws SessionExpiredException 
	 */
	boolean manageSession(Session session, String path,
			Date authenticator, String clientIP) throws SessionExpiredException;
//
//
//	/**
//	 * @param session
//	 * @param path
//	 * @param requestAuthenticator
//	 * @param clientIP
//	 * @return
//	 * @throws UserSessionExpiredException 
//	 * @throws AppSessionExpiredException 
//	 */
//	boolean manageUserSession(UserSession session, String path,
//			String requestAuthenticator, String clientIP) throws AppSessionExpiredException, UserSessionExpiredException;

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







}
