/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.innovaturelabs.buddymanagement.view;
import com.innovaturelabs.buddymanagement.entity.Technology;
import com.innovaturelabs.buddymanagement.entity.Training;
import com.innovaturelabs.buddymanagement.json.Json;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author nirmal
 */
public class TrainingView {

    private final int trainingId;
    private final String title;
    private final String trainingDescription;
    private final Integer trainingCreator;
    @Json.DateFormat
    private final LocalDate trainingStartDate;
    @Json.DateFormat
    private final LocalDate trainingEndDate;

    private final Technology technologyId;

    private final byte departmentId;
    @Json.DateTimeFormat
    private final LocalDateTime createDate;
    @Json.DateTimeFormat
    private final LocalDateTime updateDate;

    private final byte status;

    public int getTrainingId() {
        return trainingId;
    }

    public String getTitle() {
        return title;
    }

    public String getTrainingDescription() {
        return trainingDescription;
    }

    public Integer getTrainingCreator() {
        return trainingCreator;
    }

    public LocalDate getTrainingStartDate() {
        return trainingStartDate;
    }

    public LocalDate getTrainingEndDate() {
        return trainingEndDate;
    }

    public Technology getTechnologyId() {
        return technologyId;
    }

    public byte getDepartmentId() {
        return departmentId;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public byte getStatus() {
        return status;
    }


    public TrainingView(Training training){
        this.trainingId = training.getTrainingId();
        this.title = training.getTitle();
        this.trainingDescription = training.getTrainingDescription();
        this.trainingCreator = training.getTrainingCreator();
        this.trainingStartDate = training.getTrainingStartDate();
        this.trainingEndDate = training.getTrainingEndDate();
        this.technologyId=training.getTechnologyId();
        this.departmentId=training.getDepartmentId();
        this.createDate = training.getCreateDate();
        this.updateDate = training.getUpdateDate();
        this.status = training.getStatus();
    }
}
