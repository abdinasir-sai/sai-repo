package com.nouros.hrms.util.report;

import java.util.Calendar;
import java.util.Date;

public class TimeLogsUtils {
 
	private TimeLogsUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

	public static Date getStartDateOfWeek(int year, int weekNumber) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.WEEK_OF_YEAR, weekNumber);
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        return calendar.getTime();
    }
 
    public static Date getEndDateOfWeek(int year, int weekNumber) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.WEEK_OF_YEAR, weekNumber);
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek() + 4);
        return calendar.getTime();
    }
	
	
}
