/**
 * 
 */
package com.kerberos.exceptions;

import javax.ws.rs.core.Response;

/**
 * @author raunak
 *
 */
public class UnauthenticatedException extends RestException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8158808868106184237L;
	private final static String message = "Can't find related session. Unauthorized request";
	private final static int errorID = Response.Status.UNAUTHORIZED.getStatusCode();
	
	public UnauthenticatedException() {
		super(message, errorID);
	}
	

}
