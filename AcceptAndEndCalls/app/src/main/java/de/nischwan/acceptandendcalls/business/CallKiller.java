package de.nischwan.acceptandendcalls.business;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.lang.reflect.Method;

import de.nischwan.acceptandendcalls.utils.Log;

/**
 * @author Nico Schwanebeck
 */
public class CallKiller extends BroadcastReceiver {

    private static final String LOG_TAG = "CALL_KILLER";

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Log.d(LOG_TAG, "starting to end call.");

            String serviceManagerName = "android.os.ServiceManager";
            String serviceManagerNativeName = "android.os.ServiceManagerNative";
            String telephonyName = "com.android.internal.telephony.ITelephony";

            Class telephonyClass;
            Class telephonyStubClass;
            Class serviceManagerClass;
            Class serviceManagerNativeClass;

            Method telephonyEndCall;

            // Method getService;
            Object telephonyObject;
            Object serviceManagerObject;

            telephonyClass = Class.forName(telephonyName);
            telephonyStubClass = telephonyClass.getClasses()[0];
            serviceManagerClass = Class.forName(serviceManagerName);
            serviceManagerNativeClass = Class.forName(serviceManagerNativeName);

            Method getService = serviceManagerClass.getMethod("getService", String.class);

            Method tempInterfaceMethod = serviceManagerNativeClass.getMethod(
                    "asInterface", IBinder.class);

            Binder tmpBinder = new Binder();
            tmpBinder.attachInterface(null, "fake");

            serviceManagerObject = tempInterfaceMethod.invoke(null, tmpBinder);
            IBinder retbinder = (IBinder) getService.invoke(serviceManagerObject, "phone");
            Method serviceMethod = telephonyStubClass.getMethod("asInterface", IBinder.class);

            telephonyObject = serviceMethod.invoke(null, retbinder);
            telephonyEndCall = telephonyClass.getMethod("endCall");

            telephonyEndCall.invoke(telephonyObject);
            Log.d(LOG_TAG, "call should end now");

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "FATAL ERROR: could not connect to telephony subsystem");
            Log.e(LOG_TAG, "Exception object: " + e);
        }
    }
}
