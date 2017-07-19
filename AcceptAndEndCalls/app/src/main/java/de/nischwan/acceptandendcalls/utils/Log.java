package de.nischwan.acceptandendcalls.utils;

/**
 * @author Nico Schwanebeck.
 */
public class Log {
    private static final boolean DEBUG = false;

    public static boolean isDebugEnabled() {
        return DEBUG;
    }

    public static void d(String logTag, String message) {
        if (DEBUG) {
            android.util.Log.d(logTag, message);
        }
    }

    public static void e(String logTag, String message) {
        android.util.Log.e(logTag, message);
    }
}
