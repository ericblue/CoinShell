package io.coinshell.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.LoggerFactory;

public class DateTimeUtils {

    private static final Logger logger = LoggerFactory.getLogger(DateTimeUtils.class);

    private static String       dateFormat = "yyyy-MM-dd";

    public static LocalTime getLocalTimeFromString(String timeString, String format) {

        logger.debug("timeString = " + timeString + " format = " + format);

        if (timeString == null) {
            return null;
        }

        Date date = null;
        try {
            date = new SimpleDateFormat(format).parse(timeString);
        } catch (ParseException e) {
            logger.error(e.getMessage());
        }

        LocalTime time = null;
        if (date != null) {
            time = LocalTime.fromDateFields(date);
        }

        return (time);

    }

    public static DateTime getDateTimeFromString(String timeString, String format) {

        logger.debug("Parsing timestring = " + timeString);

        DateTimeFormatter df = DateTimeFormat.forPattern(format);
        DateTime dt = null;

        // No offset present
        if (timeString.contains("Z")) {
            dt = df.withZone(DateTimeZone.UTC).parseDateTime(timeString);
        } else {
            dt = df.withOffsetParsed().parseDateTime(timeString);
        }

        return dt;

    }

    public static Date getDateFromString(String dateString) {

        DateFormat dateFormat = new SimpleDateFormat(DateTimeUtils.dateFormat);
        Date date = null;
        ;
        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            logger.error(e.getMessage());
        }

        return date;

    }

    public static Date getDateFromString(String dateString, TimeZone tz) {

        DateFormat dateFormat = new SimpleDateFormat(DateTimeUtils.dateFormat);
        if (tz != null) {
            dateFormat.setTimeZone(tz);
        }
        Date date = null;
        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            logger.error(e.getMessage());
        }

        return date;

    }

    public static Date getDateFromString(String dateString, String format, TimeZone tz) {

        DateFormat dateFormat = new SimpleDateFormat(format);
        if (tz != null) {
            logger.debug("Converting to timezone " + tz);
            dateFormat.setTimeZone(tz);
        }

        Date date = null;

        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            logger.error(e.getMessage());
        }

        return date;

    }

    public static Date getDateFromString(String dateString, String format) {

        DateFormat dateFormat = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            logger.error(e.getMessage());
        }

        return date;

    }

    public static String getStringFromDate(Date date, String dateFormat) {

        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        String dateString = sdf.format(date);

        return dateString;

    }

    public static String getDateDelta(String inputDate, int numDays) {

        String endDate = null;

        try {
            Date startDate = new SimpleDateFormat(dateFormat).parse(inputDate);

            logger.debug("inputDate:    " + inputDate);

            // get a calendar instance, which defaults to "now"
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);

            // add numDays(+-) to the date/calendar
            calendar.add(Calendar.DATE, numDays);

            // now get new end date
            Date delta = calendar.getTime();
            SimpleDateFormat df = new SimpleDateFormat(dateFormat);
            StringBuilder sb = new StringBuilder(df.format(delta));
            endDate = sb.toString();

            // print out tomorrow's date
            logger.debug("endDate: " + endDate);

        } catch (ParseException e) {
            logger.error(e.getMessage());
        }
        return endDate;

    }

    public static String getTimeZoneOffsetString(DateTime dt) {

        SimpleDateFormat sd = new SimpleDateFormat("Z");
        sd.setTimeZone(dt.getZone().toTimeZone());
        return sd.format(dt.toDate());

    }

    public static Integer getDaysBetweenDates(Date startDate, Date endDate) {
        return Days.daysBetween(new DateTime(startDate).withTime(0, 0, 0, 0),
                new DateTime(endDate).withTime(23, 59, 59, 0)).getDays();
    }

    public static String detectDateTimeFormat(String date) {

        // TODO Move this incoming time format to Formats
        String DATE_FORMAT = "yyyy-MM-dd";
        String TIME_FORMAT_SPACE1 = "yyyy-MM-dd HH:mm";
        String TIME_FORMAT_SPACE2 = "yyyy-MM-dd HH:mm:ss";
        String TIME_FORMAT_NO_MICRO = "yyyy-MM-dd'T'HH:mm:ss";
        String TIME_FORMAT_NO_MICRO_FULL = "yyyy-MM-dd'T'HH:mm:ssZ";
        String TIME_FORMAT_MICRO = "yyyy-MM-dd'T'HH:mm:ss.S";
        String TIME_FORMAT_FULL = "yyyy-MM-dd'T'HH:mm:ss.SZ";


        String format = null;

        if (date.contains(" ")) {
            if (StringUtils.countMatches(date, ":") > 1) {
                logger.debug("Found TIME_FORMAT_SPACE2");
                format = TIME_FORMAT_SPACE2;
            } else {
                logger.debug("Found TIME_FORMAT_SPACE1");
                format = TIME_FORMAT_SPACE1;
            }

        } else if (date.contains("T")) {
            if (date.contains(".")) {
                String[] parts = date.split("\\.");
                String tzPart = parts[1];
                if (tzPart.contains("+") || tzPart.contains("-"))   {
                    logger.debug("Found TIME_FORMAT_FULL");
                    format = TIME_FORMAT_FULL;
                } else{
                    logger.debug("Found TIME_FORMAT_MICRO");
                    format = TIME_FORMAT_MICRO;
                }
            } else {
                if ( (StringUtils.countMatches(date, "-") >= 3) || (date.contains("+"))) {
                    logger.debug("Found TIME_FORMAT_NO_MICRO_FULL");
                    return TIME_FORMAT_NO_MICRO_FULL;
                } else {
                    logger.debug("Found TIME_FORMAT_NO_MICRO");
                    return TIME_FORMAT_NO_MICRO;
                }
            }
        } else {
            format = DATE_FORMAT;
        }

        logger.debug("returning format = " + format);
        return format;


    }

}
