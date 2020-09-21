package com.example.finalproject2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.finalproject2.Helper.ReminderHelper;
import com.example.finalproject2.Model.AppData;
import com.example.finalproject2.Model.Plant;
import com.example.finalproject2.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;

public class LoginActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private EditText email, password;
    private Button login, signup;
    private boolean isNeedToRewrite = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mRequestPermission();
        initComponent();
    }

    private void mRequestPermission() {
        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {
                Manifest.permission.READ_CALENDAR,
                Manifest.permission.WRITE_CALENDAR,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.VIBRATE,
                Manifest.permission.INTERNET
        };

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
    }

    private void initComponent() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.username);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                logIn();
            }
        });
        signup = findViewById(R.id.signup);
        signup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                signUp();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check auth on Activity start
        if (mAuth.getCurrentUser() != null) {
            isNeedToRewrite = false;
            onAuthSuccess(mAuth.getCurrentUser());
        }
    }

    private void onAuthSuccess(FirebaseUser user) {
        String username = usernameFromEmail(user.getEmail());
        // Write new user
        writeNewUser(user.getUid(), username, user.getEmail());
        // Go to MainActivity
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }

    private void logIn() {
        Log.d("Tag", "signIn");

        String _email = email.getText().toString();
        String _password = password.getText().toString();

        mAuth.signInWithEmailAndPassword(_email, _password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            getWarningDialog().show();
                        } else {
                            Toast.makeText(LoginActivity.this, "Sign In Failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void signUp() {
        String _email = email.getText().toString();
        String _password = password.getText().toString();

        mAuth.createUserWithEmailAndPassword(_email, _password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            onAuthSuccess(task.getResult().getUser());
                        } else {
                            Toast.makeText(LoginActivity.this, "Sign Up Failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }

    private void writeNewUser(String userId, String name, String email) {
        User user = new User(name, email);
        AppData.user = user;
        if (isNeedToRewrite)
            mDatabase.child("users").child(userId).setValue(user);
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private AlertDialog.Builder getWarningDialog() {
        return new AlertDialog.Builder(this)
                .setTitle("Sync reminder")
                .setMessage("Do you want to sync your old watering schedule?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (AppData.user != null)
                            for (Plant p : AppData.user.userPlants) {
                                ReminderHelper.setReminder(LoginActivity.this, p);
                            }
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert);
    }
}