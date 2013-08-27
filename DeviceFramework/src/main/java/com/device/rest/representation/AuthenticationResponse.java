/**
 * 
 */
package com.device.rest.representation;

/**
 * Represents the RESTFull response structure required for App and User Authentication
 * 
 * @author raunak
 *
 */
public class AuthenticationResponse {
	
	/**
	 * Encrypted with Kerberos Master Key
	 * then with Password
	 */
	private String tgtPacketAppLoginName;
	/**
	 * Encrypted with Kerberos Master Key
	 * then with Password
	 */
	private String tgtPacketUsername;
	/**
	 * Encrypted with Kerberos Master Key
	 * then with Password
	 */
	private String tgtPacketExpiryTime;
	/**
	 * Encrypted with Kerberos Master Key
	 * then with Password
	 */
	private String tgtPacketSessionID;
	/**
	 * Kerberos Generated Session ID
	 * encrypted with Password
	 */
	private String encSessionID;
	
	/**
	 * @return <code>String</code> the encSessionID
	 */
	public String getEncSessionID() {
		return encSessionID;
	}
	/**
	 * @param <code>String</code> encSessionID the encSessionID to set
	 */
	public void setEncSessionID(String encSessionID) {
		this.encSessionID = encSessionID;
	}
	/**
	 * @return <code>String</code> the tgtPacketAppLoginName
	 */
	public String getTgtPacketAppLoginName() {
		return tgtPacketAppLoginName;
	}
	/**
	 * @param <code>String</code> tgtPacketAppLoginName the tgtPacketAppLoginName to set
	 */
	public void setTgtPacketAppLoginName(String tgtPacketAppLoginName) {
		this.tgtPacketAppLoginName = tgtPacketAppLoginName;
	}
	/**
	 * @return <code>String</code> the tgtPacketUsername
	 */
	public String getTgtPacketUsername() {
		return tgtPacketUsername;
	}
	/**
	 * @param <code>String</code> tgtPacketUsername the tgtPacketUsername to set
	 */
	public void setTgtPacketUsername(String tgtPacketUsername) {
		this.tgtPacketUsername = tgtPacketUsername;
	}
	/**
	 * @return <code>String</code> the tgtPacketExpiryTime
	 */
	public String getTgtPacketExpiryTime() {
		return tgtPacketExpiryTime;
	}
	/**
	 * @param <code>String</code> tgtPacketExpiryTime the tgtPacketExpiryTime to set
	 */
	public void setTgtPacketExpiryTime(String tgtPacketExpiryTime) {
		this.tgtPacketExpiryTime = tgtPacketExpiryTime;
	}
	/**
	 * @return <code>String</code> the tgtPacketSessionID
	 */
	public String getTgtPacketSessionID() {
		return tgtPacketSessionID;
	}
	/**
	 * @param <code>String</code> tgtPacketSessionID the tgtPacketSessionID to set
	 */
	public void setTgtPacketSessionID(String tgtPacketSessionID) {
		this.tgtPacketSessionID = tgtPacketSessionID;
	}
	
	
}
