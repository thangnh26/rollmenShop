package com.example.shopGiay.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "Voucher")
public class Voucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDate createDate;

    private LocalDate updateDate;

    @NotNull(message = "Value không được để trống")
    @PositiveOrZero(message = "Value phải là số dương hoặc bằng không")
    private Double value;

    @NotNull(message = "Quantity không được để trống")
    @PositiveOrZero(message = "Quantity phải là số dương hoặc bằng không")
    private Integer quantity;

    @NotBlank(message = "Tên không được để trống")
    private String nameVoucher;

    @NotNull(message = "Ngày bắt đầu không được để trống")
    @Future(message = "Ngày bắt đầu phải là ngày trong tương lai")
    private LocalDate startDate;

    @NotNull(message = "Ngày kết thúc không được để trống")
    @Future(message = "Ngày kết thúc phải là ngày trong tương lai")
    private LocalDate endDate;

    @NotNull(message = "Trạng thái không được để trống")
    @PositiveOrZero(message = "Trạng thái phải là số dương hoặc bằng không")
    @Column(name = "status")
    private Integer status;
//    @Column(name = "condition", nullable = false)
//    private String condition;


    public Voucher() {
    }

    public Voucher(Integer id, LocalDate createDate, LocalDate updateDate, Double value, Integer quantity, String nameVoucher, LocalDate startDate, LocalDate endDate, Integer status) {
        this.id = id;
        this.createDate = createDate;
        this.updateDate = updateDate;
        this.value = value;
        this.quantity = quantity;
        this.nameVoucher = nameVoucher;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getNameVoucher() {
        return nameVoucher;
    }

    public void setNameVoucher(String nameVoucher) {
        this.nameVoucher = nameVoucher;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}


