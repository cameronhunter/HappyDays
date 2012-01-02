package uk.co.cameronhunter.happydays;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import uk.co.cameronhunter.happydays.utils.Android;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

public class HappyDaysReceiver extends BroadcastReceiver {

	private static final SimpleDateFormat FULL_DATE_FORMAT = new SimpleDateFormat( "yyyy-MM-dd" );
	private static final SimpleDateFormat NO_YEAR_DATE_FORMAT = new SimpleDateFormat( "--MM-dd" );

	@Override
	public void onReceive( Context context, Intent intent ) {
		Date date = new Date( System.currentTimeMillis() );
		if ( date.getHours() != 0 || date.getMinutes() != 0 ) { return; }

		notifyBirthdays( context, date.getDate(), date.getMonth() + 1, date.getYear() );
	}

	private void notifyBirthdays( Context context, int day, int month, int year ) {
		String dateFormat = String.format( "%02d-%02d", month, day );

		String[] selection = { ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.CommonDataKinds.Event.START_DATE, ContactsContract.CommonDataKinds.Event.CONTACT_ID };
		String where = ContactsContract.Data.MIMETYPE + "= ? " + "AND " + ContactsContract.CommonDataKinds.Event.TYPE + " IN (" + ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY + ") "
				+ "AND substr(" + ContactsContract.CommonDataKinds.Event.START_DATE + ", -5, 5)= ?";
		String[] args = { ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE, dateFormat };
		String order = ContactsContract.Contacts.LAST_TIME_CONTACTED + " ASC";

		Log.i( "Date", dateFormat );

		ContentResolver cr = context.getContentResolver();
		Cursor cursor = cr.query( ContactsContract.Data.CONTENT_URI, selection, where, args, order );

		if ( cursor.getCount() > 0 ) {
			while ( cursor.moveToNext() ) {
				try {
					String contactName = cursor.getString( 0 );
					String birthday = cursor.getString( 1 );
					String id = cursor.getString( 2 );

					boolean hasYear = hasYear( birthday );

					SimpleDateFormat format = hasYear ? FULL_DATE_FORMAT : NO_YEAR_DATE_FORMAT;
					Date date = format.parse( birthday );

					String message = hasYear ? "They're " + (year - date.getYear()) + " years old today" : null;

					notification( Uri.withAppendedPath( ContactsContract.Contacts.CONTENT_URI, id ), contactName.hashCode(), contactName + "'s birthday", message, context );
				}
				catch ( ParseException e ) {
					Log.e( "ERROR", e.getMessage() );
				}
			}
		}

		cursor.close();
	}

	private static boolean hasYear( String birthday ) {
		return !birthday.startsWith( "-" );
	}

	private void notification( Uri uri, int id, String title, String message, Context context ) {
		NotificationManager notificationManager = Android.notificationManager( context );

		Notification notification = new Notification( android.R.drawable.ic_menu_my_calendar, title, System.currentTimeMillis() );

		Intent intent = new Intent( Intent.ACTION_VIEW, uri );

		PendingIntent contentIntent = PendingIntent.getActivity( context, 0, intent, 0 );
		notification.setLatestEventInfo( context, title, message, contentIntent );
		notificationManager.notify( id, notification );
	}

}
