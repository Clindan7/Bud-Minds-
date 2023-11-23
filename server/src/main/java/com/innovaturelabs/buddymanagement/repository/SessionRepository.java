package com.innovaturelabs.buddymanagement.repository;

import com.innovaturelabs.buddymanagement.entity.Session;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface SessionRepository extends Repository<Session, Integer> {
    Session save(Session session);

    Optional<Session> findById(Integer sessionId);

    @Query(value = "select * from session where session_id=?1 AND status !=?2", nativeQuery = true)
    Optional<Session> findByIdAndStatusNot(Integer sessionId, byte status);

    @Query(value = "select count(*) from session where trainer_id=?1 AND (((?2 BETWEEN session_start and session_end) OR (?3 BETWEEN session_start and session_end)) OR((session_start BETWEEN ?2 and ?3) OR (session_end BETWEEN ?2 and ?3)))", nativeQuery = true)
    Integer countInTrainer(Integer mentorId, LocalDateTime start, LocalDateTime end);

    @Query(value = "select count(*) from session where trainer_id=?1 AND session_id!=?2 AND (((?3 BETWEEN session_start and session_end) OR (?4 BETWEEN session_start and session_end)) OR((session_start BETWEEN ?3 and ?4) OR (session_end BETWEEN ?3 and ?4)))", nativeQuery = true)
    Integer countInTrainer(Integer mentorId, Integer sessionId, LocalDateTime start, LocalDateTime end);

    @Query(value = "select count(*) from session where joiner_group_id=?1 AND (((session_start BETWEEN ?2 and ?3) OR (session_end BETWEEN ?2 and ?3)) OR ((?2 BETWEEN session_start and session_end) OR (?3 BETWEEN session_start and session_end)))", nativeQuery = true)
    Integer countInGroup(Integer groupId, LocalDateTime start, LocalDateTime end);

    @Query(value = "select count(*) from session where joiner_group_id=?1 AND session_id!=?2 AND (((session_start BETWEEN ?3 and ?4) OR (session_end BETWEEN ?3 and ?4)) OR ((?3 BETWEEN session_start and session_end) OR (?4 BETWEEN session_start and session_end)))", nativeQuery = true)
    Integer countInGroup(Integer groupId, Integer sessionId, LocalDateTime start, LocalDateTime end);

    @Query(value = "select * from session where training_id=?1 AND status IN ?2 ORDER BY create_date DESC", nativeQuery = true)
    Page<Session> findByTrainingIdAndStatus(Integer trainingId, List<Byte> status, Pageable of);

    @Query(value = "select count(*) from session where training_id=?1 AND status IN ?2", nativeQuery = true)
    Integer countByTrainingIdAndStatus(Integer trainingId, List<Byte> status);

    @Query(value = "select * from session where trainer_id=?1 AND status IN ?2 ORDER BY create_date DESC", nativeQuery = true)
    Page<Session> findByTrainerIdAndStatus(Integer trainerId, List<Byte> status, Pageable of);

    @Query(value = "select count(*) from session where trainer_id=?1 AND status IN ?2", nativeQuery = true)
    Integer countByTrainerIdAndStatus(Integer trainerId, List<Byte> status);

    @Query(value = "select * from session where joiner_group_id=?1 AND trainer_id=?2 AND training_id in(select training_id from training where technology_id=?3) AND status IN ?4 ORDER BY create_date DESC", nativeQuery = true)
    Page<Session> findByTraineeUserAndStatus(Integer groupId, Integer trainerId, Integer technologyId, List<Byte> status, Pageable of);

    @Query(value = "select * from session where joiner_group_id=?1 AND status IN ?2 ORDER BY create_date DESC", nativeQuery = true)
    Page<Session> findByJoinerGroupIdAndStatus(Integer groupId, List<Byte> status, Pageable of);

    @Query(value = "select * from session s,joiner_group g where s.joiner_group_id=g.joiner_group_id and s.status In ?1 order by g.joiner_group_name asc", nativeQuery = true)
    List<Session> findByJoinerGroupIdFilter(List<Byte> status);

    @Query(value = "select count(*) from session where joiner_group_id=?1 AND status IN ?2", nativeQuery = true)
    Integer countByJoinerGroupIdAndStatus(Integer groupId, List<Byte> status);

    @Query(value = "select * from session where training_id=?1 AND trainer_id=?2 AND status IN ?3 ORDER BY create_date DESC", nativeQuery = true)
    Page<Session> findByTrainingIdAndTrainerIdAndStatus(Integer trainingId, Integer trainerId, List<Byte> status, Pageable of);

    @Query(value = "select count(*) from session where training_id=?1 AND trainer_id=?2 AND status IN ?3", nativeQuery = true)
    Integer countByTrainingIdAndTrainerIdAndStatus(Integer trainingId, Integer trainerId, List<Byte> status);

    @Query(value = "select * from session where training_id=?1 AND joiner_group_id=?2 AND status IN ?3 ORDER BY create_date DESC", nativeQuery = true)
    Page<Session> findByTrainingIdAndJoinerGroupIdAndStatus(Integer trainingId, Integer groupId, List<Byte> status, Pageable of);

    @Query(value = "select count(*) from session where training_id=?1 AND joiner_group_id=?2 AND status IN ?3", nativeQuery = true)
    Integer countByTrainingIdAndJoinerGroupIdAndStatus(Integer trainingId, Integer groupId, List<Byte> status);

    @Query(value = "select * from session where trainer_id=?1 AND joiner_group_id=?2 AND status IN ?3 ORDER BY create_date DESC", nativeQuery = true)
    Page<Session> findByTrainerIdAndJoinerGroupIdAndStatus(Integer trainerId, Integer groupId, List<Byte> status, Pageable of);

    @Query(value = "select count(*) from session where trainer_id=?1 AND joiner_group_id=?2 AND status IN ?3", nativeQuery = true)
    Integer countByTrainerIdAndJoinerGroupIdAndStatus(Integer trainerId, Integer groupId, List<Byte> status);

    @Query(value = "select * from session where training_id=?1 AND trainer_id=?2 AND joiner_group_id=?3 AND status IN ?4 ORDER BY create_date DESC", nativeQuery = true)
    Page<Session> findByTrainingIdAndTrainerIdAndJoinerGroupIdAndStatus(Integer trainingId, Integer trainerId, Integer groupId, List<Byte> status, Pageable of);

    @Query(value = "select count(*) from session where training_id=?1 AND trainer_id=?2 AND joiner_group_id=?3 AND status IN ?4", nativeQuery = true)
    Integer countByTrainingIdAndTrainerIdAndJoinerGroupIdAndStatus(Integer trainingId, Integer trainerId, Integer groupId, List<Byte> status);

    Page<Session> findAllByStatusInOrderByCreateDateDesc(List<Byte> status, Pageable paging);

    Integer countByStatusIn(List<Byte> status);

    @Query(value = "select * from session where training_id in(select training_id from training where technology_id=?1) AND trainer_id=?2 AND joiner_group_id=?3 AND status IN ?4 ORDER BY create_date DESC", nativeQuery = true)
    Page<Session> findByTechnologyIdAndTrainerIdAndJoinerGroupIdAndStatus(Integer technologyId, Integer trainerId, Integer groupId, List<Byte> statusList, PageRequest of);

    @Query(value = "select count(*) from session where training_id in(select training_id from training where technology_id=?1) AND trainer_id=?2 AND joiner_group_id=?3 AND status IN ?4", nativeQuery = true)
    Integer countByTechnologyIdAndTrainerIdAndJoinerGroupIdAndStatus(Integer technologyId, Integer trainerId, Integer groupId, List<Byte> status);

    @Query(value = "select * from session where trainer_id=?1 AND training_id in(select training_id from training where technology_id=?2) AND status IN ?3 ORDER BY create_date DESC", nativeQuery = true)
    Page<Session> findByTrainerIdAndTechnologyIdAndStatus(Integer trainerId, Integer technologyId, List<Byte> statusList, PageRequest of);

    @Query(value = "select count(*) from session where trainer_id=?1 AND training_id in(select training_id from training where technology_id=?2) AND status IN ?3 ORDER BY create_date DESC", nativeQuery = true)
    Integer countByTrainerIdAndTechnologyIdAndStatus(Integer trainerId, Integer technologyId, List<Byte> statusList);

    @Query(value = "select * from session where training_id in(select training_id from training where technology_id=?1) AND joiner_group_id=?2 AND status IN ?3 ORDER BY create_date DESC", nativeQuery = true)
    Page<Session> findByTechnologyIdAndJoinerGroupIdAndStatus(Integer technologyId, Integer groupId, List<Byte> statusList, PageRequest of);

    @Query(value = "select count(*) from session where training_id in(select training_id from training where technology_id=?1) AND joiner_group_id=?2 AND status IN ?3 ORDER BY create_date DESC", nativeQuery = true)
    Integer countByTechnologyIdAndJoinerGroupIdAndStatus(Integer technologyId, Integer groupId, List<Byte> statusList);

    @Query(value = "select * from session where training_id in(select training_id from training where technology_id=?1) AND status IN ?2 ORDER BY create_date DESC", nativeQuery = true)
    Page<Session> findByTechnologyIdAndStatus(Integer technologyId, List<Byte> statusList, PageRequest of);

    @Query(value = "select count(*) from session where training_id in(select training_id from training where technology_id=?1) AND status IN ?2 ORDER BY create_date DESC", nativeQuery = true)
    Integer countByTechnologyIdAndStatus(Integer technologyId, List<Byte> statusList);

    @Query(value = "select count(*) from session where joiner_group_id=?1 AND trainer_id=?2 AND training_id in(select training_id from training where technology_id=?3) AND status IN ?4 ORDER BY create_date DESC", nativeQuery = true)
    Integer countByTraineeUserAndStatus(Integer joinerGroupId, Integer trainerId, Integer technologyId, List<Byte> statusList);

    @Query(value = "select * from session where joiner_group_id=?1 AND status IN ?2 ORDER BY create_date DESC", nativeQuery = true)
    Page<Session> findByAllTraineeUserAndStatus(Integer joinerGroupId, List<Byte> statusList, PageRequest of);

    @Query(value = "select * from session s,training t where s.training_id=t.training_id and s.status In ?1 order by t.title asc", nativeQuery = true)
    List<Session> findByAllTraineeUserAndStatus(List<Byte> statusList);
    @Query(value = "select count(*) from session where joiner_group_id=?1 AND status IN ?2", nativeQuery = true)
    Integer findByAllCountTraineeUserAndStatus(Integer joinerGroupId, List<Byte> statusList);
}