/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.innovaturelabs.buddymanagement.view;

public class ResponseView {

    private String errorCode;
    private String errorMessage;

    public ResponseView(String errorMessage, String errorCode) {

        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public ResponseView() {

        this.errorCode = "0";
        this.errorMessage = "";
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
