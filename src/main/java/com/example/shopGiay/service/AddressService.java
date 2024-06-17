package com.example.shopGiay.service;
import com.example.shopGiay.model.Address;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
public interface AddressService {
    Page<Address> getAllAddresses(Pageable pageable);
    Optional<Address> getAddressById(Integer id);
    Address saveAddress(Address address);
    void deleteAddress(Integer id);
}
