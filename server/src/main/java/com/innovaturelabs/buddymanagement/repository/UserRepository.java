/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.innovaturelabs.buddymanagement.repository;

import com.innovaturelabs.buddymanagement.entity.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

/**
 * @author nirmal
 */
public interface UserRepository extends Repository<User, Integer> {

    Optional<User> findById(Integer userId);

    Optional<User> findByUserIdAndPassword(Integer userId, String password);

    Optional<User> findByEmail(String email);


    User save(User user);

    Collection<User> findAll();

    User findByUserIdAndStatus(Integer userId, byte value);

    User findByUserId(Integer userId);

    Optional<User> findByUserIdAndUserRole(Integer userId, byte userRole);

    Optional<User> findByEmployeeId(Long employeeId);

    @Query(value = "select count(*) from user where joiner_group_id=?1 and status=1", nativeQuery = true)
    Integer findByGroupActive(Integer joinerGroupId);

    @Query(value = "select * from user where user_role=?1 and status in(1,2) order by create_date desc", nativeQuery = true)
    List<User> findAllUserByRegisteredStatus(byte role);

    @Query(value = "select * from user where (manager_id = ?1 OR (?1 IS NULL AND manager_id IS NULL)) and user_role=2 and status=1 and (first_name like concat (?2,'%') or last_name like concat(?2,'%'))", nativeQuery = true)
    List<User> findAllByManagerIdUserIdAndUserRoleAndStatus(Integer managerId,String search, Pageable of);

    @Query(value = "select count(*) from user where (manager_id = ?1 OR (?1 IS NULL AND manager_id IS NULL)) and user_role=2 and status=1 and (first_name like concat (?2,'%') or last_name like concat(?2,'%'))", nativeQuery = true)
    Integer countByManagerIdUserIdAndUserRoleAndStatus(Integer managerId, String search);


    @Query(value = "select * from user where user_id=?1 and user_role=?2 and status in(1,2)", nativeQuery = true)
    Optional<User> findUsers(Integer userId, byte userRole);

    Optional<User> findByUserIdAndUserRoleAndStatus(Integer userId, byte userRole, byte status);


    @Query(value = "select * from user where joiner_group_id=?1 and (first_name like ?2%  or last_name like ?2%) and user_role=4 and status in(1,2)", nativeQuery = true)
    List<User> findGroupResourcesTrainees(Integer joinerGroupId,String search,Pageable pageable);

    @Query(value = "select count(*) from user where joiner_group_id=?1 and (first_name like ?2%  or last_name like ?2%) and user_role=4 and status in(1,2)", nativeQuery = true)
    Integer countByGroupResourcesTrainees(Integer joinerGroupId,String search);

    @Query(value = "select * from user where user_role=4 and status in(1,2) and joiner_group_id is null and (first_name like ?1%  or last_name like ?1%)", nativeQuery = true)
    List<User> findUnassignedGroupResourcesTrainees(String search,Pageable pageable);

    @Query(value = "select count(*) from user where user_role=4 and status in(1,2) and joiner_group_id is null and (first_name like ?1%  or last_name like ?1%)", nativeQuery = true)
    Integer countUnassignedGroupResourcesTrainees(String search);

    @Query(value = "select * from user where joiner_group_id=?1", nativeQuery = true)
    List<User> findByJoinerGroup(Integer groupId);

    @Query(value = "select * from user where joiner_group_id=?1", nativeQuery = true)
    List<User> findByGroup(Integer joinerGroupId);
}
