package com.service.app.rest.representation;

import java.util.Map;

public class ServiceAccessRequest {

	private Map<String, String> encData; //Encrypted using Session ID
	private String encRequestAuthenticator; 	//Encrypted using Session ID
	private String encAppLoginName;
	
	public Map<String, String> getEncData() {
		return encData;
	}
	
	public String getEncRequestAuthenticator() {
		return encRequestAuthenticator;
	}
	
	public String getEncAppLoginName() {
		return encAppLoginName;
	}
	
	public void setEncData(Map<String, String> data) {
		this.encData = data;
	}
	
	public void setEncRequestAuthenticator(String authenticator) {
		this.encRequestAuthenticator = authenticator;
	}
	
	public void setEncAppLoginName(String appLoginName) {
		this.encAppLoginName = appLoginName;
	}
	
	
}