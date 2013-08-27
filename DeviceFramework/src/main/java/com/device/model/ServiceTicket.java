/**
 * 
 */
package com.device.model;

import java.util.Date;

/**
 * @author raunak
 *
 */
public class ServiceTicket {

	private String serviceSessionID;
	private String serviceTicketPacketAppLoginName;
	private String serviceTicketPacketExpiryTime;
	private String serviceTicketPacketUsername;
	private String serviceTicketPacketSessionID;
	private String serviceName;
	private Date created;
	
	/**
	 * @return <code>String</code> the serviceSessionID
	 */
	public String getServiceSessionID() {
		return serviceSessionID;
	}
	/**
	 * @param <code>String</code> serviceSessionID the serviceSessionID to set
	 */
	public void setServiceSessionID(String serviceSessionID) {
		this.serviceSessionID = serviceSessionID;
	}
	/**
	 * @return the serviceTicketPacketAppLoginName
	 */
	public String getServiceTicketPacketAppLoginName() {
		return serviceTicketPacketAppLoginName;
	}
	/**
	 * @param serviceTicketPacketAppLoginName the serviceTicketPacketAppLoginName to set
	 */
	public void setServiceTicketPacketAppLoginName(
			String serviceTicketPacketAppLoginName) {
		this.serviceTicketPacketAppLoginName = serviceTicketPacketAppLoginName;
	}
	/**
	 * @return the serviceTicketPacketExpiryTime
	 */
	public String getServiceTicketPacketExpiryTime() {
		return serviceTicketPacketExpiryTime;
	}
	/**
	 * @param serviceTicketPacketExpiryTime the serviceTicketPacketExpiryTime to set
	 */
	public void setServiceTicketPacketExpiryTime(
			String serviceTicketPacketExpiryTime) {
		this.serviceTicketPacketExpiryTime = serviceTicketPacketExpiryTime;
	}
	/**
	 * @return the serviceTicketPacketUsername
	 */
	public String getServiceTicketPacketUsername() {
		return serviceTicketPacketUsername;
	}
	/**
	 * @param serviceTicketPacketUsername the serviceTicketPacketUsername to set
	 */
	public void setServiceTicketPacketUsername(String serviceTicketPacketUsername) {
		this.serviceTicketPacketUsername = serviceTicketPacketUsername;
	}
	/**
	 * @return the serviceTicketPacketSessionID
	 */
	public String getServiceTicketPacketSessionID() {
		return serviceTicketPacketSessionID;
	}
	/**
	 * @param serviceTicketPacketSessionID the serviceTicketPacketSessionID to set
	 */
	public void setServiceTicketPacketSessionID(String serviceTicketPacketSessionID) {
		this.serviceTicketPacketSessionID = serviceTicketPacketSessionID;
	}
	/**
	 * @return <code>String</code> the serviceName
	 */
	public String getServiceName() {
		return serviceName;
	}
	/**
	 * @param <code>String</code> serviceName the serviceName to set
	 */
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	/**
	 * @return <code>Date</code> the created
	 */
	public Date getCreated() {
		return created;
	}
	/**
	 * @param <code>Date</code> created the created to set
	 */
	public void setCreated(Date created) {
		this.created = created;
	}
	
}
