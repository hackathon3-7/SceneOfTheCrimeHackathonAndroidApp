package com.blogspot.knightweng.sceneofthecrimehackathon;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

public class SceneOfTheCrimeHackathonActivity extends Activity
{
    /**
     * This activity loads a service.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(SceneOfTheCrimeHackathonActivity.this, com.blogspot.knightweng.sceneofthecrimehackathon.SceneOfTheCrimeHackathonService.class);
        startService(intent);

        finish();
        /*Intent intent = new Intent(this, MessageBoxActivity.class);
        intent.putExtra("com.blogspot.knightweng.sceneofthecrimehackathon", 5566);
        startActivity(intent);*/
    }


}


