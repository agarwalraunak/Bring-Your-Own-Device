/**
 * 
 */
package com.service.model.kerberos;

import java.util.HashMap;
import java.util.Map;

/**
 * @author raunak
 *
 */
public class TGT {
	
	private String tgtPacketAppLoginName;
	private String tgtPacketExpiryTime;
	private String tgtPacketSessionID;
	private String tgtPacketUsername;
	/**
	 * Key is the Service Name
	 */
	private Map<String, ServiceTicket> serviceTicketMap;
	
	public TGT() {
		serviceTicketMap = new HashMap<>();
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
	 * Creates and returns <code>ServiceTicket</code>
	 * @param <code>String</code> serviceName
	 * @param <code>String</code> serviceSessionID
	 * @param <code>String</code> serviceTicketPacketAppLoginName
	 * @param <code>String</code> serviceTicketPacketExpiryTime
	 * @param <code>String</code> serviceTicketPacketSessionID
	 * @param <code>String</code> serviceTicketPacketUsername
	 * @return <code>ServiceTicket</code>
	 */
	public ServiceTicket createServiceTicket(String serviceName, String serviceSessionID, 
			String serviceTicketPacketAppLoginName, String serviceTicketPacketExpiryTime,
			String serviceTicketPacketSessionID, String serviceTicketPacketUsername){
		
		ServiceTicket ticket = new ServiceTicket();
		ticket.setServiceName(serviceName);
		ticket.setServiceSessionID(serviceSessionID);
		ticket.setServiceTicketPacketAppLoginName(serviceTicketPacketAppLoginName);
		ticket.setServiceTicketPacketExpiryTime(serviceTicketPacketExpiryTime);
		ticket.setServiceTicketPacketSessionID(serviceTicketPacketSessionID);
		ticket.setServiceTicketPacketUsername(serviceTicketPacketUsername);
		
		serviceTicketMap.put(serviceName, ticket);
		
		return ticket;
	}
}
