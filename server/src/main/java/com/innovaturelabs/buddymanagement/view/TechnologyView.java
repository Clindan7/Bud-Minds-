/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.innovaturelabs.buddymanagement.view;

import com.innovaturelabs.buddymanagement.entity.Technology;
import com.innovaturelabs.buddymanagement.json.Json;

import java.time.LocalDateTime;

/**
 * @author nirmal
 */
public class TechnologyView {

    private final int technologyId;
    private final String technologyName;

    @Json.DateTimeFormat
    private final LocalDateTime createDate;
    @Json.DateTimeFormat
    private final LocalDateTime updateDate;
    private final byte status;

    public int getTechnologyId() {
        return technologyId;
    }

    public String getTechnologyName() {
        return technologyName;
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

    public TechnologyView(Technology technology) {
        this.technologyId = technology.getTechnologyId();
        this.technologyName = technology.getTechnologyName();
        this.createDate = technology.getCreateDate();
        this.updateDate = technology.getUpdateDate();
        this.status=technology.getStatus();
    }
}
