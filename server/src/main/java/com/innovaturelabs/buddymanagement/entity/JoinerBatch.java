package com.innovaturelabs.buddymanagement.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class JoinerBatch {
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
    private Integer joinerBatchId;
    private String joinerBatchName;
    private byte status;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    public JoinerBatch() {
    }

    public JoinerBatch(Integer joinerBatchId) {
        this.joinerBatchId = joinerBatchId;
    }

    public JoinerBatch(String joinerBatchName) {
        this.joinerBatchName = joinerBatchName;
        this.status = Status.ACTIVE.value;
        LocalDateTime dt = LocalDateTime.now();
        this.createDate = dt;
        this.updateDate = dt;
    }
    public Integer getJoinerBatchId() {
        return joinerBatchId;
    }

    public void setJoinerBatchId(Integer joinerBatchId) {
        this.joinerBatchId = joinerBatchId;
    }

    public String getJoinerBatchName() {
        return joinerBatchName;
    }

    public void setJoinerBatchName(String joinerBatchName) {
        this.joinerBatchName = joinerBatchName;
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
    public boolean equals(Object object) {
        if (!(object instanceof JoinerBatch)) {
            return false;
        }
        JoinerBatch other = (JoinerBatch) object;
        return Objects.equals(this.joinerBatchId, other.joinerBatchId);
    }

    @Override
    public String toString() {
        return "com.innovaturelabs.buddymanagement.entity.JoinerBatch[ joinerBatchId=" + joinerBatchId + " ]";
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (joinerBatchId != null ? joinerBatchId.hashCode() : 0);
        return hash;
    }
}
