package com.example.tindercut.ui.login;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;

import android.accounts.AccountManager;
import android.app.Activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.misc.AsyncTask;
import com.example.tindercut.MainActivity;
import com.example.tindercut.R;
import com.example.tindercut.data.model.LoggedInUser;
import com.example.tindercut.ui.login.LoginViewModel;
import com.example.tindercut.ui.login.LoginViewModelFactory;
import com.example.tindercut.databinding.ActivityLoginBinding;
import com.example.tindercut.ui.register.RegisterActivity;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.io.IOException;

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
        checkGoogleAuth();
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
                loginViewModel.login(usernameEditText.getText().toString(),
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


//    @Override
//    protected void onActivityResult(final int requestCode, final int resultCode,
//                                    final Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 123 && resultCode == RESULT_OK) {
//            final String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
//            AsyncTask<Void, Void, String> getToken = new AsyncTask<Void, Void, String>() {
//                @Override
//                protected String doInBackground(Void... params) {
//                    try {
//                        String token = GoogleAuthUtil.getToken(LoginActivity.this,
//                                accountName, SCOPES);
//                        return token;
//
//                    } catch (UserRecoverableAuthException userAuthEx) {
//                        startActivityForResult(userAuthEx.getIntent(), 123);
//                    } catch (IOException ioEx) {
//                        Log.d(TAG, "IOException");
//                    } catch (GoogleAuthException fatalAuthEx) {
//                        Log.d(TAG, "Fatal Authorization Exception" + fatalAuthEx.getLocalizedMessage());
//                    }
//                    return null;
//                }
//
//                @Override
//                protected void onPostExecute(String token) {
//                    ;
//                }
//
//            };
//            getToken.execute(null, null, null);
//        }
//    }

    private void openRegisterActivity() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
        openMainActivity();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
}