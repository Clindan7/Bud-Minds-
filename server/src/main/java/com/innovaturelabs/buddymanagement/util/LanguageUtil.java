/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.innovaturelabs.buddymanagement.util;

import java.util.Locale;

import com.innovaturelabs.buddymanagement.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

/**
 * @author Ajmal
 */
public class LanguageUtil {

    @Autowired
    private MessageSource messageSource;

    public String getTranslatedText(String msgKey, Object[] obj, String lang) {
        if(!lang.equals("en")){
            throw new BadRequestException(getTranslatedText("language.not.supported", null, "en"));
        }
        return messageSource.getMessage(msgKey, obj, Locale.ENGLISH);
    }
}
