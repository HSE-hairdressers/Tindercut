package com.example.tindercut;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;

import com.example.tindercut.data.User;
import com.example.tindercut.ui.hairdresser.HairdresserFragment;
import com.example.tindercut.ui.home.HomeFragment;
import com.example.tindercut.ui.login.LoginActivity;
import com.example.tindercut.ui.photosearch.SearchPhotoFragment;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

/**
 * Main activity, which holds all fragments with app main functional
 * Also used for navigation working
 */
public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private final Fragment fragment1 = new SearchPhotoFragment();
    private final Fragment fragment2 = new HomeFragment();
    private final Fragment fragment3 = new HairdresserFragment();
    private final FragmentManager fm = getSupportFragmentManager();
    private Fragment active;

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
        setContentView(R.layout.activity_main);

        fm.beginTransaction().add(R.id.main_container, fragment3, "3").hide(fragment3).commit();
        fm.beginTransaction().add(R.id.main_container, fragment2, "2").hide(fragment2).commit();
        fm.beginTransaction().add(R.id.main_container ,fragment1, "1").commit();

        active = fragment1;

        bottomNavigationView = findViewById(R.id.bottom_navigator);
        bottomNavigationView.setSelectedItemId(R.id.navigation_searchphoto);

        /**
         * Listener of navigation between fragments
         */
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            /**
             *
             * @param item Selected item
             * @return Returns if item is selected
             */
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_searchphoto:
                        fm.beginTransaction().hide(active).show(fragment1).commit();
                        active = fragment1;
                        return true;
                    case R.id.navigation_profile:

                        if (!User.isLoggedIn(getApplicationContext()) && !LoggedInWithGoogleAuth()) {
                            openLoginActivity();
                        }

                        fm.beginTransaction().hide(active).show(fragment3).commit();
                        active = fragment3;
                        return true;
                    case R.id.navigation_home:
                        fm.beginTransaction().hide(active).show(fragment2).commit();
                        active = fragment2;
                        return true;
                }
                return false;
            }
        });

    }

    /**
     * Opens LoginActivity if needed
     */
    private void openLoginActivity() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Checks if user is logged in with Google
     * @return Returns boolean if user is logged in
     */
    private Boolean LoggedInWithGoogleAuth() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        return account != null;
    }
}