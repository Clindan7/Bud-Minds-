package com.innovaturelabs.buddymanagement.repository;

import com.innovaturelabs.buddymanagement.entity.JoinerBatch;
import com.innovaturelabs.buddymanagement.entity.JoinerGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface JoinerGroupRepository extends Repository<JoinerGroup, Integer> {
    @Query(value = "select * from joiner_group where joiner_group_name like %?1% and status=1 order by update_date desc", nativeQuery = true)
    Page<JoinerGroup> findByGroupSearch(String groupSearch, PageRequest of);


    @Query(value = "select * from joiner_group where status=1 order by update_date desc", nativeQuery = true)
    Page<JoinerGroup> findAllGroup(Pageable paging);


    @Query(value = "select * from joiner_group where status=1 and joiner_batch_id=?1 order by update_date desc", nativeQuery = true)
    Page<JoinerGroup> findByJoinerBatch(JoinerBatch batchId, PageRequest of);

    Optional<JoinerGroup> findByJoinerGroupId(Integer joinerGroupId);

    Optional<JoinerGroup> findByJoinerGroupIdAndStatus(Integer joinerGroupId,byte value);


    JoinerGroup save(JoinerGroup joinerGroup);

    @Query(value = "select * from joiner_group where joiner_group_name like %?1% and status=1", nativeQuery = true)
    List<JoinerGroup> findByGroupSearch(String groupSearch);

    @Query(value = "select * from joiner_group where status=1 and joiner_batch_id=?1 order by update_date desc", nativeQuery = true)
    List<JoinerGroup> findByJoinerBatch(JoinerBatch batchId);

    @Query(value = "select * from joiner_group where status=1 order by create_date desc", nativeQuery = true)
    List<JoinerGroup> findAllGroup();

    @Query(value = "select count(*) from joiner_group where joiner_batch_id=?1 and status=1",nativeQuery = true)
    Integer countInBatch(Integer batchId);

    @Query(value = "select * from joiner_group where joiner_batch_id=?1 and (joiner_group_name like %?2%) and status=1 order by update_date desc",nativeQuery = true)
    Page<JoinerGroup> findByJoinerBatchIdAndGroupSearch(Integer joinerBatchId, String groupSearch, Pageable of);

    @Query(value = "select * from joiner_group where joiner_batch_id=?1 and (joiner_group_name like %?2%) and status=1",nativeQuery = true)
    List<JoinerGroup> findByJoinerBatchIdAndGroupSearch(Integer joinerBatchId, String groupSearch);

    List<JoinerGroup> findByJoinerBatchJoinerBatchIdAndStatus(Integer joinerBatchId, byte value);

    Optional<JoinerGroup> findByJoinerGroupName(String joinerGroupName);

    Optional<JoinerGroup> findByJoinerGroupNameAndJoinerBatchJoinerBatchIdAndStatus(String joinerGroupName,Integer joinerBatchId,byte status);

    Collection<JoinerGroup> findAllByStatusOrderByCreateDateDesc(byte status);
}
