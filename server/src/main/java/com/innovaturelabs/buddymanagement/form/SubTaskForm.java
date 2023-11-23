package com.innovaturelabs.buddymanagement.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;

public class SubTaskForm {
    private static final String VALIDATION = "^[a-zA-Z0-9\\p{Punct}]+([\\s][a-zA-Z0-9\\p{Punct}]+)*+$";

    @NotBlank(message = "{task.name.required}")
    @Size(message = "{size.task.name}", min = 1, max = 50)
    @Pattern(message = "{invalid.task.name}", regexp = VALIDATION)
    private String taskName;

    @NotBlank(message = "{task.description.required}")
    @Size(message = "{task.description.size}", min = 1, max = 250)
    @Pattern(message = "{invalid.task.description}", regexp = VALIDATION)
    private String taskDescription;

    private Integer parentTaskId;

    private LocalDate taskStart;

    private LocalDate taskEnd;

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public Integer getParentTaskId() {
        return parentTaskId;
    }

    public void setParentTaskId(Integer parentTaskId) {
        this.parentTaskId = parentTaskId;
    }

    public LocalDate getTaskStart() {
        return taskStart;
    }

    public void setTaskStart(LocalDate taskStart) {
        this.taskStart = taskStart;
    }

    public LocalDate getTaskEnd() {
        return taskEnd;
    }

    public void setTaskEnd(LocalDate taskEnd) {
        this.taskEnd = taskEnd;
    }

}
