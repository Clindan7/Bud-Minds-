package com.innovaturelabs.buddymanagement.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserUpdateForm {

    @NotBlank(message = "{first.name.required}")
    @Size(message = "{size.first.name}", min = 1, max = 50)
    @Pattern(message = "{invalid.first.name}", regexp = "^[a-zA-Z]+([\\s][a-zA-Z]+)*+$")
    private String firstName;

    @NotBlank(message = "{last.name.required}")
    @Size(message = "{size.last.name}", min = 1, max = 20)
    @Pattern(message = "{invalid.last.name}", regexp = "^[a-zA-Z]+([\\s][a-zA-Z]+)*+$")
    private String lastName;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
