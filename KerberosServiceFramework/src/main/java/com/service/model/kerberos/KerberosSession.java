/**
 * 
 */
package com.service.model.kerberos;

import java.util.Date;

/**
 * @author raunak
 *
 */
public class KerberosSession{
	
	private TGT tgt;
	private String sessionID;
	private Date created;
	
	public KerberosSession(String sessionID, TGT tgt) {
		this.sessionID = sessionID;  
		this.tgt = tgt;
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
	/**
	 * @return the tgt
	 */
	public TGT getTgt() {
		return tgt;
	}
	/**
	 * @param tgt the tgt to set
	 */
	public void setTgt(TGT tgt) {
		this.tgt = tgt;
	}
}
