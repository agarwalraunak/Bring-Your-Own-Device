package com.service.kerberos.client;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.service.app.rest.representation.ServiceAccessRequest;
import com.service.app.rest.representation.ServiceAccessResponse;
import com.service.config.KerberosURLConfig;
import com.service.exception.RestClientException;
import com.service.exception.common.InternalSystemException;
import com.service.kerberos.api.IAccessServiceAPI;
import com.service.model.kerberos.ServiceSession;
import com.service.util.connectionmanager.ConnectionManagerImpl.RequestMethod;
import com.service.util.connectionmanager.IConnectionManager;
import com.service.util.dateutil.IDateUtil;
import com.service.util.encryption.IEncryptionUtil;
import com.service.util.hashing.IHashUtil;

/**
 * @author raunak
 *
 */
@Component
public class KerberosKeyServerClientImpl implements IKerberosKeyServerClient {
	
	public enum SecretKeyType{
		SERVICE_KEY("SERVICE_KEY");
		
		private String value;
		
		SecretKeyType(String value){
			this.value = value;
		}
		
		public String getValue() {
			return value;
		}
	}
	
	private static Logger log = Logger.getLogger(KerberosKeyServerClientImpl.class);
	
	private @Autowired IEncryptionUtil iEncryptionUtil;
	private @Autowired IDateUtil iDateUtil;
	private @Autowired IConnectionManager iConnectionManager;
	private @Autowired IHashUtil iHashUtil;
	
	private @Autowired IAccessServiceAPI iAccessServiceAPI;
	
	private @Autowired KerberosURLConfig kerberosURLConfig;
	
	@Override
	public SecretKey getKeyFromKeyServer(ServiceSession serviceSession) throws IOException, RestClientException, InternalSystemException{
		
		log.info("Entering getKeyFromKeyServer method");
		
		//Retrieve the Session ID and Service Ticket Packet AppLoginName from ServiceSession
		log.debug("Retrieving the Session ID and Service Ticket Packet AppLoginName from ServiceSession");
		String sessionID = serviceSession.getSessionID();
		String serviceTicketPacketAppLoginName = serviceSession.getServiceTicket().getServiceTicketPacketAppLoginName();
		
		//Creating SessionKey from the Session ID
		log.debug("Creating SessionKey from the Session ID");
		SecretKey sessionKey = iEncryptionUtil.generateSecretKey(sessionID);
		
		//Creating Service Access Request attributes
		log.debug("Creating Service Access Request attributes");
		Map<String, String> requestData = new HashMap<>();
		requestData.put("KEY_TYPE", SecretKeyType.SERVICE_KEY.getValue());
		String requestAuthenticatorStr = iDateUtil.createAuthenticator();
		Date requestAuthenticator = iDateUtil.generateDateFromString(requestAuthenticatorStr);
		
		//Creating Service Access Request
		log.debug("Creating Service Access Request");
		ServiceAccessRequest request = iAccessServiceAPI.createServiceAccessRequest(requestAuthenticatorStr, requestData, serviceTicketPacketAppLoginName, sessionKey);

		//Requesting Key Server for Service Key
		log.debug("Requesting Key Server for Service Key");
		ServiceAccessResponse response = iConnectionManager.generateRequest(kerberosURLConfig.getKERBEROS_KEY_SERVER_KEY_REQUEST_URL(), RequestMethod.POST_REQUEST_METHOD, ServiceAccessResponse.class, 
				iConnectionManager.generateJSONStringForObject(request));
	
		//Decrypt and validate the authenticator
		log.debug("Decrypting and validating the authenticator");
		Date responseAuthenticator = iAccessServiceAPI.decryptAndValidateServiceAccessResponseAuthenticator(response.getEncResponseAuthenticator(), requestAuthenticator, sessionKey);
		
		//Set the Latest Authenticator in to the Service Session
		log.debug("Setting the Latest Authenticator in to the Service Session");
		serviceSession.setLatestAuthenticator(responseAuthenticator);
		
		//Retrieve the Service Key from the Response
		log.debug("Retrive the Service Key from the Response");
		Map<String, String> responseData = iEncryptionUtil.decrypt(sessionKey, response.getEncResponseData());
		String serviceKeyString = responseData.get("Key");
		SecretKey serviceKey = iEncryptionUtil.generateSecretKeyFromBytes(iHashUtil.stringToByte(serviceKeyString));
		
		return serviceKey;
	}
}