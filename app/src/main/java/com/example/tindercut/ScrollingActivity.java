package com.example.tindercut;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tindercut.data.model.HairdresserDetailBody;
import com.example.tindercut.data.model.HairdresserImagesDetails;
import com.example.tindercut.data.model.SearchImageData;
import com.example.tindercut.data.model.SearchImageResponse;
import com.example.tindercut.databinding.ActivityScrollingBinding;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ScrollingActivity extends AppCompatActivity {

    private final int PAGE_SIZE = 5;
    boolean isLoading = false;
    private ActivityScrollingBinding binding;
    private ImageView hairdresserIcon;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v("DEV", "created");
        super.onCreate(savedInstanceState);

        binding = ActivityScrollingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = binding.toolbarLayout;
        toolBarLayout.setTitle(getTitle());

        //Получаем данные
        Bundle bundle = getIntent().getExtras();
        ArrayList<SearchImageData> dataArrayList = (ArrayList<SearchImageData>) getIntent().getSerializableExtra("dataArray");
        ArrayList<ArrayList<String>> imageUrlsArray = new ArrayList<>();
        ArrayList<String> hairdressers = new ArrayList<>();
        HashMap<String, ArrayList<String>> extras = new HashMap<>();
        Log.v("DEV", "created");


        //Заполняем из данных
        for (int i = 0; i < dataArrayList.size(); i++) {

            HairdresserDetailBody hairdresser = dataArrayList.get(i).getHairdresser();
            ArrayList<HairdresserImagesDetails> hairImages = dataArrayList.get(i).getImages();
            String name = hairdresser.getName();
            String hairdresserIcon = hairdresser.getPic();

            hairdressers.add(name);

            ArrayList<String> extrasArr = new ArrayList<>();
            extrasArr.add(hairdresserIcon); // 1 параметр - Икнока

            extras.put(name, extrasArr);

            ArrayList<String> imageUrls = new ArrayList<>();

            for (int j = 0; j < hairImages.size(); j++) {
                HairdresserImagesDetails hairdresserImagesDetails = hairImages.get(j);
                String imageUrl = hairdresserImagesDetails.getImg_path();
                imageUrls.add(imageUrl);
            }

            imageUrlsArray.add(imageUrls);


        }

        System.out.println(hairdressers);

        //Получаем scrollView страницы
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        DataAdapter adapter = new DataAdapter(getApplicationContext(), imageUrlsArray, hairdressers, extras);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);


    }


}