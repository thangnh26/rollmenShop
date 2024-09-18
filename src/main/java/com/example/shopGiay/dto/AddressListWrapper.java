package com.example.shopGiay.dto;

import com.example.shopGiay.model.Address;

import java.util.ArrayList;
import java.util.List;

public class AddressListWrapper {
    private List<Address> addresses;

    public AddressListWrapper() {
        this.addresses = new ArrayList<>();
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }
}