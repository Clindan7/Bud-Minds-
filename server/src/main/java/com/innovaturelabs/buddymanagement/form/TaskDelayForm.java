package com.innovaturelabs.buddymanagement.form;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class TaskDelayForm {

    private static final String VALIDATION = "^[a-zA-Z0-9\\p{Punct}]+([\\s][a-zA-Z0-9\\p{Punct}]+)*+$";

    @Pattern(message = "{invalid.delay.reason}", regexp = VALIDATION)
    @Size(message = "{delay.description.invalid}", min = 1, max = 100)
    private String delayReason;

    public String getDelayReason() {
        return delayReason;
    }

    public void setDelayReason(String delayReason) {
        this.delayReason = delayReason;
    }
}
