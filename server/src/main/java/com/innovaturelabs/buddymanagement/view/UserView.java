/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.innovaturelabs.buddymanagement.view;

import com.innovaturelabs.buddymanagement.entity.User;
import com.innovaturelabs.buddymanagement.json.Json;

import java.time.LocalDateTime;

/**
 * @author nirmal
 */
public class UserView {

    private final int userId;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final Long employeeId;
    private final short status;
    private final byte userRole;

    private final byte department;


    @Json.DateTimeFormat
    private final LocalDateTime createDate;
    @Json.DateTimeFormat
    private final LocalDateTime updateDate;

    public UserView(User user) {
        this.userId = user.getUserId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.status = user.getStatus();
        this.createDate = user.getCreateDate();
        this.updateDate = user.getUpdateDate();
        this.userRole = user.getUserRole();
        this.employeeId = user.getEmployeeId();
        this.department = user.getDepartment();
    }


    public int getUserId() {
        return userId;
    }


    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public short getStatus() {
        return status;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public byte getUserRole() {
        return userRole;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public byte getDepartment() {
        return department;
    }

}
