package de.nischwan.acceptandendcalls.business;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;

/**
 * This class is not written by me.
 * Source: https://gist.github.com/ftvs/e61ccb039f511eb288ee
 * <p>
 * I modified it, though.
 * <p>
 * This class keeps track of the states of a phone call and releases related events.
 */
public abstract class PhoneCallStateHandler extends BroadcastReceiver {

    private static final String LAST_CALL_STATE_KEY = "lastCallState";

    private static boolean isIncoming;

    @Override
    public void onReceive(Context context, Intent intent) {

        if (!intent.getAction().equals("android.intent.action.NEW_OUTGOING_CALL")) {
            String stateStr = intent.getExtras().getString(TelephonyManager.EXTRA_STATE);
            int state = 0;
            if (TelephonyManager.EXTRA_STATE_IDLE.equals(stateStr)) {
                state = TelephonyManager.CALL_STATE_IDLE;
            } else if (TelephonyManager.EXTRA_STATE_OFFHOOK.equals(stateStr)) {
                state = TelephonyManager.CALL_STATE_OFFHOOK;
            } else if (TelephonyManager.EXTRA_STATE_RINGING.equals(stateStr)) {
                state = TelephonyManager.CALL_STATE_RINGING;
            }

            onCallStateChanged(context, state);
        }
    }

    protected abstract void onIncomingCallReceived(Context ctx);

    protected abstract void onIncomingCallAnswered(Context ctx);

    protected abstract void onIncomingCallEnded(Context ctx);

    private void onCallStateChanged(Context context, int state) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        int lastState = preferences.getInt(LAST_CALL_STATE_KEY, TelephonyManager.CALL_STATE_IDLE);

        if (lastState == state) {
            return;
        }

        switch (state) {
            case TelephonyManager.CALL_STATE_RINGING:
                isIncoming = true;
                onIncomingCallReceived(context);
                break;

            case TelephonyManager.CALL_STATE_OFFHOOK:
                if (lastState == TelephonyManager.CALL_STATE_RINGING) {
                    isIncoming = true;
                    onIncomingCallAnswered(context);
                }
                break;

            case TelephonyManager.CALL_STATE_IDLE:
                if (lastState != TelephonyManager.CALL_STATE_RINGING && isIncoming) {
                    onIncomingCallEnded(context);
                }
                break;
        }

        preferences.edit().putInt(LAST_CALL_STATE_KEY, state).apply();
    }
}