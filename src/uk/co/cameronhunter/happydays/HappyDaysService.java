package uk.co.cameronhunter.happydays;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

public class HappyDaysService extends Service {

	private HappyDaysReceiver receiver;

	@Override
	public void onCreate() {
		super.onCreate();
		receiver = new HappyDaysReceiver();
		registerReceiver( receiver, new IntentFilter( Intent.ACTION_TIME_TICK ) );
	}

	@Override
	public void onDestroy() {
		unregisterReceiver( receiver );
		super.onDestroy();
	}

	@Override
	public IBinder onBind( Intent intent ) {
		return null;
	}

}
