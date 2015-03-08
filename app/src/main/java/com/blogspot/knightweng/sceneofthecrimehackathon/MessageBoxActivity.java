package com.blogspot.knightweng.sceneofthecrimehackathon;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MessageBoxActivity extends Activity
{
    public ProgressDialog p;

    public void UploadFile(String fileName)
    {
        UploadFileTask uploadFileTask = new UploadFileTask(fileName);
        uploadFileTask.execute();
    }

    public void showMessageBox()
    {
        p=new ProgressDialog(this);
        p.setTitle("CALL OF JUSTICE");
        p.setMessage("HERO OR NOT ? \nCLICKED FOR UPLOAD YOUR EVIDENCE");
        p.setButton("YES", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                UploadFile("VID_20150308_094554.mp4");
                //walkdir(Environment.getExternalStorageDirectory());
                Intent intent = new Intent(MessageBoxActivity.this, com.blogspot.knightweng.sceneofthecrimehackathon.SceneOfTheCrimeHackathonService.class);
                startService(intent);

                finish();
            }
        });
        p.setButton2("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(MessageBoxActivity.this, com.blogspot.knightweng.sceneofthecrimehackathon.SceneOfTheCrimeHackathonService.class);
                startService(intent);

                finish();
            }
        });
        p.show();

        new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long second) {
                for(int i=1;i<3;i++){
                    p.setTitle("倒數"+ (second/1000)+"秒");

                }
            }

            @Override
            public void onFinish() {
                p.setTitle("TIME'S UP");
                p.setMessage("倒數已結束，是否上傳檔案");
            }
        }.start();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle intent_extras = getIntent().getExtras();
        if (intent_extras != null && intent_extras.containsKey("com.blogspot.knightweng.sceneofthecrimehackathon"))
        {
            Log.d("Upload", "MessageBox");
            showMessageBox();
            //UploadFile("IMG_20150307_190252.jpg");
        }


    }

    @Override
    protected void onDestroy() {
        p.dismiss();
        super.onDestroy();
    }

    public class UploadFileTask extends AsyncTask<Void, Integer, Void> {
        private String mFileName;

        public UploadFileTask(String fileName) {
            mFileName = fileName;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... voids) {

            Log.d("Upload", "Upload fileName: " + "/storage/emulated/legacy/DCIM/Camera/" + mFileName);
            // Set your file path here
            //FileInputStream fstrm = new FileInputStream("/storage/emulated/legacy/DCIM/Camera/" + mFileName);

            // Set your server page url (and the file title/description)
            HttpFileUpload hfu = new HttpFileUpload("http://192.168.1.33/upload.php");

            hfu.SendNow("/storage/emulated/legacy/DCIM/Camera/" + mFileName);

            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            Log.d("Upload", "onPostExecute");
            Toast.makeText(getApplicationContext(), "Upload complete", Toast.LENGTH_LONG).show();
        }
    }
}


