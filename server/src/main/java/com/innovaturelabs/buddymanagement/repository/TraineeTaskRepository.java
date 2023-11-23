package com.innovaturelabs.buddymanagement.repository;

import com.innovaturelabs.buddymanagement.entity.TraineeTask;
import com.innovaturelabs.buddymanagement.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TraineeTaskRepository extends JpaRepository<TraineeTask, Integer> {

    @Query(value = "select * from trainee_task where trainee_id=?1 and task_id=?2 ORDER BY create_date DESC", nativeQuery = true)
    TraineeTask findByTraineeIdAndTaskId(Integer user, Integer taskId);

    Optional<TraineeTask> findByTraineeTaskIdAndStatus(Integer traineeTaskId, byte value);

    @Query(value = "select * from trainee_task where task_id=?1", nativeQuery = true)
    List<TraineeTask> findByTask(Integer taskId);

    @Query(value = "select * from trainee_task left join score on trainee_task.trainee_task_id=score.trainee_task_id where task_id in(select task_id from task where task_id=?1)", nativeQuery = true)
    Page<TraineeTask> findByTask(Integer taskId, Pageable pageable);

    @Query(value = "select count(*) from trainee_task left join score on trainee_task.trainee_task_id=score.trainee_task_id where task_id in(select task_id from task where task_id=?1)", nativeQuery = true)
    Integer countByTask(Integer taskId);

    @Query(value = "select * from trainee_task where task_id=?1 and status in ?2 ORDER BY create_date DESC", nativeQuery = true)
    Page<TraineeTask> findByTaskAndStatus(Integer taskId, List<Byte> status, PageRequest of);

    @Query(value = "select * from trainee_task where task_id=?1 and trainee_id in (select user_id from user where joiner_group_id=?2) and status in ?3 ORDER BY create_date DESC", nativeQuery = true)
    Page<TraineeTask> findByTaskIdAndJoinerGroupId(Integer taskId, Integer joinerGroupId, List<Byte> status, PageRequest of);

    Page<TraineeTask> findAllByStatusInOrderByCreateDateDesc(List<Byte> status, Pageable paging);

    @Query(value = "select * from trainee_task where trainee_id in (select user_id from user where joiner_group_id=?1) and status in ?2 ORDER BY create_date DESC", nativeQuery = true)
    Page<TraineeTask> findByJoinerGroupId(Integer joinerGroupId, List<Byte> status, PageRequest of);

    @Query(value = "select * from trainee_task where trainee_id=?1 and status in ?2 ORDER BY create_date DESC", nativeQuery = true)
    Page<TraineeTask> findByTaskOfTrainee(Integer currentUser, List<Byte> status, PageRequest of);

    @Query(value = "select count(*) from trainee_task where trainee_id=?1 and status in ?2", nativeQuery = true)
    Integer findByTaskOfTraineeCount(Integer currentUser, List<Byte> status);

    @Query(value = "select * from trainee_task where trainee_task_id=?1 and  trainee_id=?2 and status=?3", nativeQuery = true)
    Optional<TraineeTask> findByTraineeTaskIdAndStatusOfTrainee(Integer traineeTaskId, User currentUser, byte value);

    @Query(value = "select * from trainee_task where task_id=?1 and trainee_id in (select user_id from user where joiner_group_id=?2) and trainee_id=?3 and status in ?4 ORDER BY create_date DESC", nativeQuery = true)
    Page<TraineeTask> findByTaskIdAndJoinerGroupIdAndTraineeId(Integer taskId, Integer joinerGroupId, Integer traineeId, List<Byte> status, PageRequest of);

    @Query(value = "select * from trainee_task where task_id=?1 and trainee_id=?2 and status ?3 ORDER BY create_date DESC", nativeQuery = true)
    Page<TraineeTask> findByTaskIdAndTraineeId(Integer taskId, Integer traineeId, List<Byte> status, PageRequest of);

    @Query(value = "select * from trainee_task where trainee_id=?1 and status in ?2 ORDER BY create_date DESC", nativeQuery = true)
    Page<TraineeTask> findByTraineeIdAndGroup(Integer traineeId, List<Byte> status, PageRequest of);

    @Query(value = "select * from trainee_task where trainee_id=?1 and status in ?2 ORDER BY create_date DESC", nativeQuery = true)
    Page<TraineeTask> findByTraineeId(Integer traineeId, List<Byte> status, PageRequest of);


    @Query(value = "select count(*) from trainee_task where task_id=?1 and trainee_id in (select user_id from user where joiner_group_id=?2) and trainee_id=?3 and status in ?4", nativeQuery = true)
    Integer findByTaskIdAndJoinerGroupIdAndTraineeIdCount(Integer taskId, Integer joinerGroupId, Integer traineeId, List<Byte> status);

    @Query(value = "select count(*) from trainee_task where task_id=?1 and trainee_id in (select user_id from user where joiner_group_id=?2) and status in ?3", nativeQuery = true)
    Integer findByTaskIdAndJoinerGroupIdCount(Integer taskId, Integer joinerGroupId, List<Byte> status);

    @Query(value = "select count(*) from trainee_task where task_id=?1 and trainee_id=?2 and status in ?3", nativeQuery = true)
    Integer findByTaskIdAndTraineeIdCount(Integer taskId, Integer traineeId, List<Byte> status);

    @Query(value = "select count(*) from trainee_task where trainee_id=?1 and status in ?2", nativeQuery = true)
    Integer findByTraineeIdAndGroupCount(Integer traineeId, List<Byte> status);

    @Query(value = "select count(*) from trainee_task where trainee_id in (select user_id from user where joiner_group_id=?1) and status in ?2", nativeQuery = true)
    Integer findByJoinerGroupIdCount(Integer joinerGroupId, List<Byte> status);

    @Query(value = "select count(*) from trainee_task where task_id=?1 and status in ?2", nativeQuery = true)
    Integer findByTaskAndStatusCount(Integer taskId, List<Byte> status);

    @Query(value = "select count(*) from trainee_task where trainee_id=?1 and status in ?2", nativeQuery = true)
    Integer findByTraineeIdCount(Integer traineeId, List<Byte> status);

    @Query(value = "select count(*) from trainee_task where  status in ?1 ORDER BY create_date DESC", nativeQuery = true)
    Integer findCountAllStatus(List<Byte> status);

    @Query(value = "select * from trainee_task where task_id=?1 and trainee_id=?2 and status=2", nativeQuery = true)
    Optional<TraineeTask> findByTaskIdAndTraineeIds(Integer taskId, Integer traineeId);

    Optional<TraineeTask> findById(Integer traineeTaskId);


    @Query(value = "select * from trainee_task left join score on trainee_task.trainee_task_id=score.trainee_task_id where score.trainee_task_id IS NULL and trainee_id in(select user_id from user where user_id=?1 and mentor_id=?2 and manager_id=?3) and task_id in(select task_id from task where task_id=?4)", nativeQuery = true)
    List<TraineeTask> findByTraineeIdMentorIdManagerIdRangeNoScore(Integer traineeId, Integer mentorId, Integer managerId, Integer taskId, Pageable pageable);

    @Query(value = "select count(*) from trainee_task left join score on trainee_task.trainee_task_id=score.trainee_task_id where score.trainee_task_id IS NULL and trainee_id in(select user_id from user where user_id=?1 and mentor_id=?2 and manager_id=?3) and task_id in(select task_id from task where task_id=?4)", nativeQuery = true)
    Integer countByTraineeIdMentorIdManagerIdRangeNoScore(Integer traineeId, Integer mentorId, Integer managerId, Integer taskId);

    @Query(value = "select * from trainee_task left join score on trainee_task.trainee_task_id=score.trainee_task_id where score.overall_score>=?1 AND score.overall_score<?2 and trainee_id in(select user_id from user where user_id=?3 and mentor_id=?4 and manager_id=?5) and task_id in(select task_id from task where task_id=?6)", nativeQuery = true)
    List<TraineeTask> findByTraineeIdMentorIdManagerIdRange(Integer rangeStart, Integer rangeEnd, Integer traineeId, Integer mentorId, Integer managerId, Integer taskId, Pageable pageable);

    @Query(value = "select count(*) from trainee_task left join score on trainee_task.trainee_task_id=score.trainee_task_id where score.overall_score>=?1 AND score.overall_score<?2 and trainee_id in(select user_id from user where user_id=?3 and mentor_id=?4 and manager_id=?5) and task_id in(select task_id from task where task_id=?6)", nativeQuery = true)
    Integer countByTraineeIdMentorIdManagerIdRange(Integer rangeStart, Integer rangeEnd, Integer traineeId, Integer mentorId, Integer managerId, Integer taskId);

    @Query(value = "select * from trainee_task where trainee_id in(select user_id from user where user_id=?1 and mentor_id=?2 and manager_id=?3) and task_id in(select task_id from task where task_id=?4 )", nativeQuery = true)
    List<TraineeTask> findByTraineeIdMentorIdManagerId(Integer traineeId, Integer mentorId, Integer managerId, Integer taskId, Pageable pageable);

    @Query(value = "select count(*) from trainee_task where trainee_id in(select user_id from user where user_id=?1 and mentor_id=?2 and manager_id=?3) and task_id in(select task_id from task where task_id=?4)", nativeQuery = true)
    Integer countByTraineeIdMentorIdManagerId(Integer traineeId, Integer mentorId, Integer managerId, Integer taskId);


    @Query(value = "select * from trainee_task left join score on trainee_task.trainee_task_id=score.trainee_task_id where score.trainee_task_id IS NULL and trainee_id in(select user_id from user where user_id=?1 and mentor_id=?2) and task_id in(select task_id from task where task_id=?3)", nativeQuery = true)
    List<TraineeTask> findByTraineeIdMentorIdAndRangeNoScore(Integer traineeId, Integer mentorId, Integer taskId, Pageable pageable);

    @Query(value = "select count(*) from trainee_task left join score on trainee_task.trainee_task_id=score.trainee_task_id where score.trainee_task_id IS NULL and trainee_id in(select user_id from user where user_id=?1 and mentor_id=?2) and task_id in(select task_id from task where task_id=?3)", nativeQuery = true)
    Integer countByTraineeIdMentorIdAndRangeNoScore(Integer traineeId, Integer mentorId, Integer taskId);

    @Query(value = "select * from trainee_task left join score on trainee_task.trainee_task_id=score.trainee_task_id where score.overall_score>=?1 AND score.overall_score<?2 and trainee_id in(select user_id from user where user_id=?3 and mentor_id=?4) and task_id in(select task_id from task where task_id=?5)", nativeQuery = true)
    List<TraineeTask> findByTraineeIdMentorIdAndRange(Integer rangeStart, Integer rangeEnd, Integer traineeId, Integer mentorId, Integer taskId, Pageable pageable);

    @Query(value = "select count(*) from trainee_task left join score on trainee_task.trainee_task_id=score.trainee_task_id where score.overall_score>=?1 AND score.overall_score<?2 and trainee_id in(select user_id from user where user_id=?3 and mentor_id=?4) and task_id in(select task_id from task where task_id=?5)", nativeQuery = true)
    Integer countByTraineeIdMentorIdAndRange(Integer rangeStart, Integer rangeEnd, Integer traineeId, Integer mentorId, Integer taskId);

    @Query(value = "select * from trainee_task where trainee_id in(select user_id from user where user_id=?1 and mentor_id=?2) and task_id in(select task_id from task where task_id=?3)", nativeQuery = true)
    List<TraineeTask> findByTraineeIdMentorId(Integer traineeId, Integer mentorId, Integer taskId, Pageable pageable);

    @Query(value = "select count(*) from trainee_task where trainee_id in(select user_id from user where user_id=?1 and mentor_id=?2) and task_id in(select task_id from task where task_id=?3)", nativeQuery = true)
    Integer countByTraineeIdMentorId(Integer traineeId, Integer mentorId, Integer taskId);

    @Query(value = "select * from trainee_task left join score on trainee_task.trainee_task_id=score.trainee_task_id where score.trainee_task_id IS NULL and trainee_id in(select user_id from user where user_id=?1 and manager_id=?2) and task_id in(select task_id from task where task_id=?3)", nativeQuery = true)
    List<TraineeTask> findByTraineeManagerIdAndRangeNoScore(Integer traineeId, Integer managerId, Integer taskId, Pageable pageable);

    @Query(value = "select count(*) from trainee_task left join score on trainee_task.trainee_task_id=score.trainee_task_id where score.trainee_task_id IS NULL and trainee_id in(select user_id from user where user_id=?1 and manager_id=?2) and task_id in(select task_id from task where task_id=?3)", nativeQuery = true)
    Integer countByTraineeManagerIdAndRangeNoScore(Integer traineeId, Integer managerId, Integer taskId);

    @Query(value = "select * from trainee_task left join score on trainee_task.trainee_task_id=score.trainee_task_id where score.overall_score>=?1 AND score.overall_score<?2 and trainee_id in(select user_id from user where user_id=?3 and manager_id=?4) and task_id in(select task_id from task where task_id=?5)", nativeQuery = true)
    List<TraineeTask> findByTraineeManagerIdAndRange(Integer rangeStart, Integer rangeEnd, Integer traineeId, Integer managerId, Integer taskId, Pageable pageable);

    @Query(value = "select count(*) from trainee_task left join score on trainee_task.trainee_task_id=score.trainee_task_id where score.overall_score>=?1 AND score.overall_score<?2 and trainee_id in(select user_id from user where user_id=?3 and manager_id=?4) and task_id in(select task_id from task where task_id=?5)", nativeQuery = true)
    Integer countByTraineeManagerIdAndRange(Integer rangeStart, Integer rangeEnd, Integer traineeId, Integer managerId, Integer taskId);

    @Query(value = "select * from trainee_task where trainee_id in(select user_id from user where user_id=?1 and manager_id=?2) and task_id in(select task_id from task where task_id=?3)", nativeQuery = true)
    List<TraineeTask> findByTraineeManagerId(Integer traineeId, Integer managerId, Integer taskId, Pageable pageable);

    @Query(value = "select count(*) from trainee_task where trainee_id in(select user_id from user where user_id=?1 and manager_id=?2) and task_id in(select task_id from task where task_id=?3)", nativeQuery = true)
    Integer countByTraineeManagerId(Integer traineeId, Integer managerId, Integer taskId);

    @Query(value = "select * from trainee_task left join score on trainee_task.trainee_task_id=score.trainee_task_id where score.trainee_task_id IS NULL and trainee_id in(select user_id from user where mentor_id=?1 and manager_id=?2) and task_id in(select task_id from task where task_id=?3)", nativeQuery = true)
    List<TraineeTask> findByMentorIdManagerIdAndRangeNoScore(Integer mentorId, Integer managerId, Integer taskId, Pageable pageable);

    @Query(value = "select count(*) from trainee_task left join score on trainee_task.trainee_task_id=score.trainee_task_id where score.trainee_task_id IS NULL and trainee_id in(select user_id from user where mentor_id=?1 and manager_id=?2) and task_id in(select task_id from task where task_id=?3)", nativeQuery = true)
    Integer countByMentorIdManagerIdAndRangeNoScore(Integer mentorId, Integer managerId, Integer taskId);

    @Query(value = "select * from trainee_task left join score on trainee_task.trainee_task_id=score.trainee_task_id where score.overall_score>=?1 AND score.overall_score<?2 and trainee_id in(select user_id from user where mentor_id=?3 and manager_id=?4) and task_id in(select task_id from task where task_id=?5)", nativeQuery = true)
    List<TraineeTask> findByMentorIdManagerIdAndRange(Integer rangeStart, Integer rangeEnd, Integer mentorId, Integer managerId, Integer taskId, Pageable pageable);

    @Query(value = "select count(*) from trainee_task left join score on trainee_task.trainee_task_id=score.trainee_task_id where score.overall_score>=?1 AND score.overall_score<?2 and trainee_id in(select user_id from user where mentor_id=?3 and manager_id=?4) and task_id in(select task_id from task where task_id=?5)", nativeQuery = true)
    Integer countByMentorIdManagerIdAndRange(Integer rangeStart, Integer rangeEnd, Integer mentorId, Integer managerId, Integer taskId);

    @Query(value = "select * from trainee_task where trainee_id in(select user_id from user where mentor_id=?1 and manager_id=?2) and task_id in(select task_id from task where task_id=?3)", nativeQuery = true)
    List<TraineeTask> findByMentorIdManagerId(Integer mentorId, Integer managerId, Integer taskId, Pageable pageable);

    @Query(value = "select count(*) from trainee_task where trainee_id in(select user_id from user where mentor_id=?1 and manager_id=?2) and task_id in(select task_id from task where task_id=?3)", nativeQuery = true)
    Integer countByMentorIdManagerId(Integer mentorId, Integer managerId, Integer taskId);

    @Query(value = "select * from trainee_task left join score on trainee_task.trainee_task_id=score.trainee_task_id where score.trainee_task_id IS NULL and trainee_id in(select user_id from user where user_id=?1) and task_id in(select task_id from task where task_id=?2)", nativeQuery = true)
    List<TraineeTask> findByTraineeIdAndRangeNoScore(Integer traineeId, Integer taskId, Pageable pageable);

    @Query(value = "select count(*) from trainee_task left join score on trainee_task.trainee_task_id=score.trainee_task_id where score.trainee_task_id IS NULL and trainee_id in(select user_id from user where user_id=?1) and task_id in(select task_id from task where task_id=?2)", nativeQuery = true)
    Integer countByTraineeIdAndRangeNoScore(Integer traineeId, Integer taskId);

    @Query(value = "select * from trainee_task left join score on trainee_task.trainee_task_id=score.trainee_task_id where score.overall_score>=?1 AND score.overall_score<?2 and trainee_id in(select user_id from user where user_id=?3) and task_id in(select task_id from task where task_id=?4)", nativeQuery = true)
    List<TraineeTask> findByTraineeIdAndRange(Integer rangeStart, Integer rangeEnd, Integer traineeId, Integer taskId, Pageable pageable);

    @Query(value = "select count(*) from trainee_task left join score on trainee_task.trainee_task_id=score.trainee_task_id where score.overall_score>=?1 AND score.overall_score<?2 and trainee_id in(select user_id from user where user_id=?3) and task_id in(select task_id from task where task_id=?4)", nativeQuery = true)
    Integer countByTraineeIdAndRange(Integer rangeStart, Integer rangeEnd, Integer traineeid, Integer taskId);

    @Query(value = "select * from trainee_task where trainee_id in(select user_id from user where user_id=?1) and task_id in(select task_id from task where task_id=?2)", nativeQuery = true)
    List<TraineeTask> findByTraineeId(Integer traineeId, Integer taskId, Pageable pageable);

    @Query(value = "select count(*) from trainee_task where trainee_id in(select user_id from user where user_id=?1) and task_id in(select task_id from task where task_id=?2)", nativeQuery = true)
    Integer countByTraineeId(Integer traineeId, Integer taskId);

    @Query(value = "select * from trainee_task left join score on trainee_task.trainee_task_id=score.trainee_task_id where score.trainee_task_id IS NULL and trainee_id in(select user_id from user where mentor_id=?1) and task_id in(select task_id from task where task_id=?2)", nativeQuery = true)
    List<TraineeTask> findByMentorIdAndRangeNoScore(Integer mentorId, Integer taskId, Pageable pageable);

    @Query(value = "select count(*) from trainee_task left join score on trainee_task.trainee_task_id=score.trainee_task_id where score.trainee_task_id IS NULL and trainee_id in(select user_id from user where mentor_id=?1) and task_id in(select task_id from task where task_id=?2)", nativeQuery = true)
    Integer countByMentorIdAndRangeNoScore(Integer mentorId, Integer taskId);

    @Query(value = "select * from trainee_task left join score on trainee_task.trainee_task_id=score.trainee_task_id where score.overall_score>=?1 AND score.overall_score<?2 and trainee_id in(select user_id from user where mentor_id=?3) and task_id in(select task_id from task where task_id=?4)", nativeQuery = true)
    List<TraineeTask> findByMentorIdAndRange(Integer rangeStart, Integer rangeEnd, Integer mentorId, Integer taskId, Pageable pageable);

    @Query(value = "select count(*) from trainee_task left join score on trainee_task.trainee_task_id=score.trainee_task_id where score.overall_score>=?1 AND score.overall_score<?2 and trainee_id in(select user_id from user where mentor_id=?3) and task_id in(select task_id from task where task_id=?4)", nativeQuery = true)
    Integer countByMentorIdAndRange(Integer rangeStart, Integer rangeEnd, Integer mentorId, Integer taskId);

    @Query(value = "select * from trainee_task where trainee_id in(select user_id from user where mentor_id=?1) and task_id in(select task_id from task where task_id=?2)", nativeQuery = true)
    List<TraineeTask> findByMentorId(Integer mentorId, Integer taskId, Pageable pageable);

    @Query(value = "select count(*) from trainee_task where trainee_id in(select user_id from user where mentor_id=?1) and task_id in(select task_id from task where task_id=?2)", nativeQuery = true)
    Integer countByMentorId(Integer mentorId, Integer taskId);

    @Query(value = "select * from trainee_task left join score on trainee_task.trainee_task_id=score.trainee_task_id where score.trainee_task_id IS NULL and trainee_id in(select user_id from user where manager_id=?1) and task_id in(select task_id from task where task_id=?2)", nativeQuery = true)
    List<TraineeTask> findByManagerIdAndRangeNoScore(Integer managerId, Integer taskId, Pageable pageable);

    @Query(value = "select count(*) from trainee_task left join score on trainee_task.trainee_task_id=score.trainee_task_id where score.trainee_task_id IS NULL and trainee_id in(select user_id from user where manager_id=?1) and task_id in(select task_id from task where task_id=?2)", nativeQuery = true)
    Integer countByManagerIdAndRangeNoScore(Integer managerId, Integer taskId);

    @Query(value = "select * from trainee_task left join score on trainee_task.trainee_task_id=score.trainee_task_id where score.overall_score>=?1 AND score.overall_score<?2 and trainee_id in(select user_id from user where manager_id=?3) and task_id in(select task_id from task where task_id=?4)", nativeQuery = true)
    List<TraineeTask> findByManagerIdAndRange(Integer rangeStart, Integer rangeEnd, Integer managerId, Integer taskId, Pageable pageable);

    @Query(value = "select count(*) from trainee_task left join score on trainee_task.trainee_task_id=score.trainee_task_id where score.overall_score>=?1 AND score.overall_score<?2 and trainee_id in(select user_id from user where manager_id=?3) and task_id in(select task_id from task where task_id=?4)", nativeQuery = true)
    Integer countByManagerIdAndRange(Integer rangeStart, Integer rangeEnd, Integer mentorId, Integer taskId);

    @Query(value = "select * from trainee_task where trainee_id in(select user_id from user where manager_id=?1) and task_id in(select task_id from task where task_id=?2)", nativeQuery = true)
    List<TraineeTask> findByManagerId(Integer managerId, Integer taskId, Pageable pageable);

    @Query(value = "select count(*) from trainee_task where trainee_id in(select user_id from user where manager_id=?1) and task_id in(select task_id from task where task_id=?2)", nativeQuery = true)
    Integer countByManagerId(Integer managerId, Integer taskId);

    @Query(value = "select * from trainee_task left join score on trainee_task.trainee_task_id=score.trainee_task_id where score.trainee_task_id IS NULL and task_id in(select task_id from task where task_id=?1)", nativeQuery = true)
    List<TraineeTask> findByRangeNoScore(Integer taskId, Pageable pageable);

    @Query(value = "select count(*) from trainee_task left join score on trainee_task.trainee_task_id=score.trainee_task_id where score.trainee_task_id IS NULL and task_id in(select task_id from task where task_id=?1)", nativeQuery = true)
    Integer countByRangeNoScore(Integer taskId);

    @Query(value = "select * from trainee_task left join score on trainee_task.trainee_task_id=score.trainee_task_id where score.overall_score>=?1 AND score.overall_score<?2 and task_id in(select task_id from task where task_id=?3)", nativeQuery = true)
    List<TraineeTask> findByRange(Integer rangeStart, Integer rangeEnd, Integer taskId, Pageable pageable);

    @Query(value = "select count(*) from trainee_task left join score on trainee_task.trainee_task_id=score.trainee_task_id where score.overall_score>=?1 AND score.overall_score<?2 and task_id in(select task_id from task where task_id=?3)", nativeQuery = true)
    Integer countByRange(Integer rangeStart, Integer rangeEnd, Integer taskId);

    @Query(value = "select * from trainee_task where task_id=?1 and trainee_id=?2", nativeQuery = true)
    TraineeTask findByTaskId(Integer parentTask, Integer traineeId);

    @Query(value = "select * from trainee_task t,user u where t.trainee_id=u.user_id and task_id=?1", nativeQuery = true)
    List<TraineeTask> findByUserGroup(Integer taskId);
}
