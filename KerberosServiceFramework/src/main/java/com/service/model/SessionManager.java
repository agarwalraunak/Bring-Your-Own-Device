/**
 * 
 */
package com.service.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.service.config.SessionConfig;
import com.service.model.kerberos.KerberosSession;
import com.service.model.kerberos.TGT;
import com.service.util.dateutil.IDateUtil;
import com.service.util.hashing.IHashUtil;

/**
 * @author raunak
 *
 */
@Component
@Scope(value=ConfigurableBeanFactory.SCOPE_SINGLETON)
public class SessionManager {
	
	private KerberosSession kerberosSession;
	private Map<String, Session> appSessionDirectory;		//Key is the appLoginName
	private @Autowired IHashUtil iHashUtil;
	private @Autowired IDateUtil iDateUtil;
	
	public SessionManager() {
		appSessionDirectory = new HashMap<>();
	}
	
	/**
	 * @return the appSession
	 */
	public KerberosSession getKerberosAppSession() {
		return kerberosSession;
	}

	
	/**
	 * @param kerberosAppSession the kerberosAppSession to set
	 */
	public void setKerberosAppSession(KerberosSession kerberosAppSession) {
		this.kerberosSession = kerberosAppSession;
	}
	
	

	/**
	 * @return the appSessionDirectory
	 */
	public Map<String, Session> getAppSessionDirectory() {
		return appSessionDirectory;
	}

	/**
	 * @param sessionID
	 * @param tgtPacketAppLoginName
	 * @param tgtPacketExpiryTime
	 * @param tgtPacketSessionID
	 * @param tgtPacketUsername
	 * @return
	 */
	public KerberosSession createKerberosSession(String sessionID, String tgtPacketAppLoginName, String tgtPacketExpiryTime,
			String tgtPacketSessionID, String tgtPacketUsername){
		
		TGT tgt = new TGT();
		
		tgt.setTgtPacketAppLoginName(tgtPacketAppLoginName);
		tgt.setTgtPacketExpiryTime(tgtPacketExpiryTime);
		tgt.setTgtPacketSessionID(tgtPacketSessionID);
		tgt.setTgtPacketUsername(tgtPacketUsername);
		
		KerberosSession kerberosSession = new KerberosSession(sessionID, tgt);
		
		return kerberosSession;
	}

	/**
	 * @param serviceSessionID
	 * @param username
	 * @return AppSession or null if the AppSession does not exist or input parameters are invalid
	 */
	public Session createSession(String appLoginName, String username, String serviceSessionID, String clientIP, Date latestAuthenticator) {
		
		if (serviceSessionID == null || serviceSessionID.isEmpty() || clientIP == null || clientIP.isEmpty()){
			return null;
		}
		
		Session session = new Session(iHashUtil.getSessionKey(), appLoginName, username, serviceSessionID, clientIP, latestAuthenticator, SessionConfig.getAppSessionExpiryTime());

		appSessionDirectory.put(appLoginName, session);
		
		return session;
	}
	
	/**
	 * @param appID
	 * @return
	 */
	public Session findActiveAppSessionByAppID(String appID){
		
		if(appID == null){
			return null;
		}
		
		Session appSession = appSessionDirectory.get(appID);
		
		if (appSession != null){
			//Check if the appSession active flag is true and if it has expired set Active flag to false
			if (appSession.isActive() && appSession.getExpiryTime().before(new Date())){
				appSession.setActive(false);
			}
			if (appSession.isActive())
				return appSession;
		}
			
		return null;
	}

}
