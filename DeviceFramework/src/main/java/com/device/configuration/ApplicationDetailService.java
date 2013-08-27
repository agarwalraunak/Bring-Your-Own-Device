/**
 * 
 */
package com.device.configuration;

/**
 * @author raunak
 *
 */
public class ApplicationDetailService {
	
	private String appLoginName;
	private String appPassword;
	
	/**
	 * @param <code>String</code> appLoginName
	 * @param <code>String</code> appPassword
	 */
	public ApplicationDetailService(String appLoginName, String appPassword) {
		super();
		this.appLoginName = appLoginName;
		this.appPassword = appPassword;
	}
	
	/**
	 * @return <code>String</code> Configured App Login Name
	 */
	public String getAppLoginName() {
		return appLoginName;
	}


	/**
	 * @return <code>String</code> Configured App Password
	 */
	public String getAppPassword() {
		return appPassword;
	}
	
}
