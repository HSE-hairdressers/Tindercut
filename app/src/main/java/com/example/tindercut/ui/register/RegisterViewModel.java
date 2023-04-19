package com.example.tindercut.ui.register;

import android.content.Context;
import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tindercut.R;
import com.example.tindercut.data.RegisterRepository;
import com.example.tindercut.data.Result;
import com.example.tindercut.data.model.LoggedInUser;

import java.util.HashMap;

public class RegisterViewModel extends ViewModel {
    private final MutableLiveData<RegisterFormState> registerFormState = new MutableLiveData<>();
    private final MutableLiveData<RegisterResult> registerResult = new MutableLiveData<>();
    private final RegisterRepository registerRepository;

    RegisterViewModel(RegisterRepository registerRepository) {
        this.registerRepository = registerRepository;
    }

    LiveData<RegisterFormState> getRegisterFormState() {
        return registerFormState;
    }

    LiveData<RegisterResult> getRegisterResult() {
        return registerResult;
    }

    public void register(HashMap<String, String> info, Context context) {
        // can be launched in a separate asynchronous job
        Result<LoggedInUser> result = registerRepository.register(info, context);

        if (result instanceof Result.Success) {
            LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
            registerResult.setValue(new RegisterResult(new RegisteredInUserView(data.getDisplayName())));
        } else {
            registerResult.setValue(new RegisterResult(R.string.login_failed));
        }
    }

    public void registerDataChanged(HashMap<String, String> info) {
        if (!isUserNameValid(info.get("username"))) {
            registerFormState.setValue(new RegisterFormState(R.string.invalid_username, null, null, null, null, null, null));
        } else if (!isPasswordValid(info.get("password"))) {
            registerFormState.setValue(new RegisterFormState(null, R.string.invalid_password, null, null, null, null, null));
        } else if (!isVerificationCorrect(info.get("password"), info.get("verification"))) {
            registerFormState.setValue(new RegisterFormState(null, null, null, null, null, null, R.string.verification_error));
        } else {
            registerFormState.setValue(new RegisterFormState(true));
        }
    }

    private boolean isVerificationCorrect(String password, String verification) {
        if (password == null) {
            return false;
        }
        return password.equals(verification);
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }
}