package com.innovaturelabs.buddymanagement.repository;

import com.innovaturelabs.buddymanagement.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Integer> {

    @Query(value = "select * from task where training_id=?1 and task_name like ?2% and status = 1 ORDER BY create_date DESC", nativeQuery = true)
    Page<Task> findByTrainingIdAndSearch(Integer trainingId, String search, PageRequest of);

    @Query(value = "select * from task where training_id in(select training_id from training where technology_id=?1) and task_name like ?2% and status = 1 ORDER BY create_date DESC", nativeQuery = true)
    Page<Task> findByTechnologyIdAndSearch(Integer technologyId, String search, PageRequest of);
    @Query(value = "select * from task where training_id in(select training_id from training where technology_id=?1) and task_name like ?2% and task_parent_id is not null and status = 1 ORDER BY create_date DESC", nativeQuery = true)
    Page<Task> findByTechnologyIdAndSearchSub(Integer technologyId, String search, PageRequest of);

    @Query(value = "select * from task where task_id=?1 and task_name like ?2% and status = 1 ORDER BY create_date DESC", nativeQuery = true)
    Page<Task> findByTaskIdAndSearch(Integer taskId, String search, PageRequest of);

    @Query(value = "select * from task where task_id=?1 and task_parent_id is not null and task_name like ?2% and status = 1 ORDER BY create_date DESC", nativeQuery = true)
    Page<Task> findBySubTaskIdAndSearch(Integer taskId, String search, PageRequest of);

    @Query(value = "select * from task where training_id=?1 and task_id=?2 and status = 1 ORDER BY create_date DESC", nativeQuery = true)
    Page<Task> findByTrainingIdAndTask(Integer trainingId, Integer taskId, PageRequest of);

    @Query(value = "select * from task where training_id in(select training_id from training where technology_id=?1) and task_id=?2 and status = 1 ORDER BY create_date DESC", nativeQuery = true)
    Page<Task> findByTechnologyIdAndTask(Integer technologyId, Integer taskId, PageRequest of);

    @Query(value = "select * from task where training_id in(select training_id from training where technology_id=?1) and task_id=?2 and task_parent_id is not null and status = 1 ORDER BY create_date DESC", nativeQuery = true)
    Page<Task> findByTechnologyIdAndSubTask(Integer technologyId, Integer taskId, PageRequest of);

    @Query(value = "select * from task where training_id=?1 and task_id=?2 and task_parent_id is not null and status = 1 ORDER BY create_date DESC", nativeQuery = true)
    Page<Task> findByTrainingIdAndSubTask(Integer trainingId, Integer taskId, PageRequest of);

    @Query(value = "select * from task where training_id=?1 and task_name like ?2% and task_id=?3 and status = 1 ORDER BY create_date DESC", nativeQuery = true)
    Page<Task> findByTrainingIdAndSearchAndTask(Integer trainingId, String search, Integer taskId, PageRequest of);

    @Query(value = "select * from task where training_id in(select training_id from training where technology_id=?1) and task_name like ?2% and task_id=?3 and status = 1 ORDER BY create_date DESC", nativeQuery = true)
    Page<Task> findByTechnologyIdAndSearchAndTask(Integer technologyId, String search, Integer taskId, PageRequest of);

    @Query(value = "select * from task where training_id in(select training_id from training where technology_id=?1) and task_name like ?2% and task_id=?3 and task_parent_id is not null and status = 1 ORDER BY create_date DESC", nativeQuery = true)
    Page<Task> findByTechnologyIdAndSearchAndSubTask(Integer technologyId, String search, Integer taskId, PageRequest of);

    @Query(value = "select * from task where training_id=?1 and task_name like ?2% and task_id=?3 and task_parent_id is not null and status = 1 ORDER BY create_date DESC", nativeQuery = true)
    Page<Task> findByTrainingIdAndSearchAndSubTask(Integer trainingId, String search, Integer taskId, PageRequest of);

    @Query(value = "select * from task where status=1 and training_id=?1 order by update_date desc", nativeQuery = true)
    Page<Task> findByTrainingId(Integer trainingId, PageRequest of);


    @Query(value = "select * from task where task_name like ?1% and status = 1 order by create_date desc", nativeQuery = true)
    Page<Task> findBySearch(String search, PageRequest of);

    @Query(value = "select * from task where status=1 and task_id=?1 order by update_date desc", nativeQuery = true)
    Page<Task> findByFilterTaskId(Integer taskId, PageRequest of);

    @Query(value = "select * from task where status=1 and training_id in(select training_id from training where technology_id=?1) order by update_date desc", nativeQuery = true)
    Page<Task> findByTechnologyId(Integer technologyId, PageRequest of);

    @Query(value = "select * from task where status=1 and training_id in(select training_id from training where technology_id=?1) and task_parent_id is not null order by update_date desc", nativeQuery = true)
    Page<Task> findByTechnologyIdSub(Integer technologyId, PageRequest of);

    @Query(value = "select * from task where status=1 and  task_parent_id=?1", nativeQuery = true)
    Page<Task> findByFilterSubTaskId(Integer taskId, PageRequest of);

    @Query(value = "select * from task where task_parent_id is null and status = 1 order by create_date desc", nativeQuery = true)
    Page<Task> findAllTask(PageRequest of);

    @Query(value = "select * from task where task_parent_id is not null and status = 1 order by create_date desc", nativeQuery = true)
    Page<Task> findAllSubTask(PageRequest of);

    @Query(value="select task_name from task where task_parent_id is null and status = 1 order by create_date desc",nativeQuery = true)
    List<String> findByTaskName();

    @Query(value="select * from task where task_parent_id is null and status = 1 order by task_name",nativeQuery = true)
    List<Task> findByTaskNameFilter();

    @Query(value="select task_name from task where task_parent_id is not null and status = 1 order by create_date desc",nativeQuery = true)
    List<String> findBySubTaskName();

    Optional<Task> findByTaskIdAndStatus(Integer taskId, byte value);

    Optional<Task> findByTaskId(Integer taskId);

    @Query(value="select * from task where task_id =?1  and task_parent_id is not null and status = 1",nativeQuery = true)
    Optional<Task> findBySubTaskId(Integer taskId);

    @Query(value="select * from task where task_id =?1  and task_parent_id is not null and status = 1",nativeQuery = true)
    Optional<Task>findBySubTaskIdAndStatus(Integer taskId);

    @Query(value = "select count(*) from task where training_id=?1 and task_name like ?2% and task_id=?3 and status = 1", nativeQuery = true)
    Integer findByTrainingIdAndSearchAndTaskCount(Integer trainingId, String search, Integer taskId);

    @Query(value = "select count(*) from task where training_id in(select training_id from training where technology_id=?1) and task_name like ?2% and task_id=?3 and status = 1", nativeQuery = true)
    Integer findByTechnologyIdAndSearchAndTaskCount(Integer technologyId, String search, Integer taskId);

    @Query(value = "select count(*) from task where training_id in(select training_id from training where technology_id=?1) and task_name like ?2% and task_id=?3 and task_parent_id is not null and status = 1", nativeQuery = true)
    Integer findByTechnologyIdAndSearchAndSubTaskCount(Integer technologyId, String search, Integer taskId);

    @Query(value = "select count(*) from task where training_id=?1 and task_name like ?2% and task_id=?3 and task_parent_id is not null and status = 1", nativeQuery = true)
    Integer findByTrainingIdAndSearchAndSubTaskCount(Integer trainingId, String search, Integer taskId);

    @Query(value = "select count(*) from task where training_id=?1 and task_name like ?2% and status = 1", nativeQuery = true)
    Integer findByTrainingIdAndSearchCount(Integer trainingId, String search);

    @Query(value = "select count(*) from task where training_id in(select training_id from training where technology_id=?1) and task_name like ?2% and status = 1", nativeQuery = true)
    Integer findByTechnologyIdAndSearchCount(Integer technologyId, String search);

    @Query(value = "select count(*) from task where training_id in(select training_id from training where technology_id=?1) and task_name like ?2% and task_parent_id is not null and status = 1", nativeQuery = true)
    Integer findByTechnologyIdAndSearchCountSub(Integer technologyId, String search);

    @Query(value = "select count(*) from task where task_id=?1 and task_name like ?2% and status = 1", nativeQuery = true)
    Integer findByTaskIdAndSearchCount(Integer taskId, String search);

    @Query(value = "select count(*) from task where task_id=?1 and task_parent_id is not null and task_name like ?2% and status = 1", nativeQuery = true)
    Integer findBySubTaskIdAndSearchCount(Integer taskId, String search);

    @Query(value = "select count(*) from task where training_id=?1 and task_id=?2 and status = 1", nativeQuery = true)
    Integer findByTrainingIdAndTaskCount(Integer trainingId, Integer taskId);

    @Query(value = "select count(*) from task where training_id in(select training_id from training where technology_id=?1) and task_id=?2 and status = 1", nativeQuery = true)
    Integer findByTechnologyIdAndTaskCount(Integer technologyId, Integer taskId);

    @Query(value = "select count(*) from task where training_id in(select training_id from training where technology_id=?1) and task_id=?2 and task_parent_id is not null and status = 1", nativeQuery = true)
    Integer findByTechnologyIdAndSubTaskCount(Integer technologyId, Integer taskId);

    @Query(value = "select count(*) from task where training_id=?1 and task_id=?2 and task_parent_id is not null and and status = 1", nativeQuery = true)
    Integer findByTrainingIdAndSubTaskCount(Integer trainingId, Integer taskId);

    @Query(value = "select count(*) from task where status=1 and training_id=?1", nativeQuery = true)
    Integer findByTrainingIdCount(Integer trainingId);


    @Query(value = "select count(*) from task where task_name like ?1% and status = 1", nativeQuery = true)
    Integer findBySearchCount(String search);

    @Query(value = "select count(*) from task where status=1 and  task_parent_id=?1", nativeQuery = true)
    Integer findCountFilterSubTaskId(Integer taskId);

    @Query(value = "select * from task where status=1 and task_id=?1", nativeQuery = true)
    Integer findByFilterTaskId(Integer taskId);

    @Query(value = "select count(*) from task where status=1 and training_id in(select training_id from training where technology_id=?1)", nativeQuery = true)
    Integer findByTechnologyId(Integer technologyId);

    @Query(value = "select count(*) from task where status=1 and training_id in(select training_id from training where technology_id=?1) and task_parent_id is not null", nativeQuery = true)
    Integer findByTechnologyIdSub(Integer technologyId);

    @Query(value = "select * from task where status=1 and task_id=?1", nativeQuery = true)
    Task findByTaskParentId(Integer task);

    @Query(value = "select task_name from task where status=1 and task_id=?1", nativeQuery = true)
    String findByMainTask(Integer parentTaskId);
}
