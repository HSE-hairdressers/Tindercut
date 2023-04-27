package com.example.tindercut.ui.settings;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tindercut.MainActivity;
import com.example.tindercut.R;
import com.example.tindercut.data.User;
import com.example.tindercut.ui.editprofile.EditProfileActivity;

public class SettingsActivity extends AppCompatActivity {
    Button buttonEditProfile;
    Button buttonSignOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        buttonEditProfile = findViewById(R.id.editProfileButton);
        buttonSignOut = findViewById(R.id.logoutButton);

        buttonEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEditProfileActivity();
            }
        });

        buttonSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User.logout(getApplicationContext());
                Toast.makeText(getApplicationContext(), "You are logged out", Toast.LENGTH_LONG).show();
                openMainActivity();
                setResult(Activity.RESULT_OK);
                finish();
            }
        });
        Log.v("DEV", "Activity created");
    }

    private void openEditProfileActivity() {
        Intent intent = new Intent(this, EditProfileActivity.class);
        startActivity(intent);
    }

    private void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
