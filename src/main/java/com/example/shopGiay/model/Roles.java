package com.example.shopGiay.model;


import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "roles")
@Data
public class Roles implements GrantedAuthority {
    @Id
    @Column(name = "role_id")
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;


    public Integer getId(){
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getAuthority() {
        return name;
    }
}


