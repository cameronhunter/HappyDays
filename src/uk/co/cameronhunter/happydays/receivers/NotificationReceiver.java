package uk.co.cameronhunter.happydays.receivers;

import static android.content.Context.NOTIFICATION_SERVICE;
import static android.content.Intent.ACTION_VIEW;

import java.io.InputStream;
import java.util.Set;

import org.joda.time.DateTime;

import uk.co.cameronhunter.happydays.R;
import uk.co.cameronhunter.happydays.data.HappyDay;
import uk.co.cameronhunter.happydays.data.HappyDays;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.support.v4.app.NotificationCompat;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive( Context context, Intent intent ) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService( NOTIFICATION_SERVICE );

        notificationManager.cancelAll();

        DateTime now = new DateTime( System.currentTimeMillis() );
//        DateTime now = new DateTime( 2012, 10, 31, 0, 0 );
        
        Set<HappyDay> happyDays = HappyDays.get( context, now.getDayOfMonth(), now.getMonthOfYear(), now.getYear() );

        for ( HappyDay happyDay : happyDays ) {
            Notification notification = notificationFor( happyDay, context );
            if ( notification != null ) notificationManager.notify( happyDay.uri.hashCode(), notification );
        }
    }

    private static Notification notificationFor( HappyDay happyDay, Context context ) {
        int title;
        int message;

        switch ( happyDay.type ) {
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

        String notificationTitle = context.getString( title, happyDay.name );
        String notificationMessage = happyDay.yearsOld == 0 ? null : context.getResources().getQuantityString( message, happyDay.yearsOld, happyDay.yearsOld );

        NotificationCompat.Builder builder = new NotificationCompat.Builder( context );

        builder.setContentTitle( notificationTitle ).setContentText( notificationMessage );
        builder.setSmallIcon( android.R.drawable.ic_menu_my_calendar );

        InputStream photoDataStream = Contacts.openContactPhotoInputStream( context.getContentResolver(), happyDay.uri );
        if ( photoDataStream != null ) {
            Bitmap thumbnail = BitmapFactory.decodeStream( photoDataStream );
            builder.setLargeIcon( thumbnail );
        }

        builder.setContentIntent( PendingIntent.getActivity( context, 0, new Intent( ACTION_VIEW, happyDay.uri ), 0 ) );

        return builder.build();
    }

}
