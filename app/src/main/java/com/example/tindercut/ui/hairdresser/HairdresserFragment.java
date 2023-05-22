package com.example.tindercut.ui.hairdresser;

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
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.tindercut.R;
import com.example.tindercut.ScrollingActivity;
import com.example.tindercut.data.Constants;
import com.example.tindercut.data.User;
import com.example.tindercut.data.api.UploadImageApi;
import com.example.tindercut.data.api.UploadImageParamApi;
import com.example.tindercut.data.model.SearchImageData;
import com.example.tindercut.data.model.SearchImageResponse;
import com.example.tindercut.ui.settings.SettingsActivity;
import com.hbisoft.pickit.PickiT;
import com.hbisoft.pickit.PickiTCallbacks;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Fragment for viewing user profile
 */
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

    /**
     *
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return Returns View
     */
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
        hairdresserIcon.setImageResource(R.drawable.ic_profile);

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
            Glide.with(this).load(iconUrl).placeholder(R.drawable.ic_profile).into(hairdresserIcon);
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

    /**
     * Opens SettingActivity if needed
     */
    private void openSettingsActivity() {
        Intent intent = new Intent(getContext(), SettingsActivity.class);
        startActivity(intent);
    }

    /**
     * Changes mode of editable text
     * @param text Selected EditText
     */
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


    /**
     * Procedure for picking image and passing it to sendImage()
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode The integer result code returned by the child activity
     *                   through its setResult().
     * @param data An Intent, which can return result data to the caller
     *               (various data can be attached to Intent "extras").
     *
     */
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

    /**
     * Sending image on server
     * Contains retrofit2 request
     */
    public void sendImage() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.host)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UploadImageParamApi retrofitApi = retrofit.create(UploadImageParamApi.class);

        File selected_image = new File(imagePath);
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), selected_image);
        MultipartBody.Part imageBody =
                MultipartBody.Part.createFormData("image", selected_image.getName(), requestFile);
        RequestBody idBody = RequestBody.create(MediaType.parse("text/plain"), User.getID(getContext()).toString());

        Call<ResponseBody> call = retrofitApi.uploadImage(imageBody, idBody);

        call.enqueue(new Callback<ResponseBody>() {
            /**
             * Parsing the response
             * @param call request
             * @param response response for request
             */
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {

                if (response.isSuccessful()){
                    Toast.makeText(getContext(), "Изображение успешно загружено!", Toast.LENGTH_SHORT).show();

                }
                else {
                    Toast.makeText(getContext(), "Произошла ошибка!", Toast.LENGTH_SHORT).show();
                }

            }

            /**
             * Request fail handler
             * @param call request
             * @param t error on response
             */
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), t.toString(), Toast.LENGTH_SHORT).show();
                Log.v("DEV", t.toString());
            }
        });
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

    /**
     * An important procedure for selecting absolute path
     * @param path
     * @param wasDriveFile
     * @param wasUnknownProvider
     * @param wasSuccessful
     * @param Reason
     */
    @Override
    public void PickiTonCompleteListener(String path, boolean wasDriveFile, boolean wasUnknownProvider, boolean wasSuccessful, String Reason) {
        imagePath = path;
    }

    @Override
    public void PickiTonMultipleCompleteListener(ArrayList<String> paths, boolean wasSuccessful, String Reason) {

    }

    /**
     * Checks persmissions for uploading an image, requires if needed
     */
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
