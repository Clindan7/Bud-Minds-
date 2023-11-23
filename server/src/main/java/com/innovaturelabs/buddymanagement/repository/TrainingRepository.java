package com.innovaturelabs.buddymanagement.repository;

import com.innovaturelabs.buddymanagement.entity.Technology;
import com.innovaturelabs.buddymanagement.entity.Training;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TrainingRepository extends JpaRepository<Training, Integer> {

    @Query(value = "select * from training where title like ?1% and status = 1", nativeQuery = true)
    Page<Training> findBySearch(String search, PageRequest of);
//
    @Query(value = "select * from training where status = 1 order by create_date desc", nativeQuery = true)
    Page<Training> findAllTraining(Pageable paging);

    @Query(value = "select * from training where status=1 and technology_id=?1 order by update_date desc", nativeQuery = true)
    Page<Training> findByTechnologyId(Technology technologyId, Pageable of);

    @Query(value = "select * from training where status=1 and department_id=?1 order by update_date desc", nativeQuery = true)
    Page<Training> findByDepartmentId(byte departmentId, Pageable of);
    @Query(value="select title from training where status = 1 order by create_date desc",nativeQuery = true)
    List<String> findByTrainingName();

    Optional<Training> findByTrainingIdAndStatus(Integer trainingId,byte status);

    @Query(value="select * from training where training_id=?1 and status = 1",nativeQuery = true)
    Optional<Training> findByTrainingIdAndStatus(Integer trainingId);

    @Query(value = "select * from training where department_id=?1 and technology_id=?2 and title like ?3% and status = 1", nativeQuery = true)
    Page<Training> findByDepartmentIdTechnologyIdAndSearch(byte departmentId, Integer technologyId, String search,Pageable of);

    @Query(value = "select * from training where technology_id=?1 and title like ?2% and status = 1", nativeQuery = true)
    Page<Training> findByTechnologyIdAndSearch(Integer technologyId, String search,Pageable of);

    @Query(value = "select * from training where technology_id=?2 and department_id =?1 and status = 1", nativeQuery = true)
    Page<Training>  findByDepartmentIdTechnologyId(byte departmentId, Integer technologyId,Pageable of);
    @Query(value = "select * from training where department_id=?2 and title like ?1% and status = 1", nativeQuery = true)
    Page<Training>  findByDepartmentIdSearch(String search, byte departmentId,Pageable of);

    @Query(value = "select * from training where training_end_date >?1 and status=1",nativeQuery = true)
    List<Training> findByUpcomingTraining(LocalDateTime dateNow);
}
