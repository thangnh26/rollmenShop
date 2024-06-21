package com.example.shopGiay.service.impl;

import com.example.shopGiay.model.Customer;
import com.example.shopGiay.repository.CustomerRepository;
import com.example.shopGiay.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    CustomerRepository customerRepository;

    @Override
    public List<Customer> getAllCustomer() {
        return customerRepository.findAll();
    }

    @Override
    public Customer getCustomerById(Integer id) {
        return customerRepository.findById(id).orElse(null);
    }

    @Override
    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public void deleteCustomerById(Integer id) {
        customerRepository.deleteById(id);
    }

    @Override
    public Page<Customer> getCustomerByStatusNot2(Pageable pageable) {
        return customerRepository.findByStatusNot(2, pageable);
    }

    @Override
    public Page<Customer> searchCustomerByCode(String keyword, Pageable pageable) {
        return customerRepository.findByCodeContainingIgnoreCaseAndStatusNot(keyword, 2, pageable);
    }
}
