package com.example.tindercut.ui.hairdresser;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.tindercut.R;
import com.example.tindercut.data.Constants;
import com.example.tindercut.data.User;
import com.example.tindercut.ui.settings.SettingsActivity;
import com.hbisoft.pickit.PickiT;
import com.hbisoft.pickit.PickiTCallbacks;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class HairdresserFragment extends Fragment implements PickiTCallbacks {

    private final int pickedImage = 1;

    PickiT pickIt;
    private String imagePath;
    private Bitmap imageBitmap;
    String iconUrl;
    String name;

    EditText hairdresserName;
    TextView hairdresserDescription;
    Button buttonUploadPhoto;

    ImageView hairdresserIcon, hairdresserEdit;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile,
                container, false);

        pickIt = new PickiT(getContext(), this, getActivity());

        hairdresserName = (EditText) view.findViewById(R.id.hairdresserName);
        hairdresserDescription = view.findViewById(R.id.hairdresserDescription);
        hairdresserIcon = view.findViewById(R.id.hairdresserPhoto);
        hairdresserEdit = view.findViewById(R.id.settings);
        buttonUploadPhoto = view.findViewById(R.id.buttonUploadPhoto);

        Log.v("DEV", User.getName(getContext()));
        hairdresserName.setText(User.getName(getContext()));

        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null){
            name = extras.getString("name");
            iconUrl = extras.getString("icon");

            Log.v("DEV", name);
            Log.v("DEV", iconUrl);

            hairdresserName.setText(name);
            hairdresserName.setTag(hairdresserName.getKeyListener());
            hairdresserName.setKeyListener(null);

            hairdresserDescription.setText("Some description here...");
            Glide.with(this).load(iconUrl).into(hairdresserIcon);
        }

        hairdresserEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                toggleText(hairdresserName);
                openSettingsActivity();
            }
        });

        buttonUploadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK);
                pickPhoto.setType("image/*");
                pickPhoto.setAction(Intent.ACTION_GET_CONTENT);
                checkPermission();
                startActivityForResult(pickPhoto, pickedImage);
            }
        });

        Log.v("DEV", "Fragment created");
        return view;
    }

    private void openSettingsActivity() {
        Intent intent = new Intent(getContext(), SettingsActivity.class);
        startActivity(intent);
    }

    private void getHairdresserInfo(Long id, Context context) {
        // url to post our data
        String url = Constants.getProfileURL();
        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(context);
        JSONObject object = new JSONObject();
        try {
            //input your API parameters
            object.put("id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            iconUrl = response.getString("pic");
                            Glide.with(getActivity()).load(iconUrl).into(hairdresserIcon);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsonObjectRequest);
    }

    private void toggleText(EditText text){
        if (text.getKeyListener() != null){
            text.setTag(text.getKeyListener());
            text.setKeyListener(null);
            text.clearFocus();
            hairdresserEdit.setImageResource(android.R.drawable.ic_menu_edit);
        } else {
            text.setKeyListener((KeyListener) text.getTag());
            text.requestFocus();
            text.setSelection(text.getText().length());
            hairdresserEdit.setImageResource(android.R.drawable.ic_menu_save);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case pickedImage:
                //Получаем URI картинки:
                if (data == null) {
                    return;
                }
                Uri imageUri = data.getData();
                pickIt.getPath(imageUri, Build.VERSION.SDK_INT);

                try {
                    imageBitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), imageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (imageBitmap != null) {
                    sendImage();
                }
        }


    }

    public void sendImage() {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = "http://79.137.206.63:8011/hairdresser/upload";

        SimpleMultiPartRequest uploadRequest = new SimpleMultiPartRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject responseJSON = new JSONObject(response);

                            //Выполняем проверку успеха отправки изображения
                            String requestResult = responseJSON.getString("result");
                            if (requestResult.equals("Ok")) {
                                Toast.makeText(getContext(), "Изображение успешно загружено.", Toast.LENGTH_SHORT).show();
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
        String fileName = "photo";

        Log.v("REQ", User.getID(getContext()).toString());
        uploadRequest.addStringParam("id", User.getID(getContext()).toString());
        uploadRequest.addFile(fileName, imagePath);
        //uploadRequest.addMultipartParam("body", "text/plain", base64Image);

        queue.add(uploadRequest);
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
}
