package uk.co.cameronhunter.happydays.receivers;

import static android.app.AlarmManager.INTERVAL_DAY;
import static android.app.AlarmManager.RTC;
import static android.content.Context.ALARM_SERVICE;
import static android.content.Intent.ACTION_BOOT_COMPLETED;
import static android.content.Intent.ACTION_DELETE;
import static android.content.Intent.ACTION_INSERT;

import org.joda.time.DateTime;

import uk.co.cameronhunter.happydays.R;
import uk.co.cameronhunter.happydays.TimePreference;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Pair;

public class AlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive( Context context, Intent intent ) {
		String action = intent.getAction();

		boolean createAlarmIntent = ACTION_INSERT.equals( action ) || ACTION_BOOT_COMPLETED.equals( action );
		boolean removeAlarmIntent = ACTION_DELETE.equals( action );

		AlarmManager alarmManager = (AlarmManager) context.getSystemService( ALARM_SERVICE );
		
		PendingIntent birthdayNotifications = PendingIntent.getBroadcast( context, 0, new Intent( context, NotificationReceiver.class ), PendingIntent.FLAG_CANCEL_CURRENT );
		
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

		DateTime notificationTime = new DateTime( System.currentTimeMillis() );
		notificationTime.withHourOfDay( time.first.intValue() );
		notificationTime.withMinuteOfHour( time.second.intValue() );

		alarmManager.setRepeating( RTC, notificationTime.getMillis(), INTERVAL_DAY, birthdayNotifications );
	}

}
