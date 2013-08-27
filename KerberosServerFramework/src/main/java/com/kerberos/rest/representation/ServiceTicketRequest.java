/**
 * 
 */
package com.kerberos.rest.representation;

/**
 * Represents the RESTFull request structure, used to get the <code>ServiceTicket</code> from the <strong>Ticket Granting Server</code>
 * 
 * @author raunak
 *
 */
public class ServiceTicketRequest {

	/**
	 * encrypted with Kerberos Master Key
	 */
	private String tgtPacketAppLoginName;
	/**
	 * encrypted with Kerberos Master Key
	 */
	private String tgtPacketUsername;
	/**
	 * encrypted with Kerberos Master Key
	 */
	private String tgtPacketExpiryTime;
	/**
	 * encrypted with Kerberos Master Key
	 */
	private String tgtPacketSessionID;
	/**
	 * Name of the Service for which the 
	 * Service Ticket is being requested
	 * Encrypted using Kerberos Session ID
	 */
	private String encServiceName;
	
	/**
	 * Encrypted with Kerberos Session ID
	 */
	private String encRequestAuthenticator;
	/**
	 * @return <code>String</code> the encServiceName
	 */
	public String getEncServiceName() {
		return encServiceName;
	}
	/**
	 * @param <code>String</code> encServiceName the encServiceName to set
	 */
	public void setEncServiceName(String encServiceName) {
		this.encServiceName = encServiceName;
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
	
	/**
	 * @return <code>String</code> the encRequestAuthenticator
	 */
	public String getEncRequestAuthenticator() {
		return encRequestAuthenticator;
	}
	/**
	 * @param <code>String</code> encRequestAuthenticator the encRequestAuthenticator to set
	 */
	public void setEncRequestAuthenticator(String encRequestAuthenticator) {
		this.encRequestAuthenticator = encRequestAuthenticator;
	}
	
	
}
