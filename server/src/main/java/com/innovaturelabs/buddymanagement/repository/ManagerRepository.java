package com.innovaturelabs.buddymanagement.repository;

import com.innovaturelabs.buddymanagement.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface ManagerRepository extends JpaRepository<User, Integer> {

    @Query(value = "select * from user where user_role=1 and (first_name like ?1%  or last_name like ?1%) and status=?2 order by first_name,last_name", nativeQuery = true)
    Page<User> findBySearch(String search,byte status, PageRequest of);

    @Query(value = "select count(*) from user where user_role=1 and (first_name like ?1%  or last_name like ?1%) and status=?2", nativeQuery = true)
    Integer findBySearchCount(String search,byte status);

    @Query(value = "select * from user where user_role=1 and status=?1 order by create_date desc", nativeQuery = true)
    Page<User> findAllManager(byte status,Pageable paging);

    @Query(value = "select * from user where employee_Id=?1 and user_role=1 and status=?2", nativeQuery = true)
    Page<User> findByEmployeeId(Long employeeId,byte status,Pageable of);

    @Query(value = "select count(*) from user where employee_Id=?1 and user_role=1 and status=?2", nativeQuery = true)
    Integer findByEmployeeIdCount(Long employeeId,byte status);

    @Query(value="select first_name from user where user_role=1 and status in(1,2,3) order by create_date desc",nativeQuery = true)
    List<String> findByFirstName();

    @Query(value="select first_name from user where user_role=1 and status=?1 order by create_date desc",nativeQuery = true)
    List<String> findByFirstNameStatus(byte status);


    @Query(value = "select count(*) from user where manager_id=?1 and status in(1,2)",nativeQuery = true)
    Integer countInManager(Integer managerId);

    @Query(value = "select * from user where employee_Id=?1 and (first_name like ?2%  or last_name like ?2%) and user_role=1 and status=?3 order by first_name,last_name", nativeQuery = true)
    Page<User> findByEmployeeIdAndSearch(Long employeeId,String search,byte status,Pageable of);

    @Query(value = "select count(*) from user where employee_Id=?1 and (first_name like ?2%  or last_name like ?2%) and user_role=1 and status=?3", nativeQuery = true)
    Integer findByEmployeeIdAndSearchCount(Long employeeId,String search,byte status);

    @Query(value = "select * from user where user_role=1 and status in(1,2,3) order by create_date desc", nativeQuery = true)
    Page<User> findAllManagerInStatus(Pageable of);

    @Query(value = "select * from user where employee_Id=?1 and (first_name like ?2%  or last_name like ?2%) and user_role=1 and status in(1,2,3) order by first_name,last_name", nativeQuery = true)
    Page<User> findByEmployeeIdAndSearchParams(Long employeeId, String search, PageRequest of);

    @Query(value = "select count(*) from user where employee_Id=?1 and (first_name like ?2%  or last_name like ?2%) and user_role=1 and status in(1,2,3)", nativeQuery = true)
    Integer findByEmployeeIdAndSearchParamsCount(Long employeeId, String search);

    @Query(value = "select * from user where employee_Id=?1 and user_role=1 and status in(1,2,3)", nativeQuery = true)
    Page<User> findByEmployeeIdParams(Long employeeId, PageRequest of);

    @Query(value = "select count(*) from user where employee_Id=?1 and user_role=1 and status in(1,2,3)", nativeQuery = true)
    Integer findByEmployeeIdParamsCount(Long employeeId);


    @Query(value = "select * from user where user_role=1 and (first_name like ?1%  or last_name like ?1%) and status in(1,2,3) order by first_name,last_name", nativeQuery = true)
    Page<User> findBySearchParams(String search, PageRequest of);

    @Query(value = "select count(*) from user where user_role=1 and (first_name like ?1%  or last_name like ?1%) and status in(1,2,3)", nativeQuery = true)
    Integer findBySearchParamsCount(String search);

}
