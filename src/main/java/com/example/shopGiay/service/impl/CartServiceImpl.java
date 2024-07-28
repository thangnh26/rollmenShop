package com.example.shopGiay.service.impl;

import com.example.shopGiay.model.Cart;
import com.example.shopGiay.repository.CartRepository;
import com.example.shopGiay.service.CartService;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;

    public CartServiceImpl(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }


    @Override
    public Cart getOneByUserId(int cusId) {
        return cartRepository.getOneByCusId(cusId);
    }
}
