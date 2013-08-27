package com.device.service.client;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.device.configuration.KerberosURLConfig;
import com.device.exception.RestClientException;
import com.device.model.ServiceSession;
import com.device.service.api.IAccessServiceAPI;
import com.device.service.representation.ServiceAccessRequest;
import com.device.service.representation.ServiceAccessResponse;
import com.device.util.connectionmanager.ConnectionManagerImpl.RequestMethod;
import com.device.util.connectionmanager.IConnectionManager;
import com.device.util.dateutil.IDateUtil;
import com.device.util.encryption.IEncryptionUtil;
import com.device.util.hashing.IHashUtil;

/**
 * @author raunak
 *
 */
@Component
public class AccessServiceClientImpl implements IAccessServiceClient {
	
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
	
	private static Logger log = Logger.getLogger(AccessServiceClientImpl.class);
	
	private @Autowired IEncryptionUtil iEncryptionUtil;
	private @Autowired IDateUtil iDateUtil;
	private @Autowired IConnectionManager iConnectionManager;
	private @Autowired IHashUtil iHashUtil;
	
	private @Autowired IAccessServiceAPI iAccessServiceAPI;
	
	private @Autowired KerberosURLConfig kerberosURLConfig;
	
	@Override
	public Map<String, String> accessService(String accessServiceURL, ServiceSession serviceSession, Map<String, String> requestData)
			throws IOException, RestClientException{
		
		log.info("Entering getKeyFromKeyServer method");
		
		//Retrieve the Session ID and Service Ticket Packet AppLoginName from ServiceSession
		log.debug("Retrieving the Session ID and Service Ticket Packet AppLoginName from ServiceSession");
		String sessionID = serviceSession.getSessionID();
		String serviceTicketPacketAppLoginName = serviceSession.getServiceTicket().getServiceTicketPacketAppLoginName();
		
		//Creating SessionKey from the Session ID
		log.debug("Creating SessionKey from the Session ID");
		SecretKey sessionKey = iEncryptionUtil.generateSecretKey(sessionID);
		
		//Creating Service Access Request Authenticator
		log.debug("Creating Service Access Request attributes");
		String requestAuthenticatorStr = iDateUtil.createAuthenticator();
		Date requestAuthenticator = iDateUtil.generateDateFromString(requestAuthenticatorStr);
		
		//Creating Service Access Request
		log.debug("Creating Service Access Request");
		ServiceAccessRequest request = iAccessServiceAPI.createServiceAccessRequest(requestAuthenticatorStr, requestData, serviceTicketPacketAppLoginName, sessionKey);

		//Requesting Key Server for Service Key
		log.debug("Requesting Key Server for Service Key");
		ServiceAccessResponse response = iConnectionManager.generateRequest(accessServiceURL, RequestMethod.POST_REQUEST_METHOD, ServiceAccessResponse.class, 
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
		
		return responseData;
	}
}