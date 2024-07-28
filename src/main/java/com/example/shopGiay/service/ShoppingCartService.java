package com.example.shopGiay.service;


import com.example.shopGiay.dto.AddCart;
import com.example.shopGiay.dto.Result;
import com.example.shopGiay.model.CartItem;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

public interface ShoppingCartService {
    Result add(AddCart addCart);

    void remove(int id, int size);

    CartItem update(int productId, int quantity, int size);

    void clear(int id);

    List<CartItem> getAllItems();

    List<CartItem> getAllItemsByCartId(int cartId);


    int getCount();

    BigDecimal getAmount();
}
