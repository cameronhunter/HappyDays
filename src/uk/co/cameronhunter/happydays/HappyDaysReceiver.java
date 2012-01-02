package uk.co.cameronhunter.happydays;

import java.util.Date;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class HappyDaysReceiver extends BroadcastReceiver {

	@Override
	public void onReceive( Context context, Intent intent ) {
		if ( allowNotification( context ) ) {
			context.startService( new Intent( context, HappyDaysNotificationService.class ) );
		}
	}

	private static boolean allowNotification( Context context ) {
		Date date = new Date( System.currentTimeMillis() );

		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences( context );
		String time = preferences.getString( "time", context.getString( R.string.default_time ) );

		return context.getString( R.string.time_format, date.getHours(), context.getString( R.string.time_join_character ), date.getMinutes() ).equals( time );
	}

}
