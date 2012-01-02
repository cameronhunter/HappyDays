package uk.co.cameronhunter.happydays;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;


public class HappyDaysService extends Service {

	@Override
	public void onCreate() {
		registerReceiver( new HappyDaysReceiver(), new IntentFilter( Intent.ACTION_TIME_TICK ) );
	}
	
	@Override
	public IBinder onBind( Intent intent ) {
		return null;
	}

}
