
/**
 * 
 */
package com.device.model;

import java.util.Date;

/**
 * @author raunak
 *
 */
public class ServiceSession extends Session{
	
	private ServiceTicket serviceTicket;
	private String username;
	private Date latestAuthenticator;

	
	public ServiceSession(String sessionID, ServiceTicket serviceTicket, String username,
			Date latestAuthenticator) {
		super(sessionID);
		this.serviceTicket = serviceTicket;
		this.username = username;
		this.latestAuthenticator = latestAuthenticator;
	}

	/**
	 * @return <code>ServiceTicket</code> the serviceTicket
	 */
	public ServiceTicket getServiceTicket() {
		return serviceTicket;
	}

	/**
	 * @param <code>ServiceTicket</code> serviceTicket the serviceTicket to set
	 */
	public void setServiceTicket(ServiceTicket serviceTicket) {
		this.serviceTicket = serviceTicket;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the latestAuthenticator
	 */
	public Date getLatestAuthenticator() {
		return latestAuthenticator;
	}

	/**
	 * @param latestAuthenticator the latestAuthenticator to set
	 */
	public void setLatestAuthenticator(Date latestAuthenticator) {
		this.latestAuthenticator = latestAuthenticator;
	}
}
