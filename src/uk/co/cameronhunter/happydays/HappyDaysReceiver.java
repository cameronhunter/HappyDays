package uk.co.cameronhunter.happydays;

import static android.app.AlarmManager.INTERVAL_DAY;
import static android.app.AlarmManager.RTC;
import static android.content.Intent.ACTION_BOOT_COMPLETED;

import java.util.Date;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Pair;

public class HappyDaysReceiver extends BroadcastReceiver {

	@Override
	public void onReceive( Context context, Intent intent ) {

		String action = intent.getAction();

		boolean createAlarmIntent = context.getString( R.string.create_alarm_intent ).equals( action ) || ACTION_BOOT_COMPLETED.equals( action );
		boolean removeAlarmIntent = context.getString( R.string.remove_alarm_intent ).equals( action );

		AlarmManager alarmManager = (AlarmManager) context.getSystemService( Context.ALARM_SERVICE );
		PendingIntent birthdayNotifications = PendingIntent.getService( context, 0, new Intent( context, HappyDaysService.class ), PendingIntent.FLAG_CANCEL_CURRENT );

		if ( createAlarmIntent ) {
			createAlarm( alarmManager, birthdayNotifications, context );
			return;
		}

		if ( removeAlarmIntent ) {
			alarmManager.cancel( birthdayNotifications );
			return;
		}
	}
	
	private static void createAlarm( AlarmManager alarmManager, PendingIntent birthdayNotifications, Context context ) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences( context );
		Pair<Integer, Integer> time = TimePreference.stringToValue( context, preferences.getString( "time", context.getString( R.string.default_time ) ) );

		Date now = new Date( System.currentTimeMillis() );
		now.setHours( time.first.intValue() );
		now.setMinutes( time.second.intValue() );

		alarmManager.setRepeating( RTC, now.getTime(), INTERVAL_DAY, birthdayNotifications );
	}

}
