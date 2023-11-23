package com.innovaturelabs.buddymanagement.view;

import com.innovaturelabs.buddymanagement.entity.JoinerBatch;
import com.innovaturelabs.buddymanagement.entity.JoinerGroup;
import com.innovaturelabs.buddymanagement.json.Json;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class JoinerBatchView {
    private final Integer joinerBatchId;
    private final String joinerBatchName;
    private final byte status;
    @Json.DateTimeFormat
    private final LocalDateTime createDate;
    @Json.DateTimeFormat
    private final LocalDateTime updateDate;

    private List<JoinerGroupViewLite> joinerGroup;

    public JoinerBatchView(int joinerBatchId, String joinerBatchName, byte status, LocalDateTime createDate, LocalDateTime updateDate) {
        this.joinerBatchId = joinerBatchId;
        this.joinerBatchName = joinerBatchName;
        this.status = status;
        this.createDate = createDate;
        this.updateDate = updateDate;
    }

    public JoinerBatchView(JoinerBatch batch, List<JoinerGroup> joinerGroup) {
        this.joinerBatchId = batch.getJoinerBatchId();
        this.joinerBatchName = batch.getJoinerBatchName();
        this.joinerGroup=joinerGroup.stream().map(JoinerGroupViewLite::new).collect(Collectors.toList());
        this.status = batch.getStatus();
        this.createDate = batch.getCreateDate();
        this.updateDate = batch.getUpdateDate();
    }

    public JoinerBatchView(JoinerBatch joinerBatch) {
        this.joinerBatchId = joinerBatch.getJoinerBatchId();
        this.joinerBatchName = joinerBatch.getJoinerBatchName();
        this.status = joinerBatch.getStatus();
        this.createDate = joinerBatch.getCreateDate();
        this.updateDate = joinerBatch.getUpdateDate();
    }

    public Integer getJoinerBatchId() {
        return joinerBatchId;
    }

    public String getJoinerBatchName() {
        return joinerBatchName;
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

    public List<JoinerGroupViewLite> getJoinerGroup() {
        return joinerGroup;
    }

    public void setJoinerGroup(List<JoinerGroupViewLite> joinerGroup) {
        this.joinerGroup = joinerGroup;
    }
}
