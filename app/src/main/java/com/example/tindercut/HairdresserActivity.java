package com.example.tindercut;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tindercut.adapters.DataAdapter;
import com.example.tindercut.adapters.GalleryAdapter;
import com.example.tindercut.data.Constants;
import com.example.tindercut.data.User;
import com.example.tindercut.data.api.GetProfileInfoApi;
import com.example.tindercut.data.api.GetUserImagesApi;
import com.example.tindercut.data.api.UploadImageParamApi;
import com.example.tindercut.data.model.GetProfileInfoResponse;
import com.example.tindercut.data.model.UserImageDetails;
import com.example.tindercut.data.model.UserImagesResponse;
import com.example.tindercut.ui.login.LoginActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Activity for watching hairdresser profile without using fragment
 */
public class HairdresserActivity extends AppCompatActivity {

    String iconUrl;
    String name;

    EditText hairdresserName;
    TextView hairdresserDescription;

    ArrayList<String> images;
    String userId;

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
            images = new ArrayList<>();
            userId = extras.getString("id");

            hairdresserName.setText(name);
            hairdresserName.setTag(hairdresserName.getKeyListener());
            hairdresserName.setKeyListener(null);

            Glide.with(this).load(iconUrl).into(hairdresserIcon);

            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.host)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            GetUserImagesApi retrofitApi = retrofit.create(GetUserImagesApi.class);
            Call<UserImagesResponse> call = retrofitApi.getProfileImages(userId);

            call.enqueue(new Callback<UserImagesResponse>() {
                @Override
                public void onResponse(Call<UserImagesResponse> call, Response<UserImagesResponse> response) {
                    if (response.isSuccessful()){
                        ArrayList<UserImageDetails> imagesObject = response.body().getImages();
                        for (int i = 0; i < imagesObject.size(); ++i){
                            Log.v("DEV", imagesObject.get(i).getImg());
                            images.add(imagesObject.get(i).getImg());
                        }
                        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.imagesGallery);
                        GalleryAdapter adapter = new GalleryAdapter(getApplicationContext(), images);

                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        recyclerView.setAdapter(adapter);
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Fail to get response", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<UserImagesResponse> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Fail to get response = " + t.toString(), Toast.LENGTH_SHORT).show();
                }
            });

        }

    }



}