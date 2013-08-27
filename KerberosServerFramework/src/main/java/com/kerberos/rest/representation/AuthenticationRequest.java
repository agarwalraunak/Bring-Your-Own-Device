/**
 * 
 */
package com.kerberos.rest.representation;

/**
 * Represents the RESTFull request structure required for App and User Authentication 
 * 
 * @author raunak
 *
 */
public class AuthenticationRequest {

	private String appLoginName;
	private String username;
	/**
	 * @return <code>String</code> the appLoginName
	 */
	public String getAppLoginName() {
		return appLoginName;
	}
	/**
	 * @param <code>String</code> appLoginName the appLoginName to set
	 */
	public void setAppLoginName(String appLoginName) {
		this.appLoginName = appLoginName;
	}
	/**
	 * @return <code>String</code> the username
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @param <code>String</code> username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	
	
}
