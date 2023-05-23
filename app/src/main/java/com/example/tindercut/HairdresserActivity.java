package com.example.tindercut;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tindercut.adapters.DataAdapter;
import com.example.tindercut.adapters.GalleryAdapter;
import com.example.tindercut.ui.login.LoginActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

/**
 * Activity for watching hairdresser profile without using fragment
 */
public class HairdresserActivity extends AppCompatActivity {

    String iconUrl;
    String name;

    EditText hairdresserName;
    TextView hairdresserDescription;

    ArrayList<String> images;

    ImageView hairdresserIcon;

    /**
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_hairdresser);

        hairdresserName = (EditText) findViewById(R.id.hairdresserName);
        hairdresserIcon = findViewById(R.id.hairdresserPhoto);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            name = extras.getString("name");
            iconUrl = extras.getString("icon");
            images = extras.getStringArrayList("images");

            hairdresserName.setText(name);
            hairdresserName.setTag(hairdresserName.getKeyListener());
            hairdresserName.setKeyListener(null);

            Glide.with(this).load(iconUrl).into(hairdresserIcon);

            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.imagesGallery);
            GalleryAdapter adapter = new GalleryAdapter(getApplicationContext(), images);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(adapter);

        }

    }



}