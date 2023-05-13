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
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.tindercut.DataSerializable;
import com.example.tindercut.R;
import com.example.tindercut.ScrollingActivity;
import com.example.tindercut.data.Constants;
import com.example.tindercut.data.api.UploadImageApi;
import com.example.tindercut.data.model.SearchImageData;
import com.example.tindercut.data.model.SearchImageResponse;
import com.hbisoft.pickit.PickiT;
import com.hbisoft.pickit.PickiTCallbacks;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Fragment for sending a photo to server and passing data to ScrollingActivity
 */
public class SearchPhotoFragment extends Fragment implements PickiTCallbacks {

    private final int picked_image = 1;
    PickiT pickIt;
    private Bitmap ImageBitmap;
    private Button button;
    private Button button2;
    private android.widget.ImageView ImageView;
    private String imagePath;
    private View view;

    /**
     * Contains listeners for buttons on interface
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return return View
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_searchphoto,
                container, false);

        pickIt = new PickiT(getContext(), this, getActivity());

        //Привязка обработчика события к кнопке
        button = view.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            /**
             * Listener for picking an image with PickIT library
             * @param v The view that was clicked.
             */
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
            /**
             * Listener for sending an image to server
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                if (ImageBitmap != null) {
                    sendImage();
                }
            }
        });

        return view;
    }

    /**
     * Mainly used for getting an absolute path for image
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

    /**
     * Sending image on server
     * Contains retrofit2 request
     */
    public void sendImage() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.host)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UploadImageApi retrofitApi = retrofit.create(UploadImageApi.class);

        File selected_image = new File(imagePath);
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), selected_image);
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("image", selected_image.getName(), requestFile);

        Call<SearchImageResponse> call = retrofitApi.uploadImage(body);

        call.enqueue(new Callback<SearchImageResponse>() {
            /**
             * Parsing the response
             * @param call request
             * @param response response for request
             */
            @Override
            public void onResponse(Call<SearchImageResponse> call, retrofit2.Response<SearchImageResponse> response) {

                if (response.isSuccessful()){
                    Toast.makeText(getContext(), "Изображение успешно загружено!", Toast.LENGTH_SHORT).show();
                    SearchImageResponse searchImageResponse = response.body();
                    ArrayList <SearchImageData> hairData = searchImageResponse.getSearchImageData();

                    Intent scrollingIntent = new Intent(getContext(), ScrollingActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("dataArray", (Serializable) hairData);
                    scrollingIntent.putExtras(bundle);
                    Toast.makeText(getContext(), "passed", Toast.LENGTH_SHORT).show();
                    startActivity(scrollingIntent);

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
            public void onFailure(Call<SearchImageResponse> call, Throwable t) {
                Toast.makeText(getContext(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
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
}
