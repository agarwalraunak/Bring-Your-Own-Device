package com.device.service.representation;

import java.util.Map;

public class ServiceAccessResponse {

	/**
	 * Encrypted with the Session ID
	 */
	private String encResponseAuthenticator;
	/**
	 * Encrypted with the Session ID
	 */
	private Map<String, String> encResponseData;
	/**
	 * @return the encResponseAuthenticator
	 */
	public String getEncResponseAuthenticator() {
		return encResponseAuthenticator;
	}
	/**
	 * @param encResponseAuthenticator the encResponseAuthenticator to set
	 */
	public void setEncResponseAuthenticator(String encResponseAuthenticator) {
		this.encResponseAuthenticator = encResponseAuthenticator;
	}
	/**
	 * @return the encResponseData
	 */
	public Map<String, String> getEncResponseData() {
		return encResponseData;
	}
	/**
	 * @param encResponseData the encResponseData to set
	 */
	public void setEncResponseData(Map<String, String> encResponseData) {
		this.encResponseData = encResponseData;
	}
}