package com.example.tindercut;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tindercut.adapters.DataAdapter;
import com.example.tindercut.data.model.HairdresserDetailBody;
import com.example.tindercut.data.model.HairdresserImagesDetails;
import com.example.tindercut.data.model.SearchImageData;
import com.example.tindercut.databinding.ActivityScrollingBinding;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Activity for scrollview of founded hairdressers
 * Creates list of them
 */
public class ScrollingActivity extends AppCompatActivity {

    private final int PAGE_SIZE = 5;
    boolean isLoading = false;
    private ActivityScrollingBinding binding;
    private ImageView hairdresserIcon;

    /**
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
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
        ArrayList<String> userId = new ArrayList<>();
        HashMap<String, ArrayList<String>> extras = new HashMap<>();
        Log.v("DEV", "created");

        /**
         * Data parsing
         */
        for (int i = 0; i < dataArrayList.size(); i++) {

            HairdresserDetailBody hairdresser = dataArrayList.get(i).getHairdresser();
            ArrayList<HairdresserImagesDetails> hairImages = dataArrayList.get(i).getImages();
            String name = hairdresser.getName();
            String hairdresserIcon = hairdresser.getPic();
            String id = hairdresser.getId();

            hairdressers.add(name);
            userId.add(id);

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
        DataAdapter adapter = new DataAdapter(getApplicationContext(), imageUrlsArray, hairdressers, userId, extras);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);


    }


}