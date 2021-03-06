/**
 * 
 */
package com.kerberos.keyserver.rest.representation;

import java.util.HashMap;
import java.util.Map;

/**
 * @author raunak
 *
 */
public class ServiceAccessResponse {
	
	private String encResponseAuthenticator;				//Encrypted with User Session ID
	private Map<String, String> encResponseData;			//Encrypted with User Session ID
	
	public ServiceAccessResponse() {	
		encResponseData = new HashMap<String, String>();
	}
	
	public ServiceAccessResponse(Map<String, String> data) {
		this.encResponseData = data;
	}
	
	public ServiceAccessResponse(Map<String, String> data, String authenticator) {
		this.encResponseData = data;
		this.encResponseAuthenticator = authenticator;
	}
	
	public Map<String, String> getEncResponseData() {
		return encResponseData;
	}

	/**
	 * @param data the data to set
	 */
	public void setEncResponseData(Map<String, String> data) {
		this.encResponseData = data;
	}

	public String getEncResponseAuthenticator() {
		return encResponseAuthenticator;
	}

	public void setEncResponseAuthenticator(String authenticator) {
		this.encResponseAuthenticator = authenticator;
	}	
	
}