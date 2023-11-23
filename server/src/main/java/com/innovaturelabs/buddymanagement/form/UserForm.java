/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.innovaturelabs.buddymanagement.form;

import com.innovaturelabs.buddymanagement.form.validaton.Password;

import javax.validation.constraints.*;

/**
 * @author nirmal
 */
public class UserForm {

    @NotBlank
    @Size(max = 50)
    private String firstName;

    @NotBlank
    @Size(max = 20)
    private String lastName;

    @NotBlank
    @Size(max = 255)
    @Email(message = ("{invalid.email.format}"),regexp = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
    private String email;

    @Positive(message = ("{employee.id.invalid}"))
    @Min(value = 1000,message = "1948-must be greater than or equal to 1000")
    @Max(value = 9999999999L,message = "1947-must be less than or equal to 9999999999")
    private Long employeeId;

    @Password
    private String password;

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

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}