package de.nischwan.acceptandendcalls.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

/**
 * Offers some utility methods for handling dates and times.
 *
 * @author Nico Schwanebeck
 */
public final class DateUtils {
    private static final String LOG_TAG = DateUtils.class.getName();

    private DateUtils() {
        // access restriction
    }

    /**
     * @param time the time to check
     * @return true, if time contains a minute >= 0 and a second >= 0 and < 60.
     */
    public static boolean isValidTime(String time) {
        if (time != null && time.matches("\\d+:\\d+")) {
            String[] split = time.split(":");
            if (split.length == 2) {
                int minutes = Integer.parseInt(split[0]);
                int seconds = Integer.parseInt(split[1]);
                if (minutes >= 0 && seconds >= 0 && seconds < 60) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * @param time the time to prettify
     * @return the {@code time} in format "mm:ss"
     */
    public static String prettifyTime(String time) {
        String[] split = time.split(":");

        String minutes = split[0];
        if (minutes.length() == 1) {
            minutes = "0" + minutes;
        }

        String seconds = split[1];
        if (seconds.length() == 1) {
            seconds = "0" + seconds;
        }

        return minutes + ":" + seconds;
    }

    public static String dateToString(Date date) {
        String[] time = SimpleDateFormat.getTimeInstance().format(date).split(":");
        return time[0] + ":" + time[1];
    }

    /**
     * @param time pattern: "mm:ss"
     * @return the time in seconds.
     * @throws IllegalArgumentException if {@code time} is no time.
     */
    public static int timeToSeconds(String time) {
        if (time != null && time.contains(":")) {
            String[] split = time.trim().split(":");
            if (split.length == 2) {
                int minutes = Integer.parseInt(split[0]);
                int seconds = Integer.parseInt(split[1]);
                return minutes * 60 + seconds;
            }
        }

        throw new IllegalArgumentException("Time must be 'mm:ss'.");
    }

    /**
     * @param timeOffset the maximum offset time.
     * @return +/- the passed time in seconds.
     */
    public static int calculateTimeOffsetInSeconds(int timeOffset) {
        if (timeOffset <= 0) {
            return 0;
        }

        int maxOffsetInSeconds = timeOffset * 60;
        int offsetInSeconds = new Random().nextInt(maxOffsetInSeconds * 2) - maxOffsetInSeconds;
        if (Log.isDebugEnabled())
            Log.d(LOG_TAG, String.format(Locale.getDefault(), "Calculated offset: %d", offsetInSeconds));
        return offsetInSeconds;
    }
}
