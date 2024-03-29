package com.example.trygooglemapsapi;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class PermissionsActivity extends AppCompatActivity {

    private Button btnGrant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permissions);


      ///  Проверяет разрешение на получение геолокации ///
        if (ContextCompat.checkSelfPermission(PermissionsActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            startActivity(new Intent(PermissionsActivity.this,MapActivity.class));
            finish();
            return;
        }

        btnGrant = findViewById(R.id.btn_grant);

        btnGrant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dexter.withActivity(PermissionsActivity.this)
                        .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response) {

                                startActivity(new Intent(PermissionsActivity.this,MapActivity.class));
                                finish();

                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) {

                                if(response.isPermanentlyDenied()) {
                                    AlertDialog.Builder builder =
                                            new AlertDialog.Builder(PermissionsActivity.this);
                                    builder.setTitle("Permission Denied")
                                            .setMessage("Permission to access devise location is permanently denied. You need to allow the permission.  ")
                                            .setNegativeButton("Cansel", null)
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    //открывает настройки//
                                                    Intent intent = new Intent();
                                                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                                    intent.setData(Uri.fromParts("package", getPackageName(), null));

                                                }
                                            })
                                            .show();
                                }
                                   else {
                                    Toast.makeText(PermissionsActivity.this,"Permission Denied",Toast.LENGTH_SHORT);
                                    }


                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                                token.continuePermissionRequest();
                            }
                        })
                        .check();
            }
        });




    }
}

