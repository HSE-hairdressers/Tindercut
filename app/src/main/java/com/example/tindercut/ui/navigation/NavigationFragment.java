package com.example.tindercut.ui.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.tindercut.HairdresserActivity;
import com.example.tindercut.HomeActivity;
import com.example.tindercut.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class NavigationFragment extends Fragment implements View.OnClickListener {

    BottomNavigationView bottomNavigationView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_navigation,
                container, false);

        bottomNavigationView = view.findViewById(R.id.bottom_navigator);
        bottomNavigationView.setSelectedItemId(R.id.navigation_searchphoto);

        return view;
    }


    @Override
    public void onClick(View v) {

    }
}
