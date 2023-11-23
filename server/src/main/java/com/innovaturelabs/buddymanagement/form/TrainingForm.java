package com.innovaturelabs.buddymanagement.form;


import javax.validation.constraints.*;
import java.time.LocalDate;

public class TrainingForm {
    private static final String REG_X="^[a-zA-Z0-9\\p{Punct}]+([\\s][a-zA-Z0-9\\p{Punct}]+)*+$";

    @NotBlank(message = "{title.required}")
    @Size(message = "{size.title.name}", min = 1, max = 50)
    @Pattern(message = "{invalid.title.name}", regexp = REG_X)
    private String title;


    @Size(message = "{size.description}", max = 250)
    @Pattern(message = "{invalid.technology.description}", regexp = REG_X+"|(^$)")
    private String trainingDescription;

    private LocalDate trainingStartDate;

    private LocalDate trainingEndDate;
    @Min(value = 1,message = ("{invalid.department}"))
    private byte departmentId;

    private Integer technologyId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTrainingDescription() {
        return trainingDescription;
    }

    public void setTrainingDescription(String trainingDescription) {
        this.trainingDescription = trainingDescription;
    }

    public LocalDate getTrainingStartDate() {
        return trainingStartDate;
    }

    public void setTrainingStartDate(LocalDate trainingStartDate) {
        this.trainingStartDate = trainingStartDate;
    }

    public LocalDate getTrainingEndDate() {
        return trainingEndDate;
    }

    public void setTrainingEndDate(LocalDate trainingEndDate) {
        this.trainingEndDate = trainingEndDate;
    }

    public byte getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(byte departmentId) {
        this.departmentId = departmentId;
    }

    public Integer getTechnologyId() {
        return technologyId;
    }

    public void setTechnologyId(Integer technologyId) {
        this.technologyId = technologyId;
    }
}
