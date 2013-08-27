/**
 * 
 */
package com.kerberos.exceptions;

import javax.ws.rs.core.Response;

/**
 * @author raunak
 *
 */
public class UnregisteredAppException extends RestException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4289955655419902534L;

	private static final String message = "No registered app found";
	private static final int statusCode = Response.Status.BAD_REQUEST.getStatusCode();
	
	public UnregisteredAppException() {
		super(message, statusCode);
	}
}
