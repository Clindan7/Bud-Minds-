package com.innovaturelabs.buddymanagement.form;

import com.innovaturelabs.buddymanagement.form.validaton.Password;
import javax.validation.constraints.NotBlank;

public class ResetPasswordForm {

    @Password
    private String newPassword;

    @NotBlank(message = "{email.token.required}")
    private String emailToken;

    public String getEmailToken() {
        return emailToken;
    }

    public void setEmailToken(String emailToken) {
        this.emailToken = emailToken;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
