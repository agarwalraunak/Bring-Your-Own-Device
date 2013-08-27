/**
 * 
 */
package com.kerberos.exceptions;

import javax.ws.rs.core.Response;

/**
 * @author raunak
 *
 */
public class InvalidRequestException extends RestException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7011689555885078841L;
	private final static String message = "Invalid Request Parameters found. Bad Request Found";
	private final static int errorID = Response.Status.BAD_REQUEST.getStatusCode();
	
	public InvalidRequestException() {
		super(message, errorID);
	}
	

}
