package com.innovaturelabs.buddymanagement.form;

import javax.validation.constraints.*;

public class JoinerGroupForm {
    @NotBlank(message = "{group.name.required}")
    @Size(message = "{size.group.name}", min = 1, max = 20)
    @Pattern(message = "{invalid.group.name}", regexp = "^[a-zA-Z0-9]+([\\s][a-zA-Z0-9]+)*+$")
    private String joinerGroupName;


    @NotNull(message = "{batchId.required}")
    private Integer joinerBatchId;

    public String getJoinerGroupName() {
        return joinerGroupName;
    }

    public void setJoinerGroupName(String joinerGroupName) {
        this.joinerGroupName = joinerGroupName;
    }

    public Integer getJoinerBatchId() {
        return joinerBatchId;
    }

    public void setJoinerBatchId(Integer joinerBatchId) {
        this.joinerBatchId = joinerBatchId;
    }

}
