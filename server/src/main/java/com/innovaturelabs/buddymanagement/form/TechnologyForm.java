package com.innovaturelabs.buddymanagement.form;

import javax.validation.constraints.*;

public class TechnologyForm {

    @NotBlank(message = "{technology.name.required}")
    @Size(message = "{size.technology.name}", min = 1, max = 50)
    @Pattern(message = "{invalid.technology.name}", regexp = "^[a-zA-Z0-9_@./#&+-]+([\\s][a-zA-Z0-9_@./#&+-]+)*+$")
    private String technologyName;

    public String getTechnologyName() {
        return technologyName;
    }

    public void setTechnologyName(String technologyName) {
        this.technologyName = technologyName;
    }
}