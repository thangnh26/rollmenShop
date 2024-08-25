package com.example.shopGiay.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());

        if (roles.contains("ROLE_STAFF")) {
            response.sendRedirect("/admin/index");
        }else if (roles.contains("ROLE_ADMIN")) {
            response.sendRedirect("/admin/staff");
        } else if (roles.contains("CUSTOMER")) {
            response.sendRedirect("/");
        } else {
            response.sendRedirect("/login?error");
        }
    }
}
