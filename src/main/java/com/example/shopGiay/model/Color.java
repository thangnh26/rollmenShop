package com.example.shopGiay.model;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "Color")
public class Color {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false)
    @NotBlank(message = "Tên không được để trống")
    private String name;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "create_date")
    private LocalDate createDate;

    @Column(name = "update_date")
    private LocalDate updateDate;

    @Column(name = "status", nullable = false)
    private Integer status;
    @PrePersist
    public void prePersist() {
        if (this.code == null || this.code.isEmpty()) {
            this.code = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        }
        this.createDate = LocalDate.now();
    }

    // Constructors
    public Color() {}

    public Color(String name, String code, LocalDate createDate, LocalDate updateDate, Integer status) {
        this.name = name;
        this.code = code;
        this.createDate = createDate;
        this.updateDate = updateDate;
        this.status = status;
    }

    // Getters and Setters
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
