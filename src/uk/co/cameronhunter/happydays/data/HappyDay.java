package uk.co.cameronhunter.happydays.data;

import android.net.Uri;

public class HappyDay {

    public final int type;
    public final Uri uri;
    public final String name;
    public final int yearsOld;

    public HappyDay( int type, Uri uri, String name, int years ) {
        this.type = type;
        this.uri = uri;
        this.name = name;
        this.yearsOld = years;
    }
    
}
