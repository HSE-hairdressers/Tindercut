package com.example.tindercut;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.method.KeyListener;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.tindercut.data.Constants;
import com.example.tindercut.data.User;
import com.example.tindercut.ui.login.LoginActivity;
import com.example.tindercut.utils.PermissionChecker;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hbisoft.pickit.PickiT;
import com.hbisoft.pickit.PickiTCallbacks;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class HairdresserActivity extends AppCompatActivity implements PickiTCallbacks {

    private final int picked_image = 1;
    PickiT pickIt;
    private Bitmap imageBitmap;
    String iconUrl;
    String name;
    EditText hairdresserName;
    TextView hairdresserDescription;

    ImageView hairdresserIcon, hairdresserEdit;
    Button buttonUploadPhoto;
    private BottomNavigationView bottomNavigationView;
    private String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if (!User.isLoggedIn(getApplicationContext()) && !LoggedInWithGoogleAuth()) {
            openLoginActivity();
        }

        setContentView(R.layout.fragment_hairdresser);

        getHairdresserInfo(User.getID(getApplicationContext()), getApplicationContext());

        hairdresserName = (EditText) findViewById(R.id.hairdresserName);
        hairdresserDescription = findViewById(R.id.hairdresserDescription);
        hairdresserIcon = findViewById(R.id.hairdresserPhoto);
        hairdresserEdit = findViewById(R.id.editProfileIcon);

        Log.v("DEV", User.getName(getApplicationContext()));
        hairdresserName.setText(User.getName(getApplicationContext()));

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            name = extras.getString("name");
            iconUrl = extras.getString("icon");

            Log.v("DEV", name);
            Log.v("DEV", iconUrl);
            hairdresserName.setTag(hairdresserName.getKeyListener());
            hairdresserName.setKeyListener(null);

            hairdresserDescription.setText("Some description here...");
            Glide.with(this).load(iconUrl).into(hairdresserIcon);
        }

        hairdresserEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleText(hairdresserName);
            }
        });

        buttonUploadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK);
                pickPhoto.setType("image/*");
                pickPhoto.setAction(Intent.ACTION_GET_CONTENT);
                PermissionChecker.checkPermission(HairdresserActivity.this, getApplicationContext());
                startActivityForResult(pickPhoto, picked_image);
            }
        });

        Log.v("DEV", "Activity created");
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
                            Glide.with(HairdresserActivity.this).load(iconUrl).into(hairdresserIcon);
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

    private void openLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
    private Boolean LoggedInWithGoogleAuth() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        return account != null;
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case picked_image:
                //Получаем URI картинки:
                if (data == null) {
                    return;
                }
                Uri imageUri = data.getData();
                pickIt.getPath(imageUri, Build.VERSION.SDK_INT);

                try {
                    imageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                ByteArrayOutputStream byteArrayOutputStream;
                byteArrayOutputStream = new ByteArrayOutputStream();

                if (imageBitmap != null) {
                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                    byte[] bytes = byteArrayOutputStream.toByteArray();
                    final String base64Image = Base64.encodeToString(bytes, Base64.DEFAULT);
                    sendImage();
                }

        }


    }

    public void sendImage() {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = "http://79.137.206.63:8011/hairdresser/image";

        SimpleMultiPartRequest uploadRequest = new SimpleMultiPartRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject responseJSON = new JSONObject(response);

                            //Выполняем проверку успеха отправки изображения
                            String requestResult = responseJSON.getString("result");
                            if (requestResult.equals("Ok")) {
                                Toast.makeText(getApplicationContext(), "Изображение успешно загружено.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), requestResult, Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
        String fileName = "photo";

        uploadRequest.addStringParam("id", User.getID(getApplicationContext()).toString());
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
}