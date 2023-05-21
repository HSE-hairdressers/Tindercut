package com.example.tindercut.ui.register;

import androidx.annotation.Nullable;

/**
 * Data validation state of the registration form.
 */
class RegisterFormState {
    @Nullable
    private final Integer usernameError;
    @Nullable
    private final Integer passwordError;
    @Nullable
    private final Integer nameError;
    @Nullable
    private final Integer phoneError;
    @Nullable
    private final Integer addressError;
    @Nullable
    private final Integer companyError;
    @Nullable
    private final Integer verificationError;

    private final boolean isDataValid;

    RegisterFormState(@Nullable Integer usernameError, @Nullable Integer passwordError, @Nullable Integer nameError, @Nullable Integer phoneError, @Nullable Integer addressError, @Nullable Integer companyError, @Nullable Integer verificationError) {
        this.usernameError = usernameError;
        this.passwordError = passwordError;
        this.nameError = nameError;
        this.phoneError = phoneError;
        this.addressError = addressError;
        this.companyError = companyError;
        this.verificationError = verificationError;
        this.isDataValid = false;
    }

    RegisterFormState(boolean isDataValid) {
        this.usernameError = null;
        this.passwordError = null;
        this.nameError = null;
        this.phoneError = null;
        this.addressError = null;
        this.companyError = null;
        this.verificationError = null;
        this.isDataValid = isDataValid;
    }

    @Nullable
    Integer getUsernameError() {
        return usernameError;
    }

    @Nullable
    Integer getPasswordError() {
        return passwordError;
    }

    @Nullable
    Integer getNameError() {
        return nameError;
    }

    @Nullable
    Integer getPhoneError() {
        return phoneError;
    }

    @Nullable
    Integer getAddressError() {
        return addressError;
    }

    @Nullable
    Integer getCompanyError() {
        return companyError;
    }

    @Nullable
    Integer getVerificationError() {
        return verificationError;
    }

    boolean isDataValid() {
        return isDataValid;
    }
}