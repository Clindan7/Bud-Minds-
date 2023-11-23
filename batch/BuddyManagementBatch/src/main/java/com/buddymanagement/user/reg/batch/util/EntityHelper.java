package com.buddymanagement.user.reg.batch.util;

import com.buddymanagement.user.reg.batch.repository.BatchInfoRepository;
import com.buddymanagement.user.reg.batch.entity.BatchInfo;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

//import com.buddymanagement.user.reg.batch.repository.BatchInfoRepository;

/**
*
* @author anjali
*/
public class EntityHelper {
	
    @Autowired
    BatchInfoRepository batchInfoRepository;

    public void saveBatchInfo(BatchInfo batch) {
        batch.setStatus(BatchInfo.Status.STARTED.value);
        batch.setBatchType(BatchInfo.BatchType.MAILSENDING.value);
        batch.setStartTime(new Date());
        batch.setEndTime(new Date());

        batchInfoRepository.save(batch);

    }

    public void updateBatchInfo(BatchInfo batch, boolean status) {

        Date dt = new Date();
        if (status) {
            batch.setStatus(BatchInfo.Status.SUCCESS.value);
            batch.setSuccessDate(dt);
        }
        else {
            batch.setStatus(BatchInfo.Status.FAILED.value);
        }

        batch.setEndTime(dt);
        batchInfoRepository.save(batch);

    }

    public boolean isLastBatchPending() {

		      BatchInfo lastBatch = batchInfoRepository
				.findFirstByBatchTypeOrderByStartTimeDesc(BatchInfo.BatchType.MAILSENDING.value);
		if (lastBatch == null) {
			return false;
		}
		if (lastBatch.getStatus() == BatchInfo.Status.STARTED.value) {
			return true;
		} else {
			return false;
		}

    }

}
