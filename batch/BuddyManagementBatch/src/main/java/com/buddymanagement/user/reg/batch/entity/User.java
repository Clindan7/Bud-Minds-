/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.buddymanagement.user.reg.batch.entity;

import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.*;

/**
 * @author nirmal
 */
@Entity
public class User {
 public enum Status {
        INACTIVE((byte) 0),
        ACTIVE((byte) 1),
        REGISTERED((byte) 2),
        PENDING((byte) 3);
        public final byte value;

        private Status(byte value) {
            this.value = value;
        }
    }

    public enum Department {
        DEVELOPMENT((byte) 1),
        QA((byte) 2);

        public final byte value;

        private Department(byte value) {
            this.value = value;
        }
    }

    public enum Role {
        ADMIN((byte) 0),
        MANAGER((byte) 1),
        MENTOR((byte) 2),
        TRAINER((byte) 3),
        TRAINEE((byte) 4);

        public final byte value;

        private Role(byte value) {
            this.value = value;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Long employeeId;
    private byte userRole;
    private Integer managerId;
    private Integer mentorId;

    private Integer joinerGroupId;
    private byte department;
    private byte status;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    public User() {
    }

    public User(Integer userId) {
        this.userId = userId;
    }

    public User(String firstName, String lastName, String email, Long employeeId, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.employeeId = employeeId;
        this.password = password;
        this.status = Status.REGISTERED.value;
        this.department = Department.DEVELOPMENT.value;
        LocalDateTime dt = LocalDateTime.now();
        this.createDate = dt;
        this.updateDate = dt;
    }

    public User(String firstName, String lastName, String email, Long employeeId, byte department) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.employeeId = employeeId;
        this.department = department;
        this.userRole = Role.MANAGER.value;
        this.status = Status.PENDING.value;
        LocalDateTime dt = LocalDateTime.now();
        this.createDate = dt;
        this.updateDate = dt;
    }

    public User(String firstName, String lastName, String email, Long employeeId, String password, byte userRole, byte department) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.employeeId = employeeId;
        this.password = password;
        this.status = Status.REGISTERED.value;
        this.department = department;
        this.userRole = userRole;
        LocalDateTime dt = LocalDateTime.now();
        this.createDate = dt;
        this.updateDate = dt;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public byte getUserRole() {
        return userRole;
    }

    public void setUserRole(byte userRole) {
        this.userRole = userRole;
    }

    public Integer getManagerId() {
        return managerId;
    }

    public void setManagerId(Integer managerId) {
        this.managerId = managerId;
    }

    public Integer getMentorId() {
        return mentorId;
    }

    public void setMentorId(Integer mentorId) {
        this.mentorId = mentorId;
    }

    public Integer getJoinerGroupId() {
        return joinerGroupId;
    }

    public void setJoinerGroupId(Integer joinerGroupId) {
        this.joinerGroupId = joinerGroupId;
    }


    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userId != null ? userId.hashCode() : 0);
        return hash;
    }

    public byte getDepartment() {
        return department;
    }

    public void setDepartment(byte department) {
        this.department = department;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
        return Objects.equals(this.userId, other.userId);
    }

    @Override
    public String toString() {
        return "com.innovaturelabs.buddymanagement.entity.User[ userId=" + userId + " ]";
    }


}
