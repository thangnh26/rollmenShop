package com.example.shopGiay.model;

import jakarta.persistence.*;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Entity
@Table(name = "Color")
public class Color {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false)
    @NotBlank(message = "Tên không được để trống")
    private String name;
    @NotBlank(message = "Code không được để trống")
    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "create_date")
    private LocalDate createDate;

    @Column(name = "update_date")
    private LocalDate updateDate;

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Column(name = "status", nullable = false)
    private Integer status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDate getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public LocalDate getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDate updateDate) {
        this.updateDate = updateDate;
    }
}

