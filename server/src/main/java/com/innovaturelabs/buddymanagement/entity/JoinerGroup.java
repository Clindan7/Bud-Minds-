package com.innovaturelabs.buddymanagement.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class JoinerGroup {

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
    private Integer joinerGroupId;
    private String joinerGroupName;
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "joiner_batch_id", referencedColumnName = "joiner_batch_id")
    private JoinerBatch joinerBatch;
    private byte status;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    public JoinerGroup() {
        //Default constructor
    }

    public JoinerGroup(Integer joinerGroupId) {
        this.joinerGroupId = joinerGroupId;
    }

    public JoinerGroup(String joinerGroupName, JoinerBatch batch) {
        this.joinerGroupName = joinerGroupName;
        this.joinerBatch = batch;
        this.status = Status.ACTIVE.value;
        LocalDateTime dt = LocalDateTime.now();
        this.createDate = dt;
        this.updateDate = dt;
    }

    public Integer getJoinerGroupId() {
        return joinerGroupId;
    }

    public void setJoinerGroupId(Integer joinerGroupId) {
        this.joinerGroupId = joinerGroupId;
    }

    public String getJoinerGroupName() {
        return joinerGroupName;
    }

    public void setJoinerGroupName(String joinerGroupName) {
        this.joinerGroupName = joinerGroupName;
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
    public JoinerBatch getJoinerBatch() {
        return joinerBatch;
    }

    public void setJoinerBatch(JoinerBatch joinerBatch) {
        this.joinerBatch = joinerBatch;
    }
}
