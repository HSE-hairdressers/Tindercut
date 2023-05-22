package com.example.tindercut.ui.editprofile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.tindercut.MainActivity;
import com.example.tindercut.R;
import com.example.tindercut.data.Constants;
import com.example.tindercut.data.User;
import com.example.tindercut.data.api.GetProfileInfoApi;
import com.example.tindercut.data.api.RegistrationApi;
import com.example.tindercut.data.api.RegistrationResponse;
import com.example.tindercut.data.api.SubmitProfileApi;
import com.example.tindercut.data.api.UploadImageParamApi;
import com.example.tindercut.data.model.GetProfileInfoResponse;
import com.example.tindercut.utils.PermissionChecker;
import com.hbisoft.pickit.PickiT;
import com.hbisoft.pickit.PickiTCallbacks;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class EditProfileActivity extends AppCompatActivity implements PickiTCallbacks {
    PickiT pickIt;
    private Bitmap imageBitmap;
    private String imagePath;
    private final int pickedImage = 1;
    ImageView icon;
    EditText nameEditText;
    EditText phoneEditText;
    EditText companyEditText;
    EditText addressEditText;
    Button submitButton;
    String name;
    String phone;
    String company;
    String address;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_profile);

        pickIt = new PickiT(this, this, this);
        HashMap<String, String> editInfo = new HashMap<>();

        icon = findViewById(R.id.hairdresserPhoto);
        nameEditText = findViewById(R.id.name);
        phoneEditText = findViewById(R.id.phoneNumber);
        companyEditText = findViewById(R.id.company);
        addressEditText = findViewById(R.id.address);
        submitButton = findViewById(R.id.submitButton);

        getHairdresserInfo(getApplicationContext(), User.getID(getApplicationContext()));

        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK);
                pickPhoto.setType("image/*");
                pickPhoto.setAction(Intent.ACTION_GET_CONTENT);
                PermissionChecker.checkPermission(EditProfileActivity.this, getApplicationContext());
                startActivityForResult(pickPhoto, pickedImage);
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                submitChanges(editInfo, getApplicationContext());
            }
        });
        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                editInfo.put("name", nameEditText.getText().toString());
                editInfo.put("num", phoneEditText.getText().toString());
                editInfo.put("addr", addressEditText.getText().toString());
                editInfo.put("company", companyEditText.getText().toString());


//                editViewModel.editDataChanged(editInfo);
            }
        };

        nameEditText.addTextChangedListener(afterTextChangedListener);
        phoneEditText.addTextChangedListener(afterTextChangedListener);
        addressEditText.addTextChangedListener(afterTextChangedListener);
        companyEditText.addTextChangedListener(afterTextChangedListener);
    }

    private void getHairdresserInfo(Context context, Long id) {

        String userId = Long.toString(id);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.host)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GetProfileInfoApi retrofitApi = retrofit.create(GetProfileInfoApi.class);
        Call<GetProfileInfoResponse> call = retrofitApi.getProfileInfo(userId);

        call.enqueue(new Callback<GetProfileInfoResponse>() {
            @Override
            public void onResponse(Call<GetProfileInfoResponse> call, retrofit2.Response<GetProfileInfoResponse> response) {
                if (response.isSuccessful()){
                    String iconUrl = response.body().getPic();
                    Glide.with(EditProfileActivity.this).load(iconUrl).placeholder(R.drawable.ic_profile).into(icon);
                    name = response.body().getName();
                    nameEditText.setText(name);
                    company = response.body().getCompany();
                    companyEditText.setText(company);
                    address = response.body().getAddress();
                    addressEditText.setText(address);
                    phone = response.body().getPhone();
                    phoneEditText.setText(phone);
                }
                else {
                    Toast.makeText(getApplicationContext(), "Произошла ошибка!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetProfileInfoResponse> call, Throwable t) {
                t.printStackTrace();
                getHairdresserInfo(context, id);
            }
        });
    }

    private void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void updateUiWithUser() {
        String welcome = getString(R.string.successfulEdit);
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
        finish();
    }

    private void showRegisterFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    private void submitChanges(HashMap<String, String> info, Context context) {

        String userId = Long.toString(User.getID(getApplicationContext()));
        JSONObject object = new JSONObject(info);

        //  **new** request using retrofit
        Retrofit retfrofit = new Retrofit.Builder()
                .baseUrl(Constants.host)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        SubmitProfileApi registrationApi = retfrofit.create(SubmitProfileApi.class);
        Call<ResponseBody> call = registrationApi.sendInfo(userId,object.toString());

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                updateUiWithUser();
                setResult(Activity.RESULT_OK);
                finish();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, "Fail to get response = " + t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case pickedImage:
                //Получаем URI картинки:
                if (data == null) {
                    return;
                }
                Uri imageUri = data.getData();
                pickIt.getPath(imageUri, Build.VERSION.SDK_INT);
                //Находим нужный ImageView в интерфейсе
                System.out.println(imageUri);
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
//                    final String base64Image = Base64.encodeToString(bytes, Base64.DEFAULT);
                    Glide.with(this).load(imageUri).into(icon);
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
