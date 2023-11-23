/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.innovaturelabs.buddymanagement.entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
public class Training {


    public enum Status {
        INACTIVE((byte) 0),
        ACTIVE((byte) 1);
        public final byte value;

        private Status(byte value) {
            this.value = value;
        }
    }
    public enum Department {
        DEVELOPMENT((byte) 1),
        QA((byte) 2);

        public final byte value;

        private Department(byte value) {
            this.value = value;
        }
    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer trainingId;
    private String title;
    private String trainingDescription;
    private LocalDate trainingStartDate;
    private LocalDate trainingEndDate;

    private Integer trainingCreator;
    private byte status;

    private byte departmentId;
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "technology_id", referencedColumnName = "technology_id")
    private Technology technologyId;

    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    public Training() {
    }

    public Training(Integer trainingId) {
        this.trainingId = trainingId;
    }

    public Training(String title, String trainingDescription, LocalDate trainingStartDate, LocalDate trainingEndDate, Integer trainingCreator, Technology technologyId, byte departmentId) {
        this.title = title;
        this.trainingDescription = trainingDescription;
        this.trainingStartDate = trainingStartDate;
        this.trainingEndDate = trainingEndDate;
        this.trainingCreator = trainingCreator;
        this.technologyId=technologyId;
        this.departmentId=departmentId;
        this.status = Status.ACTIVE.value;
        LocalDateTime dt = LocalDateTime.now();
        this.createDate = dt;
        this.updateDate = dt;
    }

    public Integer getTrainingId() {
        return trainingId;
    }

    public void setTrainingId(Integer trainingId) {
        this.trainingId = trainingId;
    }

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

    public byte getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(byte departmentId) {
        this.departmentId = departmentId;
    }

    public Technology getTechnologyId() {
        return technologyId;
    }

    public void setTechnologyId(Technology technologyId) {
        this.technologyId = technologyId;
    }

    public void setTrainingEndDate(LocalDate trainingEndDate) {
        this.trainingEndDate = trainingEndDate;
    }

    public Integer getTrainingCreator() {
        return trainingCreator;
    }

    public void setTrainingCreator(Integer trainingCreator) {
        this.trainingCreator = trainingCreator;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }



    @Override
    public String toString() {
        return "Training{" +
                "trainingId=" + trainingId +
                '}';
    }
}
