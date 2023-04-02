package com.example.tindercut.ui.register;

import androidx.annotation.Nullable;

/**
 * Data validation state of the registration form.
 */
class RegisterFormState {
    @Nullable
    private Integer usernameError;
    @Nullable
    private Integer passwordError;
    @Nullable
    private Integer nameError;
    @Nullable
    private Integer phoneError;
    @Nullable
    private Integer addressError;
    @Nullable
    private Integer companyError;
    @Nullable
    private Integer verificationError;

    private boolean isDataValid;

    RegisterFormState(@Nullable Integer usernameError, @Nullable Integer passwordError,
                      @Nullable Integer nameError, @Nullable Integer phoneError,
                      @Nullable Integer addressError, @Nullable Integer companyError,
                      @Nullable Integer verificationError) {
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