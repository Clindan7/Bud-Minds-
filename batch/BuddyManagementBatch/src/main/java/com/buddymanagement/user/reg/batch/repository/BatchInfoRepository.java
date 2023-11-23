package com.buddymanagement.user.reg.batch.repository;

import com.buddymanagement.user.reg.batch.entity.BatchInfo;
import org.springframework.data.jpa.repository.JpaRepository;


/**
*
* @author ajmal
*/
public interface BatchInfoRepository extends JpaRepository<BatchInfo, Integer>{

	   BatchInfo findFirstByBatchTypeOrderByStartTimeDesc(byte type);
	
}
