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
				return true;
			}
		} );

		enabled.setOnPreferenceChangeListener( new OnPreferenceChangeListener() {

			@Override
			public boolean onPreferenceChange( Preference preference, Object newValue ) {
				if ( Boolean.TRUE.equals( newValue ) ) {
					startService( getServiceIntent() );
				} else {
					stopService( getServiceIntent() );
				}
				return true;
			}
		} );

		updateNotificationSummary( notificationTime );
		if ( enabled.isChecked() ) {
			startService( getServiceIntent() );
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
	
	private void updateNotificationSummary( TimePreference notificationTime ) {
		notificationTime.setSummary( getString( R.string.notification_time_summary, notificationTime.getValue() ) );
	}

	private Intent getServiceIntent() {
		return new Intent( getApplicationContext(), HappyDaysService.class );
	}

}
