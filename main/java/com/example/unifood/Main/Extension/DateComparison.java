package com.example.unifood.Main.Extension;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateComparison
{
    public static String comparisonString(String firstDate, String secondDate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date date1 = sdf.parse(firstDate);
        Date date2 = sdf.parse(secondDate);

        if (date1.after(date2)) {
            return "expired";
        } else {
            return "available";
        }
    }

    public static String daysUntil(String date1, String date2) throws ParseException {
        // Parse dates to obtain milliseconds
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date d1 = sdf.parse(date1);
        Date d2 = sdf.parse(date2);
        long diff = d2.getTime() - d1.getTime();

        // Calculate difference in days
        long daysDiff = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        return "Expires in " + String.valueOf(daysDiff) + " days";
    }

    public static Boolean checkWeek(String date2) {
        // Parse dates to obtain milliseconds
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date d1 = new Date();
        Date d2 = null;
        try {
            d2 = sdf.parse(date2);
        } catch (ParseException e) {
            d2 = new Date();
        }
        long diffInMilliseconds = d1.getTime() - d2.getTime();

        // Convert milliseconds to days
        long diffInDays = diffInMilliseconds / (24 * 60 * 60 * 1000);

        // Check if the date is more than a week ago
        return diffInDays > 7;
    }

    public static String defaultExpiry(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.WEEK_OF_YEAR, 2);
        Date twoWeeksAhead = calendar.getTime();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = dateFormat.format(twoWeeksAhead);

        return formattedDate;
    }

    public static String currentDay(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();
        String currentDateString = dateFormat.format(currentDate);

        return currentDateString;
    }
}
