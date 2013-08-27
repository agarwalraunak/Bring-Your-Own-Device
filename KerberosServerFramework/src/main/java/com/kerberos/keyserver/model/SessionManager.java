/**
 * 
 */
package com.kerberos.keyserver.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kerberos.configuration.SessionConfig;
import com.kerberos.keyserver.model.app.Session;
import com.kerberos.util.hashing.IHashUtil;

/**
 * @author raunak
 *
 */
@Component
public class SessionManager {
	
	private Map<String, Session> appSessionDirectory;		//Key is the appLoginName
	private @Autowired IHashUtil iHashUtil;
	
	public SessionManager() {
		appSessionDirectory = new HashMap<>();
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
		
		Session session = new Session(iHashUtil.getSessionKey(), appLoginName, username, serviceSessionID, 
				clientIP, latestAuthenticator, SessionConfig.getSessionExpiryTime());

		appSessionDirectory.put(appLoginName, session);
		
		return session;
	}
	
	/**
	 * @param appID
	 * @return
	 */
	public Session findActiveSessionByAppID(String appID){
		
		if(appID == null){
			return null;
		}
		
		Session session = appSessionDirectory.get(appID);
		
		if (session != null){
			//Check if the appSession active flag is true and if it has expired set Active flag to false
			if (session.isActive() && session.getExpiryTime().before(new Date())){
				session.setActive(false);
			}
			if (session.isActive())
				return session;
		}
			
		return null;
	}

}
