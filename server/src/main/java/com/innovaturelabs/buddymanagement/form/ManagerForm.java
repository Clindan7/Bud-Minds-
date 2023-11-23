package com.innovaturelabs.buddymanagement.form;

import javax.validation.constraints.*;

public class ManagerForm {

    private static final String NAME_REGEX="^[a-zA-Z]+([\\s][a-zA-Z]+)*+$";

    @NotBlank(message = "{first.name.required}")
    @Size(message = "{size.first.name}", min = 1, max = 50)
    @Pattern(message = "{invalid.first.name}", regexp = NAME_REGEX)
    private String firstName;

    @NotBlank(message = "{last.name.required}")
    @Size(message = "{size.last.name}", min = 1, max = 20)
    @Pattern(message = "{invalid.last.name}", regexp = NAME_REGEX)
    private String lastName;

    @Email(message = ("{invalid.email.format}"),regexp = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
    @NotBlank(message = ("{email.is.required}"))
    @Size(max = 255)
    private String email;

    @NotNull(message = ("{employee.id.required}"))
    @Positive(message = ("{employee.id.invalid}"))
    @Min(value = 1000,message = "1948-must be greater than or equal to 1000")
    @Max(value = 9999999999L,message = "1947-must be less than or equal to 9999999999")
    private Long employeeId;

    @Min(value = 1,message = ("{invalid.department}"))
    private byte department;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public byte getDepartment() {
        return department;
    }

    public void setDepartment(byte department) {
        this.department = department;
    }
}