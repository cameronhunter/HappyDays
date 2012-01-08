/**
 * 
 */
package uk.co.cameronhunter.happydays.adapters.notificationBuilder;

import uk.co.cameronhunter.happydays.utils.Check;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;

class HoneycombBuilder extends NotificationBuilder {

	private final Class<?> clazz;
	private final Object builder;

	public HoneycombBuilder( Context context ) throws ClassNotFoundException  {
		this.clazz = Check.notNull( findNotificationBuilder() );
		this.builder = Check.notNull( getInstance( clazz, context ) );
	}

	@Override
	public Notification getNotification() {
		try {
			return (Notification) clazz.getMethod( "getNotification" ).invoke( builder );
		}
		catch ( Exception e ) {}
		return null;
	}

	@Override
	public NotificationBuilder setContentIntent( PendingIntent pendingIntent ) {
		try {
			clazz.getMethod( "setContentIntent", PendingIntent.class ).invoke( builder, pendingIntent );
		}
		catch ( Exception e ) {}
		return this;
	}

	@Override
	public NotificationBuilder setContentText( CharSequence text ) {
		try {
			clazz.getMethod( "setContentText", CharSequence.class ).invoke( builder, text );
		}
		catch ( Exception e ) {}
		return this;
	}

	@Override
	public NotificationBuilder setContentTitle( CharSequence title ) {
		try {
			clazz.getMethod( "setContentTitle", CharSequence.class ).invoke( builder, title );
		}
		catch ( Exception e ) {}
		return this;
	}

	@Override
	public NotificationBuilder setLargeIcon( Bitmap bitmap ) {
		try {
			clazz.getMethod( "setLargeIcon", Bitmap.class ).invoke( builder, bitmap );
		}
		catch ( Exception e ) {}
		return this;
	}

	@Override
	public NotificationBuilder setSmallIcon( int drawable ) {
		try {
			clazz.getMethod( "setSmallIcon", int.class ).invoke( builder, drawable );
		}
		catch ( Exception e ) {}
		return this;
	}

	@Override
	public NotificationBuilder setWhen( long when ) {
		try {
			clazz.getMethod( "setWhen", long.class ).invoke( builder, when );
		}
		catch ( Exception e ) {}
		return this;
	}

	private static Class<?> findNotificationBuilder() throws ClassNotFoundException {
		Class<?> notificationClass = Class.forName( "android.app.Notification" );
		for ( Class<?> clazz : notificationClass.getClasses() ) {
			if ( "android.app.Notification.Builder".equals( clazz.getCanonicalName() ) ) { return clazz; }
		}
		return null;
	}
	
	private static Object getInstance( Class<?> clazz, Context context ) {
		try {
			return clazz.getConstructor( Context.class ).newInstance( context );
		}
		catch ( Exception e ) {}
		return null;
	}

}
