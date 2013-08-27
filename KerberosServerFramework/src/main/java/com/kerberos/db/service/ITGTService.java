/**
 * 
 */
package com.kerberos.db.service;

import java.util.Date;

import com.kerberos.db.model.TGT;

/**
 * @author raunak
 *
 */
public interface ITGTService {
	
	/**
	 * @param appLoginName
	 * @param username
	 * @param sessionID
	 * @param tgtExpiryDate
	 */
	void saveTGT(String appLoginName, String username, String sessionID,
			Date tgtExpiryDate);
	/**
	 * @param sessionKey
	 * @return
	 */
	TGT findTGTForSessionKey(String sessionKey);
	/**
	 * @param username
	 * @return
	 */
	TGT findActiveTGTForUsername(String username);
	/**
	 * @param tgt
	 */
	void merge(TGT tgt);

	/**
	 * @param tgt
	 */
	void deactiveTGT(TGT tgt);
	
}
