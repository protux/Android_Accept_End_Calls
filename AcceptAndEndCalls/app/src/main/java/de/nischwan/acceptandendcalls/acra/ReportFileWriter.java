package de.nischwan.acceptandendcalls.acra;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;

import org.acra.ReportField;
import org.acra.collector.CrashReportData;
import org.acra.model.Element;
import org.acra.sender.ReportSender;
import org.acra.sender.ReportSenderException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

/**
 * @author Nico Schwanebeck
 */
public class ReportFileWriter implements ReportSender {

    private static final File OUTPUT_DIR = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
    private static final String REPORT_PREFIX = "aec_errorreport_";

    @Override
    public void send(@NonNull Context context, @NonNull CrashReportData crashReportData) throws ReportSenderException {
        try {
            File outputFile = new File(OUTPUT_DIR, REPORT_PREFIX + System.currentTimeMillis());
            FileWriter fileWriter = new FileWriter(outputFile);
            for (Map.Entry<ReportField, Element> reportElement : crashReportData.entrySet()) {
                fileWriter.write(reportElement.getKey().name());
                fileWriter.write("\n=====\n");
                fileWriter.write(reportElement.getValue().value().toString());
                fileWriter.write("\n-----\n\n");
            }
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            throw new ReportSenderException(e.getMessage());
        }
    }
}
