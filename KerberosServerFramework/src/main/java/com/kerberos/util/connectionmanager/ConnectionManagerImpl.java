/**
 * 
 */
package com.kerberos.util.connectionmanager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.kerberos.exceptions.ErrorResponse;
import com.kerberos.exceptions.RestClientException;

/**
 * @author raunak
 *
 */
@Component
public class ConnectionManagerImpl implements IConnectionManager {
	
	private final String CONTENT_TYPE = "application/json";
	public enum RequestMethod{
		POST_REQUEST_METHOD("POST"), GET_REQUEST_METHOD("GET");
		
		private String value;
		
		private RequestMethod(String value){
			this.value = value;
		}
		
		public String toString(){
			return value;
		}
	}
	
	@Override
	public HttpURLConnection createConnection(String urlString, RequestMethod requestMethod)
			throws IOException {
		URL url = new URL(urlString);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setRequestMethod(requestMethod.toString());
		conn.setRequestProperty("Content-Type", CONTENT_TYPE);
		return conn;
	}
	
	@Override
	public HttpURLConnection writeDataToConnection(HttpURLConnection conn, String... input) throws IOException {
		if (conn == null || input == null){
			return null;
		}
		
		OutputStream os = conn.getOutputStream();
		for (String s : input)
			os.write(s.getBytes());
		os.flush();
		os.close();
		return conn;
	}

	@Override
	public <T> T generateRequest(String url, RequestMethod requestMethod, Class<T> representation, String... input) throws IOException, RestClientException{
		
		
		if (url == null || url.isEmpty() || requestMethod == null || representation == null){
			return null;
		}
		
		HttpURLConnection conn = createConnection(url, requestMethod);
		
		if (input != null && input.length > 0)
			conn = writeDataToConnection(conn, input);
		
		T response = fillGsonWithResponse(conn, representation);
		
		return response;
	}
	
	@Override
	public String generateJSONStringForObject(Object object) {
		
		if (object == null){
			return null;
		}
		return new Gson().toJson(object);
	}

	public <T> T fillGsonWithResponse(HttpURLConnection conn, Class<T> representation) throws IOException, RestClientException{
		
		if (conn == null || representation == null){
			return null;
		}
		
		Gson gson = new Gson();
		//If the Response is not OK
		//Reading the Error from connection and throwing RestClientException
		if (conn.getResponseCode() != 200){
			
			BufferedReader breader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
			String error = breader.readLine();
			StringBuilder errorBuilder = new StringBuilder();
			while(error != null){
				errorBuilder.append(error);
				error = breader.readLine();
			}
			breader.close();
			error = errorBuilder.toString();
			ErrorResponse errorResponse = gson.fromJson(error, ErrorResponse.class);
			throw new RestClientException(errorResponse);
		}

		InputStream is = conn.getInputStream();
		InputStreamReader isreader = new InputStreamReader(is);
		T response = gson.fromJson(isreader, representation);
		is.close();
		return response;
	}
	
}
