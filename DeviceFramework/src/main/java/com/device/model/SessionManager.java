package com.device.model;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

/**
 * @author raunak
 *
 */
@Component
public class SessionManager {

	private KerberosSession kerberosSession;
	/**
	 * Key is the Service Name
	 */
	private Map<String, ServiceSession> serviceSessionMap;
	
	public SessionManager() {
		serviceSessionMap = new HashMap<>();
	}
	
	/**
	 * @return the kerberosSession
	 */
	public KerberosSession getKerberosSession() {
		return kerberosSession;
	}
	/**
	 * @param kerberosSession the kerberosSession to set
	 */
	public void setKerberosSession(KerberosSession kerberosSession) {
		this.kerberosSession = kerberosSession;
	}
	/**
	 * Returns the <code>ServiceSession</code> related to the given <strong>serviceName</strong>
	 * @param <code>String</code> serviceName
	 * @return <code>ServiceSession</code>
	 */
	public ServiceSession findSessionByServiceName(String serviceName){
		return serviceSessionMap.get(serviceName);
	}
	
	public KerberosSession createKerberosSession(String sessionID, String tgtPacketAppLoginName, String tgtPacketExpiryTime,
			String tgtPacketSessionID, String tgtPacketUsername){
		
		TGT tgt = new TGT();
		
		tgt.setTgtPacketAppLoginName(tgtPacketAppLoginName);
		tgt.setTgtPacketExpiryTime(tgtPacketExpiryTime);
		tgt.setTgtPacketSessionID(tgtPacketSessionID);
		tgt.setTgtPacketUsername(tgtPacketUsername);
		
		kerberosSession = new KerberosSession(sessionID, tgt);
		
		return kerberosSession;
	}
}
