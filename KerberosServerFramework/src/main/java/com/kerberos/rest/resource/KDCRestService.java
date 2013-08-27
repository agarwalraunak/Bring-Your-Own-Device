/**
 * 
 */
package com.kerberos.rest.resource;

import java.util.Date;

import javax.crypto.SecretKey;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kerberos.configuration.KerberosConfigurationManager;
import com.kerberos.db.service.ITGTService;
import com.kerberos.exceptions.InternalSystemException;
import com.kerberos.exceptions.InvalidAppLoginNameException;
import com.kerberos.exceptions.UnregisteredAppException;
import com.kerberos.rest.api.IKDCAPI;
import com.kerberos.rest.representation.AuthenticationRequest;
import com.kerberos.rest.representation.AuthenticationResponse;
import com.kerberos.util.ActiveDirectory.IActiveDirectory;
import com.kerberos.util.ActiveDirectory.ActiveDirectoryImpl.SecretKeyType;
import com.kerberos.util.dateutil.IDateUtil;
import com.kerberos.util.hashing.IHashUtil;
import com.kerberos.util.keyserver.KeyServerUtil;

/**
 * This web service performs the role of the KDC in the Kerberos protocol. It is responsible to authenticate the device
 * 
 * @author raunak
 *
 */
@Component
@Path("/KDC")
public class KDCRestService {
	
	private static Logger log = Logger.getLogger(KDCRestService.class);
	
	private @Autowired IActiveDirectory iActiveDirectory;
	private @Autowired KerberosConfigurationManager configurationManager;
	
	private @Autowired IKDCAPI iKDCAPI;
	private @Autowired ITGTService iTGTService;
	private @Autowired IHashUtil iHashUtil;
	private @Autowired IDateUtil iDateUtil;
	private @Autowired KeyServerUtil keyServerUtil;
	
	@Path("/authenticate")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public AuthenticationResponse authenticateDevice(AuthenticationRequest request) throws InvalidAppLoginNameException, UnregisteredAppException, InternalSystemException{
		
		log.info("Entering the KDCRestService.authenticateDevice");
		
		String appLoginName = request.getAppLoginName();
		String username = request.getUsername();
		
		//Validate the received appLoginName
		log.debug("Validate the received AppLoginName");
		if (appLoginName == null || appLoginName.isEmpty()){
			log.error("Invalid App Login Name received");
			throw new InvalidAppLoginNameException();
		}
		
		//Retrieve the Password for App from active directory
		log.debug("Retrieving Password App from active directory");
		String appPassword = iActiveDirectory.findPasswordForApp(appLoginName);
		if (appPassword == null || appPassword.isEmpty()){
			throw new UnregisteredAppException();
		}
		
		//Retrieve the Password for User from active directory
		log.debug("Retrieving Password User from active directory");
		String userPassword = null;
		if (username != null && !username.isEmpty())
			userPassword = iActiveDirectory.findPasswordForUser(username);
		
		//Generate the Password Key
		log.debug("Generating Password Key");
		SecretKey passwordKey = iKDCAPI.generatePasswordSymmetricKey(appLoginName, appPassword, username, userPassword);
		if (passwordKey == null){
			throw new InternalSystemException();
		}
		
		//Creating the TGT
		log.debug("Create the TGT");
		String sessionID = iHashUtil.getSessionKey();
		Date tgtExpiryTime = iDateUtil.generateDateWithDelay(configurationManager.getTGT_TIME_OUT());
		iTGTService.saveTGT(appLoginName, username, sessionID, tgtExpiryTime);
		
		//Get the Kerberos Master Key
		log.debug("Fetching the Kerberos Master Key from Key Server");
		SecretKey kerberosMasterKey = keyServerUtil.getKeyFromKeyStore(null, SecretKeyType.KDC_MASTER_KEY);
		if (kerberosMasterKey == null){
			log.error("Error fetching Kerberos Master Key");
			throw new InternalSystemException();
		}
		
		//Creating the Authentication Response
		log.debug("Creating the Authentication Response");
		String tgtExpiryTimeStr = iDateUtil.generateStringFromDate(tgtExpiryTime);
		AuthenticationResponse response = iKDCAPI.createAuthenticationResponse(appLoginName, username, tgtExpiryTimeStr, sessionID, 
																			   passwordKey, kerberosMasterKey);
		
		log.info("Returning from authenticateDevice method");
		
		return response;
	}
	
}
