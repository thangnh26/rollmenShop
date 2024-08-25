package com.example.shopGiay.security;

import com.example.shopGiay.service.AdminUserDetailService;
import com.example.shopGiay.service.CustomUserDetailsService;
import com.example.shopGiay.service.StaffUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }


    private final CustomUserDetailsService userDetailsService;

    private final StaffUserDetailsService staffUserDetailsService;


    private final AdminUserDetailService adminUserDetailService;

    public WebSecurityConfig(CustomUserDetailsService userDetailsService, StaffUserDetailsService staffUserDetailsService, AdminUserDetailService adminUserDetailService) {
        this.userDetailsService = userDetailsService;
        this.staffUserDetailsService = staffUserDetailsService;
        this.adminUserDetailService = adminUserDetailService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(staffUserDetailsService)
                .passwordEncoder(passwordEncoder());
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
        auth.userDetailsService(adminUserDetailService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/", "/product/*", "/process_register", "/register","/css/**", "/buy-now","/order").permitAll()
                .antMatchers("/admin/staff").hasRole("ADMIN")
                .antMatchers("/admin/index").hasRole("STAFF")
                .anyRequest().permitAll()
                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .successHandler(new CustomAuthenticationSuccessHandler())
                .and()
                .logout()
                .logoutSuccessUrl("/")
                .permitAll()
                .and()
                .csrf()
                .disable();
    }
}
