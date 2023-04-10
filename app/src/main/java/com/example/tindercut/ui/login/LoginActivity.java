package com.example.tindercut.ui.login;

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
import com.example.tindercut.data.User;
import com.example.tindercut.data.model.LoggedInUser;
import com.example.tindercut.databinding.ActivityLoginBinding;
import com.example.tindercut.ui.register.RegisterActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private final static String G_PLUS_SCOPE =
            "oauth2:https://www.googleapis.com/auth/plus.me";
    private final static String USERINFO_SCOPE =
            "https://www.googleapis.com/auth/userinfo.profile";
    private final static String EMAIL_SCOPE =
            "https://www.googleapis.com/auth/userinfo.email";
    private final static String SCOPES = G_PLUS_SCOPE + " " + USERINFO_SCOPE + " " + EMAIL_SCOPE;
    private static final int RC_SIGN_IN = 420;
    private LoginViewModel loginViewModel;

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (User.isLogin(getApplicationContext())) {
            updateUiWithUser(new LoggedInUserView(User.getName(getApplicationContext())));
            openMainActivity();
            finish();
        }

        com.example.tindercut.databinding.ActivityLoginBinding binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        final EditText usernameEditText = binding.username;
        final EditText passwordEditText = binding.password;
        final Button loginButton = binding.login;
        final Button registerButton = binding.register;
        final ProgressBar loadingProgressBar = binding.loading;
        final SignInButton googleSignIn = binding.googleSignIn;

        registerButton.setEnabled(true);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso);
//        checkGoogleAuth();
        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {
                    updateUiWithUser(loginResult.getSuccess());
                    setResult(Activity.RESULT_OK);
                    //Complete and destroy login activity once successful
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
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString(), getApplicationContext());
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
//                loginViewModel.login(usernameEditText.getText().toString(),
//                        passwordEditText.getText().toString(), getApplicationContext());
                checkLoginInfo(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString(), getApplicationContext());
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                openRegisterActivity();
                loadingProgressBar.setVisibility(View.INVISIBLE);
            }
        });
        googleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInWithGoogle();
            }
        });
    }

    private void signInWithGoogle() {
        Intent signInIntent = gsc.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN && resultCode == RESULT_OK) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                task.getResult(ApiException.class);
                checkGoogleAuth();
            } catch (ApiException e) {
                Toast.makeText(getApplicationContext(), R.string.failedGAuth, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void checkGoogleAuth() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
//            System.out.println(account.getEmail());
            updateUiWithUser(new LoggedInUserView(account.getDisplayName()));
            setResult(Activity.RESULT_OK);
            //Complete and destroy login activity once successful
            finish();
        }
    }

    private void openRegisterActivity() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
        openMainActivity();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    private void checkLoginInfo(String username, String password, Context context) {
        // url to post our data
        String url = "http://79.137.206.63:8011/auth/login";
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
                            String result = response.getString("result");
                            String name = response.getString("response");
                            if (Objects.equals(result, "Ok")) {
                                LoggedInUser user =
                                        new LoggedInUser(java.util.UUID.randomUUID().toString(), name);
                                User.setLogin(getApplicationContext(), name);
                                updateUiWithUser(new LoggedInUserView(name));
                            } else {
                                showLoginFailed(R.string.login_failed);
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