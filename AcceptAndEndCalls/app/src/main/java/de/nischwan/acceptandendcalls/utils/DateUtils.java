package de.nischwan.acceptandendcalls.utils;

/**
 * Offers some utility methods for handling dates and times.
 *
 * @author Nico Schwanebeck
 */
public final class DateUtils {
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
}
