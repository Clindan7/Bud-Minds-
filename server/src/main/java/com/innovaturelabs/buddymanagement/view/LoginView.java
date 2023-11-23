/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.innovaturelabs.buddymanagement.view;

import com.innovaturelabs.buddymanagement.entity.User;
import com.innovaturelabs.buddymanagement.json.Json;
import com.innovaturelabs.buddymanagement.security.util.TokenGenerator.Token;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * @author nirmal
 */
public class LoginView extends UserView {

    public static class TokenView {

        private final String value;
        @Json.DateTimeFormat
        private final LocalDateTime expiry;

        public TokenView(Token token) {
            this.value = token.value;
            this.expiry=Instant.ofEpochMilli(token.expiry).atZone(ZoneId.systemDefault()).toLocalDateTime();
        }

        public TokenView(String value, long expiry) {
            this.value = value;
            this.expiry=Instant.ofEpochMilli(expiry).atZone(ZoneId.systemDefault()).toLocalDateTime();
        }

        public String getValue() {
            return value;
        }

        public LocalDateTime getExpiry() {
            return expiry;
        }
    }

    private final TokenView accessToken;
    private final TokenView refreshToken;

    public LoginView(User user, Token accessToken, Token refreshToken) {
        super(user);
        this.accessToken = new TokenView(accessToken);
        this.refreshToken = new TokenView(refreshToken);
    }

    public LoginView(User user, TokenView accessToken, TokenView refreshToken) {
        super(user);
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public TokenView getAccessToken() {
        return accessToken;
    }

    public TokenView getRefreshToken() {
        return refreshToken;
    }
}
