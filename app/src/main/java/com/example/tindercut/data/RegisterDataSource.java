package com.example.tindercut.data;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.tindercut.R;
import com.example.tindercut.data.api.RegistrationApi;
import com.example.tindercut.data.api.RegistrationResponse;
import com.example.tindercut.data.model.LoggedInUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class RegisterDataSource {
    private String name;
    private String result;

    public Result<LoggedInUser> register(HashMap<String, String> info, Context context) {
        try {
            // TODO: handle loggedInUser authentication
            checkRegisterInfo(info, context);
            if (result.equals("Ok")) {
                LoggedInUser user = new LoggedInUser(java.util.UUID.randomUUID().toString(), info.get("name"));
                return new Result.Success<>(user);
            } else {
                return new Result.Error(new AuthFailureError("Wrong username or password"));
            }
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    private void checkRegisterInfo(HashMap<String, String> info, Context context) {
        ((Map<String, String>) info).remove(((Map<String, String>) info).get("verification"));
        JSONObject object = new JSONObject(info);

        //  **new** request using retrofit
        Retrofit retfrofit = new Retrofit.Builder()
                .baseUrl(Constants.host)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RegistrationApi registrationApi = retfrofit.create(RegistrationApi.class);
        Call<RegistrationResponse> call = registrationApi.createPost(object.toString());

        call.enqueue(new Callback<RegistrationResponse>() {
            @Override
            public void onResponse(Call<RegistrationResponse> call, retrofit2.Response<RegistrationResponse> response) {
                if(response.isSuccessful()) {
                    name = response.body().getName();
                    result = "Ok";
                }
            }

            @Override
            public void onFailure(Call<RegistrationResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void logout() {
        // TODO: revoke authentication
    }
}