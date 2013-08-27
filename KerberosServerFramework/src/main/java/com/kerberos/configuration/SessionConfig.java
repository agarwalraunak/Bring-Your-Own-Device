/**
 * 
 */
package com.kerberos.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

/**
 * @author raunak
 *
 */
public class SessionConfig {
	
	private static final int SESSION_TIME_OUT;

	static{
		Properties properties = new Properties();
		InputStream inputStream = SessionConfig.class.getClassLoader().getResourceAsStream("SessionConfiguration.properties");
		try {
			properties.load(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Configuration Error: Session Configuration failed!");
		}
		SESSION_TIME_OUT = Integer.parseInt((String)properties.get("SESSION_TIME_OUT"));
	}

	/**
	 * @return the appSessionTimeOut
	 */
	public static int getSessionTimeOut() {
		return SESSION_TIME_OUT;
	}

	public static Date getSessionExpiryTime(){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.MINUTE, SESSION_TIME_OUT);
		return calendar.getTime();
	}
}
