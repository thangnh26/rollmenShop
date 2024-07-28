package com.example.shopGiay.security;

import com.example.shopGiay.model.User;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {
    public static User getPrincipal() {
        return (User) (SecurityContextHolder.getContext()).getAuthentication().getPrincipal();
    }
}
