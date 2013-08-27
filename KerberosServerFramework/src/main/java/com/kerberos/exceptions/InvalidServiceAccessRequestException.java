package com.kerberos.exceptions;

import javax.ws.rs.core.Response;

public class InvalidServiceAccessRequestException extends RestException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1501658422977265000L;
	
	private static final String message = "Invalid Access Request";
	private static final int statusCode = Response.Status.BAD_REQUEST.getStatusCode();
	
	public InvalidServiceAccessRequestException() {
		super(message, statusCode);
	}
	
	

}
