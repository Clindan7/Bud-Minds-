/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.buddymanagement.user.reg.batch.exception;

/**
 *
 * @author ajmal
 */
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 *
 * @author nirmal
 */
public class BadRequestException extends ResponseStatusException {

    public BadRequestException() {
        super(BAD_REQUEST);
    }

    public BadRequestException(String reason) {
        super(BAD_REQUEST, reason);
    }

    public BadRequestException(String reason, Throwable cause) {
        super(BAD_REQUEST, reason, cause);
    }
}
