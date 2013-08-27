/**
 * 
 */
package com.kerberos.exceptions;

import javax.ws.rs.core.Response;

/**
 * @author raunak
 *
 */
public class InvalidAppLoginNameException extends RestException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4189182339526355983L;
	private final static String message = "Invalid App Login Found. Can not be empty";
	private final static int statusCode = Response.Status.BAD_REQUEST.getStatusCode();
	
	public InvalidAppLoginNameException(){
		super(message, statusCode);
	}
	
}
