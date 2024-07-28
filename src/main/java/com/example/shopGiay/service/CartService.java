package com.example.shopGiay.service;

import com.example.shopGiay.model.Cart;

public interface CartService {
    Cart getOneByUserId(int cusId);
}
