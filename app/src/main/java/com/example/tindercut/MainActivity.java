package com.example.tindercut;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

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

                // * ТУТ НУЖНО ЧТО-ТО СДЕЛАТЬ НА СЕРВЕРЕ *

                //Отображаем картинку по URI
                ImageView.setImageURI(imageUri);

        }

    }
}