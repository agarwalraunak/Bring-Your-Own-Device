
/**
 * 
 */
package com.service.model.kerberos;

import java.util.Date;

/**
 * @author raunak
 *
 */
public class ServiceSession{
	
	private ServiceTicket serviceTicket;
	private String username;
	private Date latestAuthenticator;
	private String sessionID;
	private Date created;
	
	public ServiceSession(String sessionID, ServiceTicket serviceTicket, String username,
			Date latestAuthenticator) {
		this.sessionID = sessionID;
		this.serviceTicket = serviceTicket;
		this.username = username;
		this.latestAuthenticator = latestAuthenticator;
		created = new Date();
	}
	
	public ServiceSession() {
		
	}
	/**
	 * @return <code>String</code> the sessionID
	 */
	public String getSessionID() {
		return sessionID;
	}
	/**
	 * @param <code>String</code> sessionID the sessionID to set
	 */
	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}
	/**
	 * @return <code>Date</code> the created
	 */
	public Date getCreated() {
		return created;
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
