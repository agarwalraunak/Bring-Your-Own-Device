/**
 * 
 */
package com.kerberos.exceptions;

import javax.ws.rs.core.Response;

/**
 * @author raunak
 *
 */
public class UnauthorizedServiceTicketRequestException extends RestException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4189182339526355983L;
	private final static String message = "Unauthorized Service Ticket Request";
	private final static int statusCode = Response.Status.UNAUTHORIZED.getStatusCode();
	
	public UnauthorizedServiceTicketRequestException(){
		super(message, statusCode);
	}
	
}
