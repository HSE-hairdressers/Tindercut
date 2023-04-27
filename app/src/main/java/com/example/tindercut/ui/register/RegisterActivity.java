package com.example.tindercut.ui.register;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.tindercut.MainActivity;
import com.example.tindercut.R;
import com.example.tindercut.data.Constants;
import com.example.tindercut.data.model.LoggedInUser;
import com.example.tindercut.databinding.ActivityRegisterBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private RegisterViewModel registerViewModel;
    private ActivityRegisterBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        registerViewModel = new ViewModelProvider(this, new RegisterViewModelFactory()).get(RegisterViewModel.class);

        HashMap<String, String> registrationInfo = new HashMap<>();
        final EditText usernameEditText = binding.username;
        final EditText nameEditText = binding.name;
        final EditText phoneEditText = binding.phoneNumber;
        final EditText addressEditText = binding.address;
        final EditText companyEditText = binding.company;
        final EditText passwordEditText = binding.password;
        final EditText verificationEditText = binding.passwordVerification;

        final Button registerButton = findViewById(R.id.logoutButton);
        final ProgressBar loadingProgressBar = binding.loading;

        registerViewModel.getRegisterFormState().observe(this, new Observer<RegisterFormState>() {
            @Override
            public void onChanged(@Nullable RegisterFormState registerFormState) {
                if (registerFormState == null) {
                    return;
                }
                registerButton.setEnabled(registerFormState.isDataValid());
                if (registerFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(registerFormState.getUsernameError()));
                }
                if (registerFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(registerFormState.getPasswordError()));
                }
                if (registerFormState.getNameError() != null) {
                    nameEditText.setError(getString(registerFormState.getNameError()));
                }
                if (registerFormState.getPhoneError() != null) {
                    phoneEditText.setError(getString(registerFormState.getPhoneError()));
                }
                if (registerFormState.getAddressError() != null) {
                    addressEditText.setError(getString(registerFormState.getAddressError()));
                }
                if (registerFormState.getCompanyError() != null) {
                    companyEditText.setError(getString(registerFormState.getCompanyError()));
                }
                if (registerFormState.getVerificationError() != null) {
                    verificationEditText.setError(getString(registerFormState.getVerificationError()));
                }
            }
        });

        registerViewModel.getRegisterResult().observe(this, new Observer<RegisterResult>() {
            @Override
            public void onChanged(@Nullable RegisterResult registerResult) {
                if (registerResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (registerResult.getError() != null) {
                    showRegisterFailed(registerResult.getError());
                }
                if (registerResult.getSuccess() != null) {
                    updateUiWithUser(registerResult.getSuccess());
                    setResult(Activity.RESULT_OK);
                    //Complete and destroy register activity once successful
                    finish();
                }
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
                registrationInfo.put("username", usernameEditText.getText().toString());
                registrationInfo.put("name", nameEditText.getText().toString());
                registrationInfo.put("phone", phoneEditText.getText().toString());
                registrationInfo.put("address", addressEditText.getText().toString());
                registrationInfo.put("company", companyEditText.getText().toString());
                registrationInfo.put("password", passwordEditText.getText().toString());
                registrationInfo.put("verification", verificationEditText.getText().toString());


                registerViewModel.registerDataChanged(registrationInfo);
            }
        };

        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        nameEditText.addTextChangedListener(afterTextChangedListener);
        phoneEditText.addTextChangedListener(afterTextChangedListener);
        addressEditText.addTextChangedListener(afterTextChangedListener);
        companyEditText.addTextChangedListener(afterTextChangedListener);

        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    registerViewModel.register(registrationInfo, getApplicationContext());
                }
                return false;
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                checkRegisterInfo(registrationInfo, getApplicationContext());
            }
        });
    }

    private void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void updateUiWithUser(RegisteredInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
        openMainActivity();
    }

    private void showRegisterFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    private void checkRegisterInfo(HashMap<String, String> info, Context context) {
        // url to post our data
        String url = Constants.getRegistrationURL();
        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(context);
        ((Map<String, String>) info).remove(((Map<String, String>) info).get("verification"));
        JSONObject object = new JSONObject(info);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String result = response.getString("result");
                    String name = response.getString("response");
                    if (result.equals("Ok")) {
                        LoggedInUser user = new LoggedInUser(java.util.UUID.randomUUID().toString(), name);
                        updateUiWithUser(new RegisteredInUserView(name));
                        setResult(Activity.RESULT_OK);
                        //Complete and destroy login activity once successful
                        finish();
                    } else {
                        showRegisterFailed(R.string.login_failed);
                    }
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
}