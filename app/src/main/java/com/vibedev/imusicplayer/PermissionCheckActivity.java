package com.vibedev.imusicplayer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class PermissionCheckActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.white));
        int nightModeFlag = getResources().getConfiguration().uiMode & android.content.res.Configuration.UI_MODE_NIGHT_MASK;
        if (nightModeFlag == android.content.res.Configuration.UI_MODE_NIGHT_YES){
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.dark_toolbar));
        }

        checkPermissionAndProceed();
    }

    private void checkPermissionAndProceed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_AUDIO)
                    == PackageManager.PERMISSION_GRANTED) {
                goToMain();
            } else {
                requestPermission();
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                goToMain();
            } else {
                requestPermission();
            }
        }
    }

    private void requestPermission() {
        String permission = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
                ? Manifest.permission.READ_MEDIA_AUDIO
                : Manifest.permission.READ_EXTERNAL_STORAGE;

        Dexter.withContext(this)
                .withPermission(permission)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        Toast.makeText(PermissionCheckActivity.this, "Permission granted!", Toast.LENGTH_LONG).show();
                        goToMain();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if (!response.isPermanentlyDenied()) {
                            requestPermission();
                        } else {
                            Toast.makeText(PermissionCheckActivity.this, "Enable permission from settings.", Toast.LENGTH_LONG).show();
                            openAppSettings();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        new AlertDialog.Builder(PermissionCheckActivity.this)
                                .setMessage("App needs media access to proceed.")
                                .setPositiveButton("Allow", (dialog, which) -> token.continuePermissionRequest())
                                .setNegativeButton("Deny", (dialog, which) -> token.cancelPermissionRequest())
                                .show();
                    }
                }).check();
    }

    private void goToMain() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void openAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }
}
