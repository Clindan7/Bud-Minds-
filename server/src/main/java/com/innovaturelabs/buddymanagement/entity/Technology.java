/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.innovaturelabs.buddymanagement.entity;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
public class Technology {

    public enum Status {
        INACTIVE((byte) 0),
        ACTIVE((byte) 1);
        public final byte value;

        private Status(byte value) {
            this.value = value;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer technologyId;
    private String technologyName;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private byte status;

    public Technology() {
    }

    public Technology(String technologyName) {
        this.technologyName = technologyName;
        this.status = Status.ACTIVE.value;
        LocalDateTime dt = LocalDateTime.now();
        this.createDate = dt;
        this.updateDate = dt;
    }

    public Integer getTechnologyId() {
        return technologyId;
    }

    public String getTechnologyName() {
        return technologyName;
    }

    public void setTechnologyName(String technologyName) {
        this.technologyName = technologyName;
    }

    public void setTechnologyId(Integer technologyId) {
        this.technologyId = technologyId;
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

    public Technology(Integer technologyId) {
        this.technologyId = technologyId;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Technology{" +
                "technologyId=" + technologyId +
                '}';
    }
}
