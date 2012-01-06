package uk.co.cameronhunter.happydays;

import android.content.Intent;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.Preference.OnPreferenceChangeListener;

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
		sendBroadcast( new Intent( getString( R.string.create_alarm_intent ) ) );
	}
	
	private void removeNotificationAlarm() {
		sendBroadcast( new Intent( getString( R.string.remove_alarm_intent ) ) );
	}

	private void updateNotificationSummary( TimePreference notificationTime ) {
		notificationTime.setSummary( getString( R.string.notification_time_summary, notificationTime.getValue() ) );
	}

}
