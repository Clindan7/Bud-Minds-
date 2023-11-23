package com.innovaturelabs.buddymanagement.form;

import javax.validation.constraints.*;

public class JoinerBatchForm {
    @Size(message = "{size.batch.name}", min = 0, max = 30)
    @Pattern(message = "{invalid.batch.name}", regexp = "(^[a-zA-Z0-9]+([\\s][a-zA-Z0-9]+)*+$)|(^$)")
    private String joinerBatchName;

    @NotBlank(message = "{batch.month.required}")
    @Pattern(message = "{invalid.batch.month}", regexp = "^[a-zA-Z]+[a-zA-Z]*+$")
    private String joinerBatchMonth;

    @Max(value=9999,message = "{year.not.valid}")
    @NotNull(message = "{year.required}")
    private Integer joinerBatchYear;

    public String getJoinerBatchMonth() {
        return joinerBatchMonth;
    }

    public void setJoinerBatchMonth(String joinerBatchMonth) {
        this.joinerBatchMonth = joinerBatchMonth;
    }

    public Integer getJoinerBatchYear() {
        return joinerBatchYear;
    }

    public void setJoinerBatchYear(Integer joinerBatchYear) {
        this.joinerBatchYear = joinerBatchYear;
    }

    public String getJoinerBatchName() {
        return joinerBatchName;
    }

    public void setJoinerBatchName(String joinerBatchName) {
        this.joinerBatchName = joinerBatchName;
    }
}
