package com.innovaturelabs.buddymanagement.repository;

import com.innovaturelabs.buddymanagement.entity.Score;
import com.innovaturelabs.buddymanagement.entity.TraineeTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ScoreRepository extends JpaRepository<Score,Integer>{
    Score findByTraineeTaskId(TraineeTask traineeTaskId);


    @Query(value = "select * from score where trainee_task_id=?1 AND status=?2", nativeQuery = true)
    Optional<Score> findByTraineeTaskIdAndStatus(Integer traineeTaskId,byte value);

    Score findByScoreId(Integer scoreId);

}