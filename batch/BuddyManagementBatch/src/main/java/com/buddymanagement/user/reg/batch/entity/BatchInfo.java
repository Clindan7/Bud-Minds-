package com.buddymanagement.user.reg.batch.entity;

import com.sun.istack.NotNull;
import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author anjali
 */
@Entity
public class BatchInfo implements Serializable {

    public static enum Status {

        STARTED((byte) 1),
        SUCCESS((byte) 2),
        FAILED((byte) 3);

        public final byte value;

        private Status(byte value) {
            this.value = value;
        }
    }

    public static enum BatchType {
        MAILSENDING((byte) 1);

        public final byte value;

        private BatchType(byte value) {
            this.value = value;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer batchId;

    @NotNull
    private byte batchType;

    private byte status;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date successDate;

    public Integer getBatchId() {
        return batchId;
    }

    public void setBatchId(Integer batchId) {
        this.batchId = batchId;
    }

    public byte getBatchType() {
        return batchType;
    }

    public void setBatchType(byte batchType) {
        this.batchType = batchType;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getSuccessDate() {
        return successDate;
    }

    public void setSuccessDate(Date successDate) {
        this.successDate = successDate;
    }

}
