package com.example.shopGiay.model;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.time.LocalDate;
@Entity
@Table(name = "Size")
public class Size {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "size_number", nullable = false)
    @NotNull(message = "Kích thước không được để trống")
    @Min(value = 1, message = "Kích thước phải là một số nguyên dương")
    private Integer sizeNumber;

    @Column(name = "create_date")
    private LocalDate createDate;

    @Column(name = "update_date")
    private LocalDate updateDate;

    @Column(name = "status", nullable = false)
    private Integer status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSizeNumber() {
        return sizeNumber;
    }

    public void setSizeNumber(Integer sizeNumber) {
        this.sizeNumber = sizeNumber;
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
