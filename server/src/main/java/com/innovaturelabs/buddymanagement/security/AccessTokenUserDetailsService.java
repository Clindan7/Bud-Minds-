/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.innovaturelabs.buddymanagement.security;

import com.innovaturelabs.buddymanagement.entity.User;
import com.innovaturelabs.buddymanagement.exception.NotFoundException;
import com.innovaturelabs.buddymanagement.repository.UserRepository;
import com.innovaturelabs.buddymanagement.security.util.InvalidTokenException;
import com.innovaturelabs.buddymanagement.security.util.TokenExpiredException;
import com.innovaturelabs.buddymanagement.security.util.TokenGenerator;
import com.innovaturelabs.buddymanagement.security.util.TokenGenerator.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

/**
 * @author nirmal
 */
public class AccessTokenUserDetailsService implements AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {

    public static final String PURPOSE_ACCESS_TOKEN = "ACCESS_TOKEN";

    @Autowired
    private TokenGenerator tokenGenerator;
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken token) throws UsernameNotFoundException {
        if (!PURPOSE_ACCESS_TOKEN.equals(token.getCredentials())) {
            throw new UsernameNotFoundException("Invalid credentials");
        }

        final Status status;
        User user = new User();
        try {
            status = tokenGenerator.verify(PURPOSE_ACCESS_TOKEN, token.getPrincipal().toString());
            user = userRepository.findById(Integer.valueOf(status.data)).orElseThrow(NotFoundException::new);
            if ((user.getStatus() != (User.Status.ACTIVE.value)) && user.getStatus() != (User.Status.REGISTERED.value)) {
                throw new UsernameNotFoundException("User not found");
            }
        } catch (InvalidTokenException e) {
            throw new UsernameNotFoundException("Invalid access token", e);
        } catch (TokenExpiredException e) {
            throw new UsernameNotFoundException("Access token expired", e);
        }

        return new AccessTokenUserDetails(Integer.parseInt(status.data), user.getUserRole());
    }
}
