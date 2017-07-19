package de.nischwan.acceptandendcalls.activities.preferences;

import android.content.Context;
import android.preference.EditTextPreference;
import android.util.AttributeSet;

import de.nischwan.acceptandendcalls.activities.SettingsActivity;
import de.nischwan.acceptandendcalls.utils.DateUtils;

/**
 * @author Nico Schwanebeck
 */
public class TimePreference extends EditTextPreference {

    public TimePreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TimePreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TimePreference(Context context) {
        super(context);
    }

    @Override
    public String getText() {
        return DateUtils.prettifyTime(super.getText());
    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        super.setText(restoreValue ? getPersistedString(null) : (String) defaultValue);
    }

    @Override
    public void setText(String text) {
        if (DateUtils.isValidTime(text)) {
            super.setText(DateUtils.prettifyTime(text));
        } else {
            super.setText(SettingsActivity.DEFAULT_HANGUP_TIME);
        }
    }
}
