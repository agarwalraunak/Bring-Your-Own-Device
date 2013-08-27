package com.kerberos.session;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kerberos.exceptions.AuthenticatorValidationException;
import com.kerberos.exceptions.ErrorResponse;
import com.kerberos.exceptions.RestException;
import com.kerberos.exceptions.SessionExpiredException;
import com.kerberos.exceptions.UnauthenticatedException;
import com.kerberos.keyserver.model.SessionManager;
import com.kerberos.keyserver.model.app.Request;
import com.kerberos.keyserver.model.app.Session;
import com.kerberos.keyserver.rest.representation.ServiceAccessRequest;
import com.kerberos.util.ActiveDirectory.ActiveDirectoryImpl.SecretKeyType;
import com.kerberos.util.dateutil.IDateUtil;
import com.kerberos.util.encryption.IEncryptionUtil;
import com.kerberos.util.keyserver.KeyServerUtil;

@Component
public class SessionManagementAPIImpl implements ISessionManagementAPI {
	
	private @Autowired SessionManager sessionDirectory;
	private @Autowired IEncryptionUtil iEncryptionUtil;
	private @Autowired IDateUtil iDateUtil;
	private @Autowired KeyServerUtil keyServerUtil;
	
	public enum RequestParam{
		REQUEST_AUTHENTICATOR("REQUEST_AUTHENTICATOR"), SESSION("SESSION");
		private String value;
		
		RequestParam(String value){
			this.value = value;
		}
		
		public String getValue(){
			return value;
		}
	}
	
	@Override
	public String getRequestEntityString(InputStream inputStream) throws IOException {
		
		if (inputStream == null){
			return null;
		}
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		String entity = reader.readLine();
		StringBuilder builder = new StringBuilder();
		while(entity!=null){
			builder.append(entity);
			entity = reader.readLine();
		}

		return builder.toString();
	}

	@Override
	public <T> T identifyRequest(String restServiceRequest, Class<T> clazz) {
		
		if (restServiceRequest == null || restServiceRequest.isEmpty() || clazz == null){
			return null;
		}
		
		ObjectMapper obj = new ObjectMapper();
		T request = null;
		try {
			request = obj.convertValue(obj.readValue(new StringReader(restServiceRequest), HashMap.class), clazz);
		} catch (IllegalArgumentException | IOException e) {}
		return request;
	}
	
	@Override
	public <T> T identifyResponse(String restServiceResponse, Class<T> clazz) {
		
		if (restServiceResponse == null || restServiceResponse.isEmpty() || clazz == null){
			return null;
		}
		
		ObjectMapper obj = new ObjectMapper();
		T request = null;
		try {
			request = obj.convertValue(obj.readValue(new StringReader(restServiceResponse), HashMap.class), clazz);
		} catch (IllegalArgumentException | IOException e) {}
		return request;
	}
	
	@Override
	public ServiceAccessRequest validateUserAccessServiceRequest(ServiceAccessRequest request) throws UnauthenticatedException, AuthenticatorValidationException{
		String encAppLoginName = request.getEncAppLoginName();		
		//Decrypting the AppSessionID
		SecretKey serviceSecretKey = keyServerUtil.getKeyFromKeyStore(null, SecretKeyType.KEY_SERVER);
		String decAppLoginName = iEncryptionUtil.decrypt(serviceSecretKey, encAppLoginName)[0];		

		if (!iEncryptionUtil.validateDecryptedAttributes(decAppLoginName)){
			throw new UnauthenticatedException();
		}

		Session session = sessionDirectory.findActiveSessionByAppID(decAppLoginName);
		String sessionID = session.getSessionID();
		SecretKey sessionKey = iEncryptionUtil.generateSecretKey(sessionID);
		
		String encRequestAuthenticator = request.getEncRequestAuthenticator();		
		String decRequestAuthenticator = iEncryptionUtil.decrypt(sessionKey, encRequestAuthenticator)[0];
		
		//Validate the decrypted attributes
		if (!iEncryptionUtil.validateDecryptedAttributes(decRequestAuthenticator)){
			throw new UnauthenticatedException();
		}

		Date requestAuthenticator = iDateUtil.generateDateFromString(decRequestAuthenticator);
		if (!session.validateAuthenticator(requestAuthenticator)){
			throw new AuthenticatorValidationException();
		}

		Map<String, String> decData = iEncryptionUtil.decrypt(sessionKey, request.getEncData());
		
		request.setEncData(decData);
		request.setEncAppLoginName(decAppLoginName);
		request.setEncRequestAuthenticator(decRequestAuthenticator);

		return request;
	}
	
	@Override
	public HttpServletRequest addAttributesToRequest(HttpServletRequest httpRequest, Object entity){
		
		if (entity == null || httpRequest == null){
			return null;
		}

		if (entity instanceof ServiceAccessRequest){
			ServiceAccessRequest validatedRequest = (ServiceAccessRequest) entity;
			Session session = sessionDirectory.findActiveSessionByAppID(validatedRequest.getEncAppLoginName());
			httpRequest.setAttribute(RequestParam.REQUEST_AUTHENTICATOR.getValue(), validatedRequest.getEncRequestAuthenticator());
			httpRequest.setAttribute(RequestParam.SESSION.getValue(), session);
		}

		return httpRequest;
	}
	
	public boolean manageSession(Session session, String path, Date authenticator, String clientIP) throws SessionExpiredException{
		session.addRequest(new Request(path, authenticator));
		
		//Check if the Request IP Address is the same for which the session was created
		//Check if the Session has not been expired
		if (!session.getClientIP().equals(clientIP)){
			return false;
		}
		
		if (session.getExpiryTime().before(new Date())){
			session.setActive(false);
		}
		return true;
	}
	
	@Override
	public WebApplicationException createWebApplicationException(RestException exception){
		
		if (exception == null){
			return null;
		}
		ErrorResponse response = new ErrorResponse();
		response.setErrorId(exception.getStatusCode());
		response.setErrorMessage(exception.getMessage());
	
		return new WebApplicationException(Response.status(response.getErrorId()).entity(response).type(MediaType.APPLICATION_JSON).build());
	}
}