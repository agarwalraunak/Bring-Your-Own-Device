/**
 * 
 */
package com.service.exception.common;

import javax.ws.rs.core.Response;

import com.service.exception.RestException;

/**
 * @author raunak
 *
 */
public class SessionExpiredException extends RestException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2570544664520760756L;
	private final static String message = "Session has Expired";
	private final static int errorID = Response.Status.UNAUTHORIZED.getStatusCode();
	
	public SessionExpiredException() {
		super(message, errorID);
	}

	

}
