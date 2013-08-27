package com.device.model;

import java.util.Date;

/**
 * @author raunak
 *
 */
public abstract class Session {

	private String sessionID;
	private Date created;
	
	public Session(String sessionID) {
		this.sessionID = sessionID;  
		created = new Date();
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
}