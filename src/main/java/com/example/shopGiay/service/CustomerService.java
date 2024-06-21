package com.example.shopGiay.service;

import com.example.shopGiay.model.Category;
import com.example.shopGiay.model.Customer;
import com.example.shopGiay.model.Material;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


public interface CustomerService {
    List<Customer> getAllCustomer();
    Customer getCustomerById(Integer id);
    Customer saveCustomer(Customer customer);
    void deleteCustomerById(Integer id);
    public Page<Customer> getCustomerByStatusNot2(Pageable pageable);
    public Page<Customer> searchCustomerByCode(String keyword, Pageable pageable);
}
