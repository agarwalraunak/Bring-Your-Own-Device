package com.device.service.representation;

import java.util.Map;

public class ServiceAccessRequest {

	/**
	 * Encrypted with the Service Key 
	 * of the Service being accessed
	 */
	private String encAppLoginName;
	/**
	 * Encrypted with the Session ID given by the Service
	 * on authentication 
	 */
	private String encRequestAuthenticator;
	/**
	 * Encrypted with the Session ID given by the Service
	 * on authentication 
	 */
	private Map<String, String> encData;
	
	/**
	 * @return the encAppLoginName
	 */
	public String getEncAppLoginName() {
		return encAppLoginName;
	}
	/**
	 * @param encAppLoginName the encAppLoginName to set
	 */
	public void setEncAppLoginName(String encAppLoginName) {
		this.encAppLoginName = encAppLoginName;
	}
	/**
	 * @return the encRequestAuthenticator
	 */
	public String getEncRequestAuthenticator() {
		return encRequestAuthenticator;
	}
	/**
	 * @param encRequestAuthenticator the encRequestAuthenticator to set
	 */
	public void setEncRequestAuthenticator(String encRequestAuthenticator) {
		this.encRequestAuthenticator = encRequestAuthenticator;
	}
	/**
	 * @return the encData
	 */
	public Map<String, String> getEncData() {
		return encData;
	}
	/**
	 * @param encData the encData to set
	 */
	public void setEncData(Map<String, String> encData) {
		this.encData = encData;
	}
	
	
}
