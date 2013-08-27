/**
 * 
 */
package com.service.util.dateutil;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author raunak
 *
 */
@Component
@Scope(value=ConfigurableBeanFactory.SCOPE_SINGLETON)
public class DateUtilImpl implements IDateUtil {
	
	private final String DATE_FORMAT = "dd-MMM-yy HH:mm:ss";
	private final int AUTHENTICATORS_MINUTE_DIFFERENCE = 1;
	
	@Override
	public Date generateDateWithDelay(int delayInHour){
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.HOUR_OF_DAY, delayInHour);
		Date date = calendar.getTime();
		return date;
	}
	
	@Override
	public String generateStringFromDate(Date date){
		DateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
		return formatter.format(date);
	}
	
	@Override
	public Date generateDateFromString(String dateString){
		
		if (dateString != null && dateString.isEmpty()){
			return null;
		}
		
		DateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
		try {
			return formatter.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public boolean validateAuthenticator(Date authenticator, Date requestAuthenticator){
		if (authenticator.getTime() - requestAuthenticator.getTime() != AUTHENTICATORS_MINUTE_DIFFERENCE*60*1000) {
			return false;
		}
		return true;
	}

	@Override
	public String createAuthenticator(){

		Date date = new Date();
		DateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
		String authenticator = formatter.format(date);
		
		return authenticator;
	}
	
	@Override
	public Date createResponseAuthenticator(Date requestAuthenticator){
		
		Calendar c = Calendar.getInstance();
		c.setTime(requestAuthenticator);
		c.add(Calendar.MINUTE, AUTHENTICATORS_MINUTE_DIFFERENCE);
		Date responseAuthenticator = c.getTime();
		
		return responseAuthenticator;
	}
}
