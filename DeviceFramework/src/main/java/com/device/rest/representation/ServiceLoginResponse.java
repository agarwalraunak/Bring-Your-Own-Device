package com.device.rest.representation;

/**
 * @author raunak
 *
 */
public class ServiceLoginResponse {

	/**
	 * Encrypted with Service Session ID 
	 */
	private String encSessionID;
	/**
	 * Encrypted with Service Session ID 
	 */
	private String encResponseAuthenticator;
	/**
	 * @return <code>String</code> the encSessionID
	 */
	public String getEncSessionID() {
		return encSessionID;
	}
	/**
	 * @param <code>String</code> encSessionID the encSessionID to set
	 */
	public void setEncSessionID(String encSessionID) {
		this.encSessionID = encSessionID;
	}
	/**
	 * @return <code>String</code> the encResponseAuthenticator
	 */
	public String getEncResponseAuthenticator() {
		return encResponseAuthenticator;
	}
	/**
	 * @param <code>String</code> encResponseAuthenticator the encResponseAuthenticator to set
	 */
	public void setEncResponseAuthenticator(String encResponseAuthenticator) {
		this.encResponseAuthenticator = encResponseAuthenticator;
	}
}
