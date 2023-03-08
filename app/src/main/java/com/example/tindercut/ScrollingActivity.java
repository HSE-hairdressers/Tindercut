package com.example.tindercut;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;

import com.android.volley.cache.DiskLruBasedCache;
import com.android.volley.cache.plus.SimpleImageLoader;
import com.android.volley.misc.AsyncTask;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.tindercut.databinding.ActivityScrollingBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class ScrollingActivity extends AppCompatActivity {

    private ActivityScrollingBinding binding;
    private JSONArray hairData;
    private Bitmap ImageBitmap;
    boolean isLoading = false;
    private final int PAGE_SIZE = 10;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("?");

        binding = ActivityScrollingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = binding.toolbarLayout;
        toolBarLayout.setTitle(getTitle());
        //Получаем данные
        System.out.println("Here");
        Bundle bundle = getIntent().getExtras();
        ArrayList<DataSerializable> dataArrayList = (ArrayList<DataSerializable>) bundle.getSerializable("dataArray");
        ArrayList<String> imageUrls = new ArrayList<>();
        ArrayList<String> temp = new ArrayList<>();

        //Заполняем из данных
        for(int i = 0; i < dataArrayList.size(); i++){
            try {
                JSONObject hairDataObject = new JSONObject(dataArrayList.get(i).getData());
                JSONArray hairImages = hairDataObject.getJSONArray("images");

                //Получсаем изображения
                for (int j = 0; j < hairImages.length(); j++) {
                    JSONObject hairImage = hairImages.getJSONObject(j);
                    String hairImageName = hairImage.getString("name");
                    String imageUrl = hairImage.getString("img_path");
                    imageUrls.add(imageUrl);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        if (imageUrls.size() > PAGE_SIZE){
            for (int i = 0; i < PAGE_SIZE; i++){
                temp.add(imageUrls.get(i));
            }
        }
        else {
            for (int i = 0; i < imageUrls.size(); i++){
                temp.add(imageUrls.get(i));
            }
        }

        //Получаем scrollView страницы
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        DataAdapter adapter = new DataAdapter(getApplicationContext(), imageUrls);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == temp.size() - 1) {
                        //bottom of list!
                        loadMore();
                        isLoading = true;
                    }
                }
            }

            private void loadMore() {
                temp.add(null);
                adapter.notifyItemInserted(temp.size() - 1);


                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        temp.remove(temp.size() - 1);
                        int scrollPosition = temp.size();
                        adapter.notifyItemRemoved(scrollPosition);
                        int currentSize = scrollPosition;
                        int nextLimit = currentSize + 10;

                        if (nextLimit > imageUrls.size()) {
                            nextLimit = imageUrls.size();
                        }

                        while (currentSize - 1 < nextLimit) {
                            temp.add(imageUrls.get(currentSize));
                            currentSize++;
                        }

                        adapter.notifyDataSetChanged();
                        isLoading = false;
                    }
                }, 2000);


            }
        });



    }



}