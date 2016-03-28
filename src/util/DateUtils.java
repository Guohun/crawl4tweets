package util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
	public static long DateDiff_in_Minute(String date1, String date2) {  //date 2012-05-09 13:37:49
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
		Date date ; 
		try {
			date = (Date)formatter.parse(date1);
			Calendar calendar1 = Calendar.getInstance();
			calendar1.setTime(date);
			
			date = (Date)formatter.parse(date2);
			Calendar calendar2 = Calendar.getInstance();
			calendar2.setTime(date);

			long milliseconds1 = calendar1.getTimeInMillis();
			long milliseconds2 = calendar2.getTimeInMillis();
			long diff = milliseconds2 - milliseconds1;
			long diffMinutes = diff / (60 * 1000);
			return diffMinutes;
		} catch (ParseException e) {
			e.printStackTrace();
			return 0;
		} 	
	}
}
