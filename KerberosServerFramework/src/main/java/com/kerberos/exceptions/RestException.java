/**
 * 
 */
package com.kerberos.exceptions;


/**
 * @author raunak
 *
 */
public class RestException extends Exception{


	/**
	 * 
	 */
	private static final long serialVersionUID = -8856441808643847831L;
	private String message;
	private int statusCode;
	
	public RestException(String message, int statusCode) {
		super(message);
		this.message = message;
		this.statusCode = statusCode;
	}

	/**
	 * @return <code>String</code> the message
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @return <code>String</code> the statusCode
	 */
	public int getStatusCode() {
		return statusCode;
	}
}