package uk.co.cameronhunter.happydays;

import static android.content.Intent.ACTION_DELETE;
import static android.content.Intent.ACTION_INSERT;
import android.content.Intent;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;

public class MainActivity extends PreferenceActivity {

    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );

        addPreferencesFromResource( R.xml.preferences );

        final TimePreference notificationTime = (TimePreference) findPreference( "time" );
        final CheckBoxPreference enabled = (CheckBoxPreference) findPreference( "enabled" );

        notificationTime.setOnPreferenceChangeListener( new OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange( Preference preference, Object newValue ) {
                updateNotificationSummary( notificationTime );
                removeNotificationAlarm();
                if ( enabled.isChecked() ) {
                    createNotificationAlarm();
                }
                return true;
            }
        } );

        enabled.setOnPreferenceChangeListener( new OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange( Preference preference, Object newValue ) {
                if ( Boolean.TRUE.equals( newValue ) ) {
                    createNotificationAlarm();
                } else {
                    removeNotificationAlarm();
                }
                return true;
            }
        } );

        updateNotificationSummary( notificationTime );
        if ( enabled.isChecked() ) {
            createNotificationAlarm();
        } else {
            removeNotificationAlarm();
        }
    }

    private void createNotificationAlarm() {
        sendBroadcast( new Intent( ACTION_INSERT ) );
    }

    private void removeNotificationAlarm() {
        sendBroadcast( new Intent( ACTION_DELETE ) );
    }

    private void updateNotificationSummary( TimePreference notificationTime ) {
        notificationTime.setSummary( getString( R.string.notification_time_summary, notificationTime.getValue() ) );
    }

}
