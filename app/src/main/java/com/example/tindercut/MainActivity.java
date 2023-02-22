package com.example.tindercut;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Bitmap ImageBitmap;
    private Button button;
    private ImageView ImageView;
    private final int picked_image = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Привязка обработчика события к кнопке
        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK);
                pickPhoto.setType("image/*");
                pickPhoto.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(pickPhoto, picked_image);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case picked_image:
                //Получаем URI картинки:
                Uri imageUri = data.getData();
                //Находим нужный ImageView в интерфейсе
                ImageView = findViewById(R.id.imageView);

                try {
                    ImageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                ByteArrayOutputStream byteArrayOutputStream;
                byteArrayOutputStream = new ByteArrayOutputStream();

                if(ImageBitmap != null){
                    ImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                    byte [] bytes = byteArrayOutputStream.toByteArray();
                    final String base64Image  = Base64.encodeToString(bytes, Base64.DEFAULT);
                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                    String url ="http://77.91.78.24:8011/img";
                    System.out.println("Here");

                    SimpleMultiPartRequest uploadRequest = new SimpleMultiPartRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if(response.equals("Ok")){
                                        Toast.makeText(getApplicationContext(), "Успешно загружено!", Toast.LENGTH_SHORT).show();
                                    }
                                    else Toast.makeText(getApplicationContext(), "Ошибка загрузки!", Toast.LENGTH_SHORT).show();
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        }});
                    String fileName = Calendar.getInstance().getTime().toString()+" Photo";
                    System.out.println("Путь: "+ imageUri.getPath());
                    uploadRequest.addFile(fileName.replace(" ", ""), imageUri.getPath());
                    //uploadRequest.addMultipartParam("body", "text/plain", "some text");

                    queue.add(uploadRequest);
                    //queue.start();


                    /*StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if(response.equals("Ok")){
                                        System.out.println("Ok");
                                    }
                                    else System.out.println("Not ok");
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        }
                    }){
                        protected Map<String, String> getParams(){
                            Map<String, String> paramV = new HashMap<>();
                            paramV.put("image", base64Image);
                            return paramV;
                        }
                    };
                    queue.add(stringRequest);*/


                }

                // * ТУТ НУЖНО ЧТО-ТО СДЕЛАТЬ НА СЕРВЕРЕ *

                //Отображаем картинку по URI
                ImageView.setImageURI(imageUri);

        }

    }
}