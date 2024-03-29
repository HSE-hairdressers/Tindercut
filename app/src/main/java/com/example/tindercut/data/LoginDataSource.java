package com.example.tindercut.data;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.tindercut.data.model.LoggedInUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {
    private String name;
    private String result;

    public Result<LoggedInUser> login(String username, String password, Context context) {
        try {
            checkLoginInfo(username, password, context);
            if (result.equals("Ok")) {
                LoggedInUser user =
                        new LoggedInUser(java.util.UUID.randomUUID().toString(), name);
                return new Result.Success<>(user);
            } else {
                return new Result.Error(new AuthFailureError("Wrong username or password"));
            }
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    private void checkLoginInfo(String username, String password, Context context) {
        VolleyLog.DEBUG = true;
        // url to post our data
        String url = Constants.getLoginURL();
        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(context);
        JSONObject object = new JSONObject();
        try {
            //input your API parameters
            object.put("username", username);
            object.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            result = response.getString("result");
                            name = response.getString("response");
                            Log.v("Volley", result);
                            Log.v("Volley", name);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("DEV", error.toString());
                Toast.makeText(context, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsonObjectRequest);
    }

    public void logout() {
        // TODO: revoke authentication
    }


}