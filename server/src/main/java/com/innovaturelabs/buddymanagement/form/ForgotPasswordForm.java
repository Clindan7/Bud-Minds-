package com.innovaturelabs.buddymanagement.form;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class ForgotPasswordForm {

    @NotBlank(message = ("{email.is.required}"))
    @Size(max = 255)
    @Email(message = ("{invalid.email.format}"),regexp = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
