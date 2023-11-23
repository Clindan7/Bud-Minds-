package com.innovaturelabs.buddymanagement.view;

import com.innovaturelabs.buddymanagement.entity.Technology;

public class TraineeTechnologyView {
    private final Integer technologyId;

    private final String technologyName;

    public Integer getTechnologyId() {
        return technologyId;
    }

    public String getTechnologyName() {
        return technologyName;
    }

    public TraineeTechnologyView(Technology technology) {
        this.technologyId = technology.getTechnologyId();
        this.technologyName = technology.getTechnologyName();
    }
}
