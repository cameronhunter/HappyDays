package uk.co.cameronhunter.happydays.adapters.notificationBuilder;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

public abstract class NotificationBuilder {

	private static final int HONEYCOMB_API_VERSION = 11;

	public static NotificationBuilder create( Context context ) {
		if ( android.os.Build.VERSION.SDK_INT >= HONEYCOMB_API_VERSION ) {
			try {
				return new HoneycombBuilder( context );
			}
			catch ( Exception e ) {
				Log.e( NotificationBuilder.class.getCanonicalName(), "Couldn't use API 11 notification builder", e );
			}
		}

		return new GingerbreadBuilder( context );
	}
	
	public abstract NotificationBuilder setContentIntent( PendingIntent pendingIntent );
	public abstract NotificationBuilder setContentText( CharSequence text );
	public abstract NotificationBuilder setContentTitle( CharSequence title );
	public abstract NotificationBuilder setLargeIcon( Bitmap bitmap );
	public abstract NotificationBuilder setSmallIcon( int drawable );
	public abstract NotificationBuilder setWhen( long when );
	
	public abstract Notification getNotification();
}
