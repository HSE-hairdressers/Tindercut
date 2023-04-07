package com.example.tindercut;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class HairdresserActivity extends AppCompatActivity {

    String iconUrl;
    String name;

    TextView hairdresserName, hairdresserDescription;

    ImageView hairdresserIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hairdresser);

        hairdresserName = findViewById(R.id.hairdresserName);
        hairdresserDescription = findViewById(R.id.hairdresserDescription);
        hairdresserIcon = findViewById(R.id.hairdresserPhoto);

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            name = extras.getString("name");
            iconUrl = extras.getString("icon");

            Log.v("DEV", name);
            Log.v("DEV", iconUrl);

            hairdresserName.setText(name);
            hairdresserDescription.setText("Some description here...");
            Glide.with(this).load(iconUrl).into(hairdresserIcon);
        }

        Log.v("DEV", "Activity created");
    }

    private void lockText(EditText text){
        text.setFocusable(false);
        text.setLongClickable(false);
        text.setCursorVisible(false);
    }

    private void unlockText(EditText text){
        text.setFocusable(true);
        text.setLongClickable(true);
        text.setCursorVisible(true);
    }

}
