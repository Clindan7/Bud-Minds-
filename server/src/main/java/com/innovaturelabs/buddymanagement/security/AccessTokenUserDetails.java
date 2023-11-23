/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.innovaturelabs.buddymanagement.security;

import com.innovaturelabs.buddymanagement.exception.NotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 *
 * @author nirmal
 */
public class AccessTokenUserDetails implements UserDetails {

    public final int userId;
    String userRole ="";

    public AccessTokenUserDetails(int userId, short role) {

        switch (role) {
            case 0:
                userRole = "ADMIN";
                break;
            case 1:
                userRole = "MANAGER";

                break;
            case 2:
                userRole = "MENTOR";

                break;
            case 3:
                userRole = "TRAINER";

                break;
            case 4:
                userRole = "TRAINEE";

                break;
            default:
                throw new NotFoundException("User role not found");
        }

        this.userId = userId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String roleRolex = "ROLE_";
        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(roleRolex + userRole));
        return roles;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
