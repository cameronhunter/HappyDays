package uk.co.cameronhunter.happydays;

import java.io.InputStream;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.support.v4.app.NotificationCompat;
import android.util.Pair;

public class HappyDaysService extends IntentService {

    private static final DateTimeFormatter FULL_DATE_FORMAT = DateTimeFormat.forPattern( "yyyy-MM-dd" );
    private static final DateTimeFormatter NO_YEAR_DATE_FORMAT = DateTimeFormat.forPattern( "--MM-dd" );

    public HappyDaysService() {
        super( "Happy Days Contact Service" );
    }

    @Override
    protected void onHandleIntent( Intent intent ) {
        DateTime now = new DateTime( System.currentTimeMillis() );
        notifyBirthdays( getApplicationContext(), now.getDayOfMonth(), now.getMonthOfYear(), now.getYear() );
        // notifyBirthdays( getApplicationContext(), 31, 10, 2012 - 1900 );
    }

    private void notifyBirthdays( Context context, int day, int month, int year ) {
        String dateFormat = String.format( "%02d-%02d", month, day );

        String[] selection = { ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.CommonDataKinds.Event.START_DATE, ContactsContract.CommonDataKinds.Event.CONTACT_ID, ContactsContract.CommonDataKinds.Event.TYPE };
        String where = ContactsContract.Data.MIMETYPE + "= ? " + "AND " + ContactsContract.CommonDataKinds.Event.TYPE + " IN (" + ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY + ", " + ContactsContract.CommonDataKinds.Event.TYPE_ANNIVERSARY + ") " + "AND substr(" + ContactsContract.CommonDataKinds.Event.START_DATE + ", -5, 5)= ?";
        String[] args = { ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE, dateFormat };
        String order = ContactsContract.Contacts.LAST_TIME_CONTACTED + " ASC";

        ContentResolver cr = context.getContentResolver();
        Cursor cursor = cr.query( ContactsContract.Data.CONTENT_URI, selection, where, args, order );

        if ( cursor.getCount() > 0 ) {
            while ( cursor.moveToNext() ) {
                String contactName = cursor.getString( 0 );
                String dateText = cursor.getString( 1 );
                String id = cursor.getString( 2 );
                int type = cursor.getInt( 3 );

                boolean hasYear = hasYear( dateText );

                DateTimeFormatter format = hasYear ? FULL_DATE_FORMAT : NO_YEAR_DATE_FORMAT;
                DateTime happyDate = DateTime.parse( dateText, format );

                Uri contactUri = Uri.withAppendedPath( ContactsContract.Contacts.CONTENT_URI, id );

                Pair<String, String> message = getNotificationMessage( contactName, type, hasYear ? (year - happyDate.getYear()) : 0 );

                Notification notification = buildNotification( message.first, message.second, contactUri );

                NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService( NOTIFICATION_SERVICE );
                notificationManager.notify( contactUri.hashCode(), notification );
            }
        }

        cursor.close();
    }

    private Pair<String, String> getNotificationMessage( String contact, int type, int years ) {

        int title;
        int message;

        switch ( type ) {
            case ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY:
                title = R.string.notification_birthday_title;
                message = R.plurals.notification_birthday_message;
                break;

            case ContactsContract.CommonDataKinds.Event.TYPE_ANNIVERSARY:
                title = R.string.notification_anniversary_title;
                message = R.plurals.notification_anniversary_message;
                break;

            default:
                return null;
        }

        String notificationTitle = getString( title, contact );
        String notificationMessage = years == 0 ? null : getResources().getQuantityString( message, years, years );

        return Pair.create( notificationTitle, notificationMessage );
    }

    private static boolean hasYear( String birthday ) {
        return !birthday.startsWith( "-" );
    }

    private Notification buildNotification( String title, String message, Uri contactUri ) {

        NotificationCompat.Builder builder = new NotificationCompat.Builder( this );

        builder.setContentTitle( title ).setContentText( message );
        builder.setSmallIcon( android.R.drawable.ic_menu_my_calendar );

        InputStream photoDataStream = Contacts.openContactPhotoInputStream( getContentResolver(), contactUri );
        if ( photoDataStream != null ) {
            Bitmap thumbnail = BitmapFactory.decodeStream( photoDataStream );
            builder.setLargeIcon( thumbnail );
        }

        builder.setContentIntent( PendingIntent.getActivity( getApplicationContext(), 0, new Intent( Intent.ACTION_VIEW, contactUri ), 0 ) );
        builder.setWhen( System.currentTimeMillis() );

        return builder.build();
    }

}
