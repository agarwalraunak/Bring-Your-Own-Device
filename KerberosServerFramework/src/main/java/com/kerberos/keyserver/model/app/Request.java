package com.kerberos.keyserver.model.app;

import java.util.Date;

public class Request {
	
	private String path;
	private Date created;
	private Response response;
	private Date authenticator;
	
	public Request() {
		created = new Date();
	}
	
	public Request(String path, Date authenticator) {
		this();
		this.path = path;
		this.authenticator = authenticator;
	}

	public String getPath() {
		return path;
	}

	public Date getCreated() {
		return created;
	}

	public Response getResponse() {
		return response;
	}

	public Date getAuthenticator() {
		return authenticator;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public void setResponse(Response response) {
		this.response = response;
	}

	public void setAuthenticator(Date authenticator) {
		this.authenticator = authenticator;
	}

}
