package com.foodcityapp.esa.foodcityapplication;

import android.*;
import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ListFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.Manifest.permission.CAMERA;

public class QRCodeActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{


    private static final int REQUEST_CAMERA = 1;
    private ZXingScannerView zXingScannerView;

    //Button scnBtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        zXingScannerView = new ZXingScannerView(QRCodeActivity.this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarqr);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("FoodCity");

        ViewGroup contentFrame = (ViewGroup) findViewById(R.id.content_frame);

        contentFrame.addView(zXingScannerView);
       /* scnBtn = (Button) findViewById(R.id.scnBtn);
        scnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(zXingScannerView);
                zXingScannerView.setResultHandler(QRCodeActivity.this);
                zXingScannerView.startCamera();

            }
        });*/

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(CheckPermission()){

                Toast.makeText(this,"Permission granted!",Toast.LENGTH_LONG).show();
            }else{
                RequestPermission();
            }
        }
    }

    private void RequestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{CAMERA}, REQUEST_CAMERA);
    }

    private boolean CheckPermission() {
        return (ContextCompat.checkSelfPermission(QRCodeActivity.this, CAMERA) == PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.qrmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_showlist) {
            Intent listIntent = new Intent(QRCodeActivity.this, MainActivity.class);
            listIntent.putExtra("PAGE",2);
            startActivity(listIntent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void onRequestPermissionsResult(int requestCode, String permission[], int grantResults[]){
        switch (requestCode){
            case REQUEST_CAMERA:
                if(grantResults.length > 0){
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if(cameraAccepted){
                        Toast.makeText(this, "Permission granted!", Toast.LENGTH_LONG);

                    }else {
                        Toast.makeText(this,"Permission denied!", Toast.LENGTH_LONG);
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                            if(shouldShowRequestPermissionRationale(CAMERA)){

                                DisplayAlertMessage("You need to allow access for both permission", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                            requestPermissions(new String[]{CAMERA}, REQUEST_CAMERA);
                                        }
                                    }
                                });
                                return;
                            }
                        }
                    }
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        zXingScannerView.setResultHandler(this);
        zXingScannerView.startCamera();

       /* if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(CheckPermission()){
                if(zXingScannerView == null){
                    zXingScannerView.setResultHandler(this);
                    zXingScannerView.startCamera();
                }
            }else {
                RequestPermission();
            }
        }*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        zXingScannerView.stopCamera();
    }

    public void DisplayAlertMessage(String message, DialogInterface.OnClickListener listener){
        new AlertDialog.Builder(QRCodeActivity.this).setMessage(message).setPositiveButton("OK",listener).setNegativeButton("Cancel", null).create();
    }

    @Override
    protected void onPause() {
        super.onPause();
        zXingScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result result) {

        final String scanResult = result.getText();
        //zXingScannerView.stopCamera();
        Toast.makeText(this, scanResult,Toast.LENGTH_LONG).show();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent listIntent = new Intent(QRCodeActivity.this, MainActivity.class);
                listIntent.putExtra("PAGE",2);
                startActivity(listIntent);
                finish();
               //createNewList();
            }
        }, 2000);
    }



}
