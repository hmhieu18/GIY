package com.example.finalproject2.Helper;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.finalproject2.MainActivity;

public class PermissionHelper {
    // Function to check and request permission.
    private Activity activity;

    private void checkAndRequestPermission(Activity _activity) {

    }

    public void checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(activity.getApplicationContext(), permission)
                == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission
            ActivityCompat.requestPermissions(activity,
                    new String[]{permission},
                    requestCode);
        } else {
            Toast.makeText(activity.getApplicationContext(),
                    "Permission already granted",
                    Toast.LENGTH_SHORT)
                    .show();
        }
    }
}
