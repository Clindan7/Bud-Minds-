package com.innovaturelabs.buddymanagement.form.validaton;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.lang.annotation.Documented;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.*;

/**
 * @author nirmal
 */
@NotNull(message = "{password.required}")
@NotEmpty(message = "{password.required}")
//@Pattern(message = "{password.is.wrong}", regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])[a-zA-Z0-9 \\!\"#\\$%&'\\(\\)\\*\\+,\\-\\.\\/\\:;\\<\\=\\>\\?@\\[\\\\\\]\\^_`\\{\\|\\}~]+$", groups = Extended.class)
@Target({ANNOTATION_TYPE, FIELD, METHOD, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Constraint(validatedBy = {})
@Documented
public @interface LoginPassword {


    String message() default "{javax.validation.constraints.Pattern.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
