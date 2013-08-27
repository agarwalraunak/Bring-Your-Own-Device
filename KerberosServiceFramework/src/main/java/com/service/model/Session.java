/**
 * 
 */
package com.service.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.service.model.app.Request;

/**
 * @author raunak
 *
 */
public class Session {
	
	private String sessionID;
	private String appLoginName;
	private String username;
	private String serviceSessionID;
	private String clientIP;
	private Date created;
	private Date latestAuthenticator;
	private List<Request> requests;
	private boolean isActive;
	protected Date expiryTime;
	
	public Session() {
		created = new Date();
		isActive = true;
		requests = new ArrayList<Request>();
	}
	
	public Session(String sessionID, String appLoginName, String username, String serviceSessionID, String clientIP, Date latestAuthenticator,
			Date expiryTime) {
		this();
		this.sessionID = sessionID;
		this.appLoginName = appLoginName;
		this.username = username;
		this.serviceSessionID = serviceSessionID;
		this.clientIP = clientIP;
		this.latestAuthenticator = latestAuthenticator;
		this.expiryTime = expiryTime;
	}

	public String getSessionID() {
		return sessionID;
	}

	public String getAppLoginName() {
		return appLoginName;
	}

	public String getUsername() {
		return username;
	}

	public String getServiceSessionID() {
		return serviceSessionID;
	}

	public String getClientIP() {
		return clientIP;
	}

	public Date getCreated() {
		return created;
	}

	public Date getLatestAuthenticator() {
		return latestAuthenticator;
	}

	public List<Request> getRequests() {
		return requests;
	}

	public boolean isActive() {
		return isActive;
	}

	public Date getExpiryTime() {
		return expiryTime;
	}

	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}

	public void setAppLoginName(String appLoginName) {
		this.appLoginName = appLoginName;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setServiceSessionID(String serviceSessionID) {
		this.serviceSessionID = serviceSessionID;
	}

	public void setClientIP(String clientIP) {
		this.clientIP = clientIP;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public void setLatestAuthenticator(Date latestAuthenticator) {
		this.latestAuthenticator = latestAuthenticator;
	}

	public void setRequests(List<Request> requests) {
		this.requests = requests;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public void setExpiryTime(Date expiryTime) {
		this.expiryTime = expiryTime;
	}
	
	public void addRequest(Request request) {
		requests.add(request);
		this.setLatestAuthenticator(request.getAuthenticator());
	}
	
	public boolean validateAuthenticator(Date requestAuthenticator) {
		return latestAuthenticator.after(requestAuthenticator);
	}
	
}
