package drinkshop.cp102.server.main;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Helper {
	
	public final static String getFmtdDateToStr(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return dateFormat.format(date);
	}
	
	public final static String getFmtDateTimeToStr(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return dateFormat.format(date);
	}
	
	public final static Date getFmtStrToDate(String dateString) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		try {
			date = dateFormat.parse(dateString);
		} catch (ParseException e) {
			System.out.println("getFmtStrToDate fail");
			e.printStackTrace();
		}
		return date;
	}
	
	public final static Date getFmtStrToDateTime(String dateTimeString) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date dateTime = new Date();
		try {
			dateTime = dateFormat.parse(dateTimeString);
		} catch (ParseException e) {
			System.out.println("getFmtStrToDateTime fail");
			e.printStackTrace();
		}
		return dateTime;
	}
	
	public final static String validString(String string) {
		if (string == null) {
			return "";
		}
		return string;
	}
	
	public final static Date validDate(Date date) {
		if (date == null) {
			return new Date();
		}
		return date;
	}
}
