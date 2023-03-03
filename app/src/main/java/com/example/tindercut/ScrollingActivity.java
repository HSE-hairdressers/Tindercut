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

        //Получаем scrollView страницы
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.linearlayout);
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
                    ImageView imageView = new ImageView(getApplicationContext());
                    new DownloadImageTask(imageView).execute(imageUrl);
                    linearLayout.addView(imageView);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

}