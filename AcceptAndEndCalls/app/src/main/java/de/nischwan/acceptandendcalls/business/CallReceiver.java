package de.nischwan.acceptandendcalls.business;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.KeyEvent;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import de.nischwan.acceptandendcalls.activities.SettingsActivity;
import de.nischwan.acceptandendcalls.utils.DateUtils;
import de.nischwan.acceptandendcalls.utils.Log;
import de.nischwan.acceptandendcalls.utils.NotificationUtils;

/**
 * @author Nico Schwanebeck
 */
public class CallReceiver extends PhoneCallStateHandler {

    private static final String LOG_TAG = "CALL_RECEIVER";

    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;

    private void startCallKiller(Context context, int timeInSeconds) {
        Intent killCallIntent = new Intent(context, CallKiller.class);
        pendingIntent = PendingIntent.getBroadcast(context, 0, killCallIntent, 0);
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        long now = System.currentTimeMillis();
        long fire = now + timeInSeconds * 1_000;
        if (Log.isDebugEnabled()) {
            Log.d(LOG_TAG, "Call ends: " + new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date(fire)));
        }
        alarmManager.cancel(pendingIntent);
        alarmManager.set(AlarmManager.RTC_WAKEUP, fire, pendingIntent);
    }

    private void delayAcceptCall() {
        Random rand = new Random();
        int secondsToWait = rand.nextInt(3) + 2;
        try {
            Thread.sleep(secondsToWait * 1000);
        } catch (InterruptedException e) {
            // nothing to do
        }
    }

    private void acceptCall(Context context) {
        Intent buttonUp = new Intent(Intent.ACTION_MEDIA_BUTTON);
        buttonUp.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(
                KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK));
        try {
            context.sendOrderedBroadcast(buttonUp, "android.permission.CALL_PRIVILEGED");
        } catch (Exception e) {
            Log.d(LOG_TAG, e.getMessage());
        }
        Intent headSetUnPluggedIntent = new Intent(Intent.ACTION_HEADSET_PLUG);
        headSetUnPluggedIntent.addFlags(Intent.FLAG_RECEIVER_REGISTERED_ONLY);
        headSetUnPluggedIntent.putExtra("state", 0);
        headSetUnPluggedIntent.putExtra("name", "Headset");
        try {
            context.sendOrderedBroadcast(headSetUnPluggedIntent, null);
            Log.d(LOG_TAG, "Accept call intent sent.");
        } catch (Exception e) {
            Log.d(LOG_TAG, e.getMessage());
        }
    }

    @Override
    protected void onIncomingCallReceived(Context ctx) {
        Log.d(LOG_TAG, "incoming call");

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        boolean auto_accept = preferences.getBoolean(SettingsActivity.ACCEPT_CALL_KEY, false);

        if (auto_accept) {
            delayAcceptCall();
            acceptCall(ctx);
        }
    }

    @Override
    protected void onIncomingCallAnswered(Context ctx) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        Log.d(LOG_TAG, "Call answered");
        String time = preferences.getString(SettingsActivity.HANG_UP_TIME_KEY, SettingsActivity.DEFAULT_HANGUP_TIME);
        int durationInSeconds = DateUtils.timeToSeconds(time);
        int offsetTime = Integer.parseInt(
                preferences.getString(SettingsActivity.OFFSET_HANGUP_TIME, SettingsActivity.DEFAULT_OFFSET_HANGUP_TIME)
        );
        int offsetInSeconds = DateUtils.calculateTimeOffsetInSeconds(offsetTime);
        int callDurationInSeconds = durationInSeconds + offsetInSeconds;
        Date hangupTime = new Date(System.currentTimeMillis() + (callDurationInSeconds * 1_000));
        NotificationUtils.displayHangupTimeNotification(ctx, DateUtils.dateToString(hangupTime));
        startCallKiller(ctx, callDurationInSeconds);
    }

    @Override
    protected void onIncomingCallEnded(Context ctx) {
        Intent killCallIntent = new Intent(ctx, CallKiller.class);
        pendingIntent = PendingIntent.getBroadcast(ctx, 0, killCallIntent, 0);
        alarmManager = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
        NotificationUtils.removeNotification(ctx);
    }
}
