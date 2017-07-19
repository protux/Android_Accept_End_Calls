package de.nischwan.acceptandendcalls;

import android.app.Application;
import android.content.Context;

import org.acra.ACRA;
import org.acra.annotation.ReportsCrashes;

import de.nischwan.acceptandendcalls.acra.AcraReportSenderFactory;

/**
 * @author Nico Schwanebeck
 */
@ReportsCrashes(
        reportSenderFactoryClasses = {AcraReportSenderFactory.class}, alsoReportToAndroidFramework = true
)
public class AcceptAndEndCallApplication extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        ACRA.init(this);
    }
}
