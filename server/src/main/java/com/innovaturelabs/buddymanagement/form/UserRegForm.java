/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.innovaturelabs.buddymanagement.form;

import com.innovaturelabs.buddymanagement.form.validaton.Password;

import javax.validation.constraints.*;

/**
 * @author ajmal
 */
public class UserRegForm {

    @Size(max = 50)
    @NotBlank
    private String firstName;

    @Size(max = 20)
    @NotBlank
    private String lastName;

    @Positive(message = ("{employee.id.invalid}"))
    @Min(value = 1000,message = "1948-must be greater than or equal to 1000")
    @Max(value = 9999999999L,message = "1947-must be less than or equal to 9999999999")
    private Long employeeId;

    @Password
    private String password;

    @NotBlank
    @Size(max = 255)
    @Email(message = ("{invalid.email.format}"),regexp = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
    private String email;

    @NotNull
    private byte userRole;

    private Integer managerId;

    private Integer mentorId;

    private Integer groupId;

    public byte getUserRole() {
        return userRole;
    }

    public void setUserRole(byte userRole) {
        this.userRole = userRole;
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

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }
}
