package com.innovaturelabs.buddymanagement.repository;

import com.innovaturelabs.buddymanagement.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TrainerRepository extends JpaRepository<User,Integer> {
    @Query(value = "select * from user where user_role=3 and (first_name like ?1%  or last_name like ?1%) and status=?2", nativeQuery = true)
    Page<User> findBySearch(String search,byte status, PageRequest of);

    @Query(value = "select * from user where user_role=3 and status=?1 order by create_date desc", nativeQuery = true)
    Page<User> findAllTrainer(byte status,Pageable paging);

    @Query(value = "select * from user where employee_Id=?1 and user_role=3 and status=?2", nativeQuery = true)
    Page<User> findByEmployeeId(Long employeeId,byte status,Pageable of);

    @Query(value="select first_name from user where user_role=3 and status in(1,2,3) order by create_date desc",nativeQuery = true)
    List<String> findByFirstName();

    @Query(value="select first_name from user where user_role=3 and status=?1 order by create_date desc",nativeQuery = true)
    List<String> findByFirstNameStatus(byte status);

    @Query(value = "select * from user where employee_Id=?1 and (first_name like ?2%  or last_name like ?2%) and user_role=3 and status=?3", nativeQuery = true)
    Page<User> findByEmployeeIdAndSearch(Long employeeId,String search,byte status, Pageable of);

    @Query(value = "select * from user where user_role=3 and status in(1,2,3) order by create_date desc", nativeQuery = true)
    Page<User> findAllTrainerInStatus(Pageable of);

    @Query(value = "select * from user where employee_Id=?1 and (first_name like ?2%  or last_name like ?2%) and user_role=3 and status in(1,2,3)", nativeQuery = true)
    Page<User> findByEmployeeIdAndSearchParams(Long employeeId, String search, PageRequest of);

    @Query(value = "select * from user where employee_Id=?1 and user_role=3 and status in(1,2,3)", nativeQuery = true)
    Page<User> findByEmployeeIdParams(Long employeeId, PageRequest of);

    @Query(value = "select * from user where user_role=3 and (first_name like ?1%  or last_name like ?1%) and status in(1,2,3)", nativeQuery = true)
    Page<User> findBySearchParams(String search, PageRequest of);

    Optional<User> findByUserIdAndStatusAndUserRoleIn(Integer trainerId,Byte status,List<Byte> role);

    @Query(value="select * from user where user_role in (2,3) and status in(1) order by first_name",nativeQuery = true)
    List<User> findByAllTrainerName();
}
