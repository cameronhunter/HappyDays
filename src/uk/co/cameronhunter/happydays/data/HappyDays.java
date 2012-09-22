package uk.co.cameronhunter.happydays.data;

import java.util.HashSet;
import java.util.Set;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

public final class HappyDays {

    private static final DateTimeFormatter FULL_DATE_FORMAT = DateTimeFormat.forPattern( "yyyy-MM-dd" );
    private static final DateTimeFormatter NO_YEAR_DATE_FORMAT = DateTimeFormat.forPattern( "--MM-dd" );

    public static Set<HappyDay> get( Context context, int day, int month, int year ) {
        String dateFormat = String.format( "%02d-%02d", month, day );

        String[] selection = { ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.CommonDataKinds.Event.START_DATE, ContactsContract.CommonDataKinds.Event.CONTACT_ID, ContactsContract.CommonDataKinds.Event.TYPE };
        String where = ContactsContract.Data.MIMETYPE + "= ? " + "AND " + ContactsContract.CommonDataKinds.Event.TYPE + " IN (" + ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY + ", " + ContactsContract.CommonDataKinds.Event.TYPE_ANNIVERSARY + ") " + "AND substr(" + ContactsContract.CommonDataKinds.Event.START_DATE + ", -5, 5)= ?";
        String[] args = { ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE, dateFormat };
        String order = ContactsContract.Contacts.LAST_TIME_CONTACTED + " ASC";

        Cursor cursor = null;
        try {
            Set<HappyDay> happyDays = new HashSet<HappyDay>();

            ContentResolver cr = context.getContentResolver();
            cursor = cr.query( ContactsContract.Data.CONTENT_URI, selection, where, args, order );

            if ( cursor.moveToFirst() ) {
                while ( !cursor.isAfterLast() ) {
                    String contactName = cursor.getString( 0 );
                    String dateText = cursor.getString( 1 );
                    String id = cursor.getString( 2 );
                    int type = cursor.getInt( 3 );

                    boolean hasYear = hasYear( dateText );

                    DateTime date = DateTime.parse( dateText, hasYear ? FULL_DATE_FORMAT : NO_YEAR_DATE_FORMAT );

                    Uri contactUri = Uri.withAppendedPath( ContactsContract.Contacts.CONTENT_URI, id );

                    happyDays.add( new HappyDay( type, contactUri, contactName, hasYear ? (year - date.getYear()) : 0 ) );

                    cursor.moveToNext();
                }
            }

            return happyDays;
        } finally {
            if ( cursor != null ) cursor.close();
        }
    }

    private static boolean hasYear( String birthday ) {
        return !birthday.startsWith( "-" );
    }

    private HappyDays() {}
}
