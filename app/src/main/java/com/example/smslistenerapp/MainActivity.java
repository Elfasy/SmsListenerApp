package com.example.smslistenerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int SMS_REQUEST_CODE = 101;
    public static final String ACTION_DOWNLOAD_STATUS = "download_status";
    private BroadcastReceiver downloadReceiver;
    Button btnCheckPermission,btnDownload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCheckPermission = findViewById(R.id.btn_permission);
        btnCheckPermission.setOnClickListener(this);
        btnDownload = findViewById(R.id.btn_download);
        btnDownload.setOnClickListener(this);
        downloadReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(DownloadService.TAG,"Download selesai");
                Toast.makeText(context,"Download selesai",Toast.LENGTH_SHORT).show();
            }
        };
        IntentFilter intentFilter = new IntentFilter(ACTION_DOWNLOAD_STATUS);

        registerReceiver(downloadReceiver,intentFilter);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_permission) {
            PermissionManager.check(this, Manifest.permission.RECEIVE_SMS, SMS_REQUEST_CODE);
        }else if(view.getId() == R.id.btn_download){
            Intent downloadServiceIntent = new Intent(MainActivity.this,DownloadService.class);
            startService(downloadServiceIntent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == SMS_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "sms permission diterima", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "sms permission ditolak", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (downloadReceiver != null){
            unregisterReceiver(downloadReceiver);
        }
    }
}
