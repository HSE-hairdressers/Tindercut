package com.example.tindercut.ui.photosearch;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.example.tindercut.DataSerializable;
import com.example.tindercut.MainActivity;
import com.example.tindercut.R;
import com.example.tindercut.ScrollingActivity;
import com.hbisoft.pickit.PickiT;
import com.hbisoft.pickit.PickiTCallbacks;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

public class SearchPhotoFragment extends Fragment implements PickiTCallbacks {

    private final int picked_image = 1;
    PickiT pickIt;
    private Bitmap ImageBitmap;
    private Button button;
    private Button button2;
    private android.widget.ImageView ImageView;
    private String imagePath;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_searchphoto,
                container, false);

        pickIt = new PickiT(getContext(), this, getActivity());

        //Привязка обработчика события к кнопке
        button = view.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK);
                pickPhoto.setType("image/*");
                pickPhoto.setAction(Intent.ACTION_GET_CONTENT);
                checkPermission();
                startActivityForResult(pickPhoto, picked_image);
            }
        });

        button2 = view.findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ImageBitmap != null) {
                    sendImage();
                }
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case picked_image:
                //Получаем URI картинки:
                if (data == null) {
                    return;
                }
                Uri imageUri = data.getData();
                pickIt.getPath(imageUri, Build.VERSION.SDK_INT);
                //Находим нужный ImageView в интерфейсе
                ImageView = view.findViewById(R.id.imageView);

                try {
                    ImageBitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), imageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                ByteArrayOutputStream byteArrayOutputStream;
                byteArrayOutputStream = new ByteArrayOutputStream();

                if (ImageBitmap != null) {
                    ImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                    byte[] bytes = byteArrayOutputStream.toByteArray();
                    final String base64Image = Base64.encodeToString(bytes, Base64.DEFAULT);
                    ImageView.setImageBitmap(ImageBitmap);
                }

        }


    }

    public void sendImage() {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = "http://79.137.206.63:8011/img";

        SimpleMultiPartRequest uploadRequest = new SimpleMultiPartRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject responseJSON = new JSONObject(response);
                            JSONArray hairData = responseJSON.getJSONArray("data");

                            //Выполняем проверку успеха отправки изображения
                            String requestResult = responseJSON.getString("result");
                            if (requestResult.equals("Ok")) {
                                Toast.makeText(getContext(), "Изображение успешно загружено.", Toast.LENGTH_SHORT).show();

                                Intent scrollingIntent = new Intent(getContext(), ScrollingActivity.class);
                                ArrayList<DataSerializable> arrayToSend = new ArrayList<>();
                                for (int i = 0; i < hairData.length(); i++) {
                                    arrayToSend.add(new DataSerializable(hairData.get(i).toString()));
                                }

                                Bundle bundle = new Bundle();
                                bundle.putSerializable("dataArray", arrayToSend);
                                scrollingIntent.putExtras(bundle);
                                try {
                                    startActivity(scrollingIntent);
                                } catch (Exception e) {
                                    e.getStackTrace();
                                }

                            } else {
                                Toast.makeText(getContext(), requestResult, Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
        String fileName = Calendar.getInstance().getTime() + " Photo";
        //System.out.println("Путь: "+ imageUri.getPath());
        Log.v("DEV", imagePath);
        uploadRequest.addFile(fileName, imagePath);
        //uploadRequest.addMultipartParam("body", "text/plain", base64Image);

        queue.add(uploadRequest);
    }

    public void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);

            }

            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_NETWORK_STATE}, 101);

            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                try {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                    intent.addCategory("android.intent.category.DEFAULT");
                    intent.setData(Uri.parse(String.format("package:%s", getContext().getPackageName())));
                } catch (Exception e) {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);

                }
            }
        }
    }

    @Override
    public void PickiTonUriReturned() {

    }

    @Override
    public void PickiTonStartListener() {

    }

    @Override
    public void PickiTonProgressUpdate(int progress) {

    }


    @Override
    public void PickiTonCompleteListener(String path, boolean wasDriveFile, boolean wasUnknownProvider, boolean wasSuccessful, String Reason) {

        imagePath = path;

    }

    @Override
    public void PickiTonMultipleCompleteListener(ArrayList<String> paths, boolean wasSuccessful, String Reason) {

    }
}