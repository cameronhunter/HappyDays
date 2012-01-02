package uk.co.cameronhunter.happydays.utils;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.AUDIO_SERVICE;
import static android.content.Context.LOCATION_SERVICE;
import static android.content.Context.NOTIFICATION_SERVICE;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.content.Context;
import android.location.LocationManager;
import android.media.AudioManager;

public final class Android {

	public static AudioManager audioManager( Context context ) {
		return (AudioManager) context.getSystemService( AUDIO_SERVICE );
	}

	public static LocationManager locationManager( Context context ) {
		return (LocationManager) context.getSystemService( LOCATION_SERVICE );
	}

	public static NotificationManager notificationManager( Context context ) {
		return (NotificationManager) context.getSystemService( NOTIFICATION_SERVICE );
	}

	public static AlarmManager alarmManager( Context context ) {
		return (AlarmManager) context.getSystemService( ALARM_SERVICE );
	}
	
	private Android() {}
}
