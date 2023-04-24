package com.example.tindercut.ui.hairdresser;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.tindercut.R;
import com.example.tindercut.data.User;
import com.example.tindercut.ui.login.LoginActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class HairdresserFragment extends Fragment {

    String iconUrl;
    String name;

    EditText hairdresserName;
    TextView hairdresserDescription;

    ImageView hairdresserIcon, hairdresserEdit;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_hairdresser,
                container, false);

        hairdresserName = (EditText) view.findViewById(R.id.hairdresserName);
        hairdresserDescription = view.findViewById(R.id.hairdresserDescription);
        hairdresserIcon = view.findViewById(R.id.hairdresserPhoto);
        hairdresserEdit = view.findViewById(R.id.editProfileIcon);

        Bundle extras = getActivity().getIntent().getExtras();
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

        Log.v("DEV", "Fragment created");
        return view;
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
