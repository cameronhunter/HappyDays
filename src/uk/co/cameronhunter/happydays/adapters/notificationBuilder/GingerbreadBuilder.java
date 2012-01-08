/**
 * 
 */
package uk.co.cameronhunter.happydays.adapters.notificationBuilder;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;

class GingerbreadBuilder extends NotificationBuilder {

	private final Context context;
	private final Notification notification;

	private CharSequence title;
	private CharSequence message;
	private PendingIntent contentIntent;

	public GingerbreadBuilder( Context context ) {
		this.context = context;
		this.notification = new Notification();
	}

	@Override
	public NotificationBuilder setContentTitle( CharSequence title ) {
		this.title = title;
		return this;
	}

	@Override
	public NotificationBuilder setContentText( CharSequence text ) {
		this.message = text;
		return this;
	}

	@Override
	public NotificationBuilder setSmallIcon( int drawable ) {
		notification.icon = drawable;
		return this;
	}

	@Override
	public NotificationBuilder setContentIntent( PendingIntent pendingIntent ) {
		this.contentIntent = pendingIntent;
		return this;
	}

	@Override
	public NotificationBuilder setLargeIcon( Bitmap bitmap ) {
		return this; // Not supported in pre-honeycomb api :(
	}

	@Override
	public NotificationBuilder setWhen( long when ) {
		notification.when = when;
		return this;
	}

	@Override
	public Notification getNotification() {
		notification.setLatestEventInfo( context, title, message, contentIntent );
		return notification;
	}
}