/**
 * 
 */
package com.kerberos.exceptions;

import javax.ws.rs.core.Response;

/**
 * @author raunak
 *
 */
public class AuthenticatorValidationException extends RestException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8936457594774008513L;
	private final static String message = "Authenticator failed to validate. Unauthorized request";
	private final static int errorID = Response.Status.UNAUTHORIZED.getStatusCode();
	
	public AuthenticatorValidationException() {
		super(message, errorID);
	}
	

}
