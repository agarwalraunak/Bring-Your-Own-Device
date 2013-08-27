/**
 * 
 */
package com.device.rest.representation;

/**
 * @author raunak
 *
 */
public class ServiceLoginRequest {

	/**
	 * Encrypted with Service Key
	 */
	private String serviceTicketPacketAppLoginName;
	/**
	 * Encrypted with Service Key
	 */
	private String serviceTicketPacketUsername;
	/**
	 * Encrypted with Service Key
	 */
	private String serviceTicketPacketServiceSessionID;
	/**
	 * Encrypted with Service Key
	 */
	private String serviceTicketPacketExpiryTime;
	/**
	 * Encrypted with Service Session Key
	 */
	private String encRequestAuthenticator;
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
