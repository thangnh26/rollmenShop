package com.example.shopGiay.service.impl;

import com.example.shopGiay.model.Address;
import com.example.shopGiay.repository.AddressRepository;
import com.example.shopGiay.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Override
    public Page<Address> getAllAddresses(Pageable pageable) {
        return addressRepository.findAll(pageable);
    }

    @Override
    public Optional<Address> getAddressById(Integer id) {
        return addressRepository.findById(id);
    }

    @Override
    public Address saveAddress(Address address) {
        return addressRepository.save(address);
    }

    @Override
    public void deleteAddress(Integer id) {
        addressRepository.deleteById(id);
    }
}