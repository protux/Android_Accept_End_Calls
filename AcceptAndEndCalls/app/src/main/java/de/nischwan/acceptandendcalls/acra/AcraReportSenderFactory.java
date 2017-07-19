package de.nischwan.acceptandendcalls.acra;

import android.content.Context;
import android.support.annotation.NonNull;

import org.acra.config.ACRAConfiguration;
import org.acra.sender.ReportSender;
import org.acra.sender.ReportSenderFactory;

/**
 * @author Nico Schwanebeck
 */
public class AcraReportSenderFactory implements ReportSenderFactory {

    @NonNull
    @Override
    public ReportSender create(@NonNull Context context, @NonNull ACRAConfiguration acraConfiguration) {
        return new ReportFileWriter();
    }
}
