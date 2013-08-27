/**
 * 
 */
package com.kerberos.keyserver.rest.resource;

import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kerberos.exceptions.InvalidServiceAccessRequestException;
import com.kerberos.keyserver.model.app.Session;
import com.kerberos.keyserver.rest.representation.ServiceAccessRequest;
import com.kerberos.keyserver.rest.representation.ServiceAccessResponse;
import com.kerberos.util.ActiveDirectory.ActiveDirectoryImpl.SecretKeyType;
import com.kerberos.util.encryption.IEncryptionUtil;
import com.kerberos.util.hashing.IHashUtil;
import com.kerberos.util.keyserver.KeyServerUtil;

/**
 * @author raunak
 *
 */
@Component
@Path("/keyserver/")
public class KeyServerRestService {
	
	private static Logger log = Logger.getLogger(KeyServerRestService.class);
	
	private @Autowired KeyServerUtil keyServerUtil;
	private @Autowired IEncryptionUtil iEncryptionUtil;
	private @Autowired IHashUtil iHashUtil;
	
	@Path("/key")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ServiceAccessResponse retrieveKeyFromKeyServer(ServiceAccessRequest request, @Context HttpServletRequest httpRequest) throws InvalidServiceAccessRequestException{
		
		log.info("Entering retrieveKeyFromKeyServer method");
		
		Session session = (Session)httpRequest.getAttribute("SESSION");
		String serviceName = session.getAppLoginName();
		
		Map<String, String> data = request.getEncData();
		String keyTypeStr = data.get("KEY_TYPE");
		SecretKeyType keyType = SecretKeyType.valueOf(keyTypeStr);
		
		//Validating the Service Access Request attributes
		log.debug("Validating the Service Access Request Attributes");
		if (keyType == null && serviceName == null || serviceName.isEmpty()){
			log.error("Invalid Service Access Request");
			throw new InvalidServiceAccessRequestException();
		}
		
		//Retrieve the key from Key Server
		log.debug("Retrieving the key from Key Server");
		SecretKey key = keyServerUtil.getKeyFromKeyStore(serviceName, keyType);
		if (key == null){
			log.error("Invalid Service Access Request");
			throw new InvalidServiceAccessRequestException();
		}
		String requestedKeyString = iHashUtil.bytetoBase64String(key.getEncoded());

		Map<String, String> encResponseData = new HashMap<>();
		encResponseData.put("Key", requestedKeyString);
		
		ServiceAccessResponse response = new ServiceAccessResponse();
		response.setEncResponseData(encResponseData);
		
		log.info("Returning from retrieveKeyFromKeyServer");
		
		return response;
	}

}
