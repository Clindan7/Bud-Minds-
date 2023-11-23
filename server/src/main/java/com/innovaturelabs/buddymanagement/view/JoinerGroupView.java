package com.innovaturelabs.buddymanagement.view;

import com.innovaturelabs.buddymanagement.entity.JoinerGroup;
import com.innovaturelabs.buddymanagement.json.Json;

import java.time.LocalDateTime;

public class JoinerGroupView {

    private final int joinerGroupId;

    private final String joinerGroupName;

    private final byte status;

    @Json.DateTimeFormat
    private final LocalDateTime createDate;

    @Json.DateTimeFormat
    private final LocalDateTime updateDate;

    final JoinerBatchView joinerBatch;

    public JoinerBatchView getJoinerBatch() {
        return joinerBatch;
    }

    public JoinerGroupView(JoinerGroup joinerGroup) {
        this.joinerGroupId = joinerGroup.getJoinerGroupId();
        this.joinerGroupName = joinerGroup.getJoinerGroupName();
        this.joinerBatch = new JoinerBatchView(joinerGroup.getJoinerBatch());
        this.status = joinerGroup.getStatus();
        this.createDate = joinerGroup.getCreateDate();
        this.updateDate = joinerGroup.getUpdateDate();

    }

    public int getJoinerGroupId() {
        return joinerGroupId;
    }

    public String getJoinerGroupName() {
        return joinerGroupName;
    }

    public byte getStatus() {
        return status;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

}
