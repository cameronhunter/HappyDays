package uk.co.cameronhunter.happydays;

import android.app.backup.BackupAgentHelper;
import android.app.backup.SharedPreferencesBackupHelper;

public class BackupManager extends BackupAgentHelper {

	@Override
	public void onCreate() {
		SharedPreferencesBackupHelper helper = new SharedPreferencesBackupHelper( this, getPackageName() + "_preferences" );
		addHelper( "preferences", helper );
	}
	
}
