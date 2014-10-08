package com.codepath.apps.SimpleTwitterApp.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.format.DateUtils;
import android.util.Log;

public class DateTimeFormatter {
	private static DateTimeFormatter dtf = null;
	private DateTimeFormatter() {
		
	}
	
	public static DateTimeFormatter getInstance() {
		if (dtf == null) {
			//Log.i("debug", "This shud print only once");
			dtf = new DateTimeFormatter();
		}
		return dtf;
	}
	
	public String formatDateTime(String timeStamp) {
		String formattedTimeStamp = null;
		String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
		SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
		sf.setLenient(true);
		try {
			long dateMillis = sf.parse(timeStamp).getTime();
			formattedTimeStamp = DateUtils.getRelativeTimeSpanString(dateMillis,
					System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS).toString();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		//Log.i("debug","Formatted timestamp is "+formattedTimeStamp);
		return formattedTimeStamp;
	}
	
	public String formatDateTimeinDetailView(String timeStamp) {
		String formattedTimeStamp = null;
		String pattern = "^\\w+ (\\w+) (\\d+) (\\d+:\\d+):\\d+ \\+\\d+ \\d\\d(\\d+)";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(timeStamp);
		if (m.find()) {
	         formattedTimeStamp = ""+m.group(3)+" . "+m.group(2)+" "+m.group(1)+" "+m.group(4);
	     } else {
	         System.out.println("NO MATCH");
	     }
		return formattedTimeStamp;
	}
	
	public String getTwitterFormattedDateTime(String timeStamp) {
		String rv;
		String [] words = timeStamp.split(" ");
		if(words.length !=3) {
			return timeStamp;
		}
		if(timeStamp.equalsIgnoreCase("0 minutes ago")) {
			rv = "Just now";
		} else {
			
			if(words[1].equalsIgnoreCase("minutes")) {
				rv = words[0]+"m";
			} else if (words[1].equalsIgnoreCase("hour") || words[1].equalsIgnoreCase("hours")) {
				rv = words[0]+"h";
			}
			else if (words[1].equalsIgnoreCase("day") || words[1].equalsIgnoreCase("days")) {
				rv = words[0]+"d";
			}else if (words[1].equalsIgnoreCase("day") || words[1].equalsIgnoreCase("days")) {
				rv = words[0]+"d";
			} else {
				rv = timeStamp;
			}
		}
		return rv;
	}
	

}
