package com.example.tindercut;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HairdresserActivity extends AppCompatActivity {

    String iconUrl;
    String name;

    EditText hairdresserName;
    TextView hairdresserDescription;

    ImageView hairdresserIcon, hairdresserEdit;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hairdresser);

        bottomNavigationView = findViewById(R.id.bottom_navigator);
        bottomNavigationView.setSelectedItemId(R.id.navigation_profile);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.navigation_searchphoto:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.navigation_profile:
                        return true;
                    case R.id.navigation_home:
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }

        });

        hairdresserName = (EditText) findViewById(R.id.hairdresserName);
        hairdresserDescription = findViewById(R.id.hairdresserDescription);
        hairdresserIcon = findViewById(R.id.hairdresserPhoto);
        hairdresserEdit = findViewById(R.id.editProfileIcon);

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            name = extras.getString("name");
            iconUrl = extras.getString("icon");

            Log.v("DEV", name);
            Log.v("DEV", iconUrl);

            hairdresserName.setText(name);
            hairdresserName.setTag(hairdresserName.getKeyListener());
            hairdresserName.setKeyListener(null);

            hairdresserDescription.setText("Some description here...");
            Glide.with(this).load(iconUrl).into(hairdresserIcon);
        }

        hairdresserEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleText(hairdresserName);
            }
        });

        Log.v("DEV", "Activity created");
    }


    private void toggleText(EditText text){
        if (text.getKeyListener() != null){
            text.setTag(text.getKeyListener());
            text.setKeyListener(null);
            text.clearFocus();
            hairdresserEdit.setImageResource(android.R.drawable.ic_menu_edit);
        } else {
            text.setKeyListener((KeyListener) text.getTag());
            text.requestFocus();
            text.setSelection(text.getText().length());
            hairdresserEdit.setImageResource(android.R.drawable.ic_menu_save);
        }
    }

}