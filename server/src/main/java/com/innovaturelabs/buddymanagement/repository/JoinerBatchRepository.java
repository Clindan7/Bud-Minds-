package com.innovaturelabs.buddymanagement.repository;

import com.innovaturelabs.buddymanagement.entity.JoinerBatch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface JoinerBatchRepository extends Repository<JoinerBatch, Integer> {

    JoinerBatch save(JoinerBatch batch);

    Optional<JoinerBatch> findByJoinerBatchId(Integer joinerBatchId);

//    @Query(value = "select * from joiner_batch where joiner_batch_name like %?1% and status=1 order by update_date desc", nativeQuery = true)
    Page<JoinerBatch> findBySearch(String search, PageRequest of);

    @Query(value = "select * from joiner_batch where status=1 order by update_date desc", nativeQuery = true)
    Page<JoinerBatch> sortBatch(Pageable paging);

    Optional<JoinerBatch> findByJoinerBatchNameAndStatus(String joinerBatchName,byte status);

    Collection<JoinerBatch> findAllByStatusOrderByCreateDateDesc(byte status);

    Optional<JoinerBatch> findByJoinerBatchIdAndStatus(Integer joinerBatchId,byte status);
    @Query(value="select joiner_batch_name from joiner_batch where status = 1 order by create_date desc",nativeQuery = true)
    List<String> fetchBatchName();

    @Query(value="select joiner_batch_name from joiner_batch where status=1 order by update_date desc",nativeQuery = true)
    List<String> findAllBatchName();
}
