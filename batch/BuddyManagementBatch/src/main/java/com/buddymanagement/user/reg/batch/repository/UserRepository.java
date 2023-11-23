/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.buddymanagement.user.reg.batch.repository;


import com.buddymanagement.user.reg.batch.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


/**
 * @author nirmal
 */
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query(value="select email from user where status = 3 order by create_date desc",nativeQuery = true)
    List<String> findAllEmailByPendingStatus();
    User findByEmail(String email);

   
}
