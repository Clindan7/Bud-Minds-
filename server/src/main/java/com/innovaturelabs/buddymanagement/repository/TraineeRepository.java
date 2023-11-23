package com.innovaturelabs.buddymanagement.repository;

import com.innovaturelabs.buddymanagement.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TraineeRepository extends JpaRepository<User, Integer> {
    @Query(value = "select * from user where user_role=4 and (first_name like ?1%  or last_name like ?1%) and status=?2", nativeQuery = true)
    Page<User> findBySearch(String search, byte status, PageRequest of);

    @Query(value = "select * from user where user_role=4 and status=?1 order by create_date desc", nativeQuery = true)
    Page<User> findAllTrainees(byte status, Pageable paging);

    @Query(value = "select * from user where employee_Id=?1 and user_role=4 and status=?2", nativeQuery = true)
    Page<User> findByEmployeeId(Long employeeId, byte status, Pageable of);

    @Query(value = "select first_name from user where user_role=4 and status in(1,2) order by create_date desc", nativeQuery = true)
    List<String> findFirstNames();

    @Query(value = "select first_name from user where user_role=4 and status=?1 order by create_date desc", nativeQuery = true)
    List<String> findFirstNamesStatus(byte status);

    @Query(value = "select * from user where employee_Id=?1 and (first_name like ?2%  or last_name like ?2%) and user_role=4 and status=?3", nativeQuery = true)
    Page<User> findByEmployeeIdAndSearch(Long employeeId, String search, byte status, Pageable of);

    @Query(value = "select * from user where user_role=4 and status in(1,2) order by create_date desc", nativeQuery = true)
    List<User> findAllTraineesByPendingStatus();

    @Query(value = "select * from user where manager_id=?1 and user_role=4 and status in(1,2) and (first_name like concat(?2,'%') or last_name like concat(?2,'%'))", nativeQuery = true)
    List<User> findManagerResourcesTrainees(Integer managerId,String search, Pageable pageable);

    @Query(value = "select count(*) from user where manager_id=?1 and user_role=4 and status in(1,2) and (first_name like concat(?2,'%') or last_name like concat(?2,'%'))", nativeQuery = true)
    Integer countByManagerResourcesTrainees(Integer managerId,String search);

    @Query(value = "select * from user where user_role=4 and status in(1,2) and manager_id is null and (first_name like concat(?1,'%') or last_name like concat(?1,'%'))", nativeQuery = true)
    List<User> findUnassignedManagerResourcesTrainees(String search,Pageable of);

    @Query(value = "select count(*) from user where user_role=4 and status in(1,2) and manager_id is null and (first_name like concat(?1,'%') or last_name like concat(?1,'%'))", nativeQuery = true)
    Integer countUnassignedManagerResourcesTrainees(String search);

    @Query(value = "select * from user where mentor_id=?1 and user_role=4 and status in(1,2) and ((first_name  LIKE CONCAT(?2, '%') OR ?2 IS NULL)or (last_name  LIKE CONCAT(?2, '%') OR ?2 IS NULL))", nativeQuery = true)
    List<User> findMentorResourcesTrainees(Integer mentorId, String name, Pageable of);
    @Query(value = "select count(*) from user where mentor_id=?1 and user_role=4 and status in(1,2) and ((first_name  LIKE CONCAT(?2, '%') OR ?2 IS NULL)or (last_name  LIKE CONCAT(?2, '%') OR ?2 IS NULL))", nativeQuery = true)
    Integer findMentorResourcesTraineesCount(Integer mentorId, String name);

    @Query(value = "select * from user where user_role=4 and status in(1,2) and mentor_id is null and manager_id is not null and ((first_name  LIKE CONCAT(?1, '%') OR ?1 IS NULL)or (last_name  LIKE CONCAT(?1, '%') OR ?1 IS NULL))", nativeQuery = true)
    List<User> findUnassignedMentorResourcesTrainees(String name, Pageable of);

    @Query(value = "select count(*) from user where user_role=4 and status in(1,2) and mentor_id is null and ((first_name  LIKE CONCAT(?1, '%') OR ?1 IS NULL)or (last_name  LIKE CONCAT(?1, '%') OR ?1 IS NULL))", nativeQuery = true)
    Integer findUnassignedMentorResourcesTraineesCount(String name);

    @Query(value = "select * from user where employee_Id=?1 and (first_name like ?2%  or last_name like ?2%) and user_role=4 and status in(1,2,3)", nativeQuery = true)
    Page<User> findByEmployeeIdAndSearchParams(Long employeeId, String search, PageRequest of);

    @Query(value = "select * from user where employee_Id=?1 and user_role=4 and status in(1,2,3)", nativeQuery = true)
    Page<User> findByEmployeeIdParams(Long employeeId, PageRequest of);

    @Query(value = "select * from user where user_role=4 and (first_name like ?1%  or last_name like ?1%) and status in(1,2,3)", nativeQuery = true)
    Page<User> findBySearchParams(String search, PageRequest of);

    @Query(value = "select * from user where user_role=4 and status in(1,2,3) order by create_date desc", nativeQuery = true)
    Page<User> findAllTraineesInStatus(PageRequest of);

    @Query(value = "select count(*) from user where user_role=4 and status in(1,2)", nativeQuery = true)
    Integer findAllMentorCount();

    @Query(value = "select * from user where user_role=4 and status in(1,2)", nativeQuery = true)
    List<User> findAllMentor(PageRequest of);

}
