package com.example.tindercut.data;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.tindercut.data.model.LoggedInUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
            if (Objects.equals(result, "Ok")) {
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
        // url to post our data
        String url = "http://79.137.206.63:8011/auth/registration";
        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(context);
        ((Map<String, String>) info).remove(((Map<String, String>) info).get("verification"));
        JSONObject object = new JSONObject(info);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            result = response.getString("result");
                            name = response.getString("response");
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

    public void logout() {
        // TODO: revoke authentication
    }
}