/**
 * 
 */
package com.service.test;

import java.util.Iterator;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.service.model.Session;
import com.service.model.SessionManager;
import com.service.model.app.Request;
import com.service.model.app.Response;
import com.service.util.dateutil.IDateUtil;

@Component
@Path("/sessionManagementTest")
public class SessionManagementTest {
	
	private @Autowired SessionManager manager;
	private @Autowired IDateUtil iDateUtil;
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String test(){

		Map<String, Session> sessions = manager.getAppSessionDirectory();
		
		StringBuffer result = new StringBuffer();
		Iterator<String> iter = sessions.keySet().iterator();
		while (iter.hasNext()) {
			String key = iter.next();
			Session session = sessions.get(key);
			result.append("<b>App ID:</b> " + key + "<br><b>Session ID:</b> " + session.getSessionID() + "<br><b>User Name:</b> " + session.getUsername() + 
					"<br><b>Service Session ID:</b> " + session.getServiceSessionID());
			result.append("<br><b>ClientIP: </b>" + session.getClientIP() + "<br><b>Latest Authenticator:</b> " + 
					session.getLatestAuthenticator() + "<br><b>Created:</b> " + session.getCreated() + "<br>");
			result.append("<h3>Requests and Responses</h3>");
			result.append("<table border=\"1\"><tr><th>Path</th><th>Created</th><th>Authenticator</th><th>Response Code</th><th>Response Authenticator</th></tr>");
			for (Request request : session.getRequests()){
				Response response = request.getResponse();
				result.append("<tr><td>"+request.getPath()+"</td><td>"+request.getCreated()+"</td><td>"+iDateUtil.generateStringFromDate(request.getAuthenticator())+"</td><td>"+response.getResponseCode()
						+"</td><td>"+iDateUtil.generateStringFromDate(response.getResponseAuthenticator())+"</td></tr>");
			}
			result.append("</table>");
		}
		return new String(result);
	}

}
