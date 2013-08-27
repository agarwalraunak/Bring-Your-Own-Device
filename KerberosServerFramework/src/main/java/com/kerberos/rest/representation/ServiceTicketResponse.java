/**
 * 
 */
package com.kerberos.rest.representation;

/**
 * Represents the RESTFull response structure, used to get the <code>ServiceTicket</code> from the <strong>Ticket Granting Server</code>
 * 
 * @author raunak
 *
 */
public class ServiceTicketResponse {

	/**
	 * Encrypted with Service Key then with Kerberos Session ID
	 */
	private String serviceTicketPacketAppLoginName;
	/**
	 * Encrypted with Service Key then with Kerberos Session ID
	 */
	private String serviceTicketPacketUsername;
	/**
	 * Encrypted with Service Key then with Kerberos Session ID
	 */
	private String serviceTicketPacketServiceSessionID;
	/**
	 * Encrypted with Service Key then with Kerberos Session ID 
	 */
	private String serviceTicketPacketExpiryTime;
	/**
	 * Encrypted with Kerberos Session ID
	 */
	private String encServiceSessionID;
	/**
	 * Encrypted with Kerberos Session ID
	 */
	private String encResponseAuthenticator;
	/**
	 * @return <code>String</code> the encServiceSessionID
	 */
	public String getEncServiceSessionID() {
		return encServiceSessionID;
	}
	/**
	 * @param <code>String</code> encServiceSessionID the encServiceSessionID to set
	 */
	public void setEncServiceSessionID(String encServiceSessionID) {
		this.encServiceSessionID = encServiceSessionID;
	}
	/**
	 * @return <code>String</code> the serviceTicketPacketAppLoginName
	 */
	public String getServiceTicketPacketAppLoginName() {
		return serviceTicketPacketAppLoginName;
	}
	/**
	 * @param <code>String</code> serviceTicketPacketAppLoginName the serviceTicketPacketAppLoginName to set
	 */
	public void setServiceTicketPacketAppLoginName(
			String serviceTicketPacketAppLoginName) {
		this.serviceTicketPacketAppLoginName = serviceTicketPacketAppLoginName;
	}
	/**
	 * @return <code>String</code> the serviceTicketPacketUsername
	 */
	public String getServiceTicketPacketUsername() {
		return serviceTicketPacketUsername;
	}
	/**
	 * @param <code>String</code> serviceTicketPacketUsername the serviceTicketPacketUsername to set
	 */
	public void setServiceTicketPacketUsername(String serviceTicketPacketUsername) {
		this.serviceTicketPacketUsername = serviceTicketPacketUsername;
	}
	/**
	 * @return <code>String</code> the serviceTicketPacketServiceSessionID
	 */
	public String getServiceTicketPacketServiceSessionID() {
		return serviceTicketPacketServiceSessionID;
	}
	/**
	 * @param <code>String</code> serviceTicketPacketServiceSessionID the serviceTicketPacketServiceSessionID to set
	 */
	public void setServiceTicketPacketServiceSessionID(
			String serviceTicketPacketServiceSessionID) {
		this.serviceTicketPacketServiceSessionID = serviceTicketPacketServiceSessionID;
	}
	/**
	 * @return <code>String</code> the serviceTicketPacketExpiryTime
	 */
	public String getServiceTicketPacketExpiryTime() {
		return serviceTicketPacketExpiryTime;
	}
	/**
	 * @param <code>String</code> serviceTicketPacketExpiryTime the serviceTicketPacketExpiryTime to set
	 */
	public void setServiceTicketPacketExpiryTime(
			String serviceTicketPacketExpiryTime) {
		this.serviceTicketPacketExpiryTime = serviceTicketPacketExpiryTime;
	}
	/**
	 * @return <code>String</code> the encResponseAuthenticator
	 */
	public String getEncResponseAuthenticator() {
		return encResponseAuthenticator;
	}
	/**
	 * @param <code>String</code> encResponseAuthenticator the encResponseAuthenticator to set
	 */
	public void setEncResponseAuthenticator(String encResponseAuthenticator) {
		this.encResponseAuthenticator = encResponseAuthenticator;
	}
	
}
