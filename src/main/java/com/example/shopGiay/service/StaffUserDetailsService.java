package com.example.shopGiay.service;

import com.example.shopGiay.model.Staff;
import com.example.shopGiay.repository.StaffRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StaffUserDetailsService implements UserDetailsService {
    private final StaffRepository staffRepository;

    public StaffUserDetailsService(StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Staff staff = staffRepository.findByEmail(username);
        if (staff == null) {
            throw new UsernameNotFoundException("User not found");
        }
        Collection<SimpleGrantedAuthority> authorities = staff.getRoles().stream()
                .map(role -> {
                    String authority = "ROLE_" + role.getAuthority(); // Prefix with ROLE_
                    System.out.println("Authority: " + authority); // Debug log
                    return new SimpleGrantedAuthority(authority);
                })
                .collect(Collectors.toList());
        return new org.springframework.security.core.userdetails.User(
                staff.getEmail(),
                staff.getPassword(),
                authorities

        );
    }
}
