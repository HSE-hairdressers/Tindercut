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

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
        ArrayList<String> imageUrls = new ArrayList<String>();

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

        //Получаем scrollView страницы
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        DataAdapter adapter = new DataAdapter(getApplicationContext(), imageUrls);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

    }

}