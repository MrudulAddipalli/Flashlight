package com.example.flash;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private String[] permissions = {Manifest.permission.CAMERA};
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        if (arePermissionsEnabled()) {
            Intent i = new Intent();
            i.setAction(Intent.ACTION_MAIN);
            i.addCategory(Intent.CATEGORY_HOME);
            this.startActivity(i);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                CameraManager camManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
                String cameraId1 = null;
                String cameraId0 = null;
                try {
                    cameraId0 = camManager.getCameraIdList()[0];
                    camManager.setTorchMode(cameraId0, true);
                    Toast.makeText(getApplicationContext(), "FlashLight Started", Toast.LENGTH_SHORT).show();
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        else {
            requestMultiplePermissions();
            finish();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            CameraManager camManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
            String cameraId0 = null;
            try {
                cameraId0 = camManager.getCameraIdList()[0];
                camManager.setTorchMode(cameraId0, false);
                Toast.makeText(getApplicationContext(), "FlashLight Stopped", Toast.LENGTH_SHORT).show();
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
        finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean arePermissionsEnabled(){
        for(String permission : permissions){
            if(checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED)
                return false;
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestMultiplePermissions(){
        List<String> remainingPermissions = new ArrayList<>();
        for (String permission : permissions) {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                remainingPermissions.add(permission);
            }
        }
        requestPermissions(remainingPermissions.toArray(new String[remainingPermissions.size()]), 101);
    }
}
