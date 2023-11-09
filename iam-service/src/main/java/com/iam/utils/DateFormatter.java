package com.iam.utils;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateFormatter {

	public static String getFormattedDate() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	    String formattedDate = LocalDateTime.now().format(formatter);
	    return formattedDate;
	}
	
}