package com.example.shopGiay.service;


import com.example.shopGiay.dto.AddCart;
import com.example.shopGiay.dto.OrderRequest;
import com.example.shopGiay.dto.Result;
import com.example.shopGiay.model.CartItem;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

public interface ShoppingCartService {
    Result add(AddCart addCart, int cusId);

    void buyNow(OrderRequest orderRequest);

    void remove(int id, int size);

    CartItem update(int productId, int quantity, int size);

    void clear(int id);

    List<CartItem> getAllItems(int cusId);

    List<CartItem> getAllItemsByCartId(int cartId);


    int getCount();

    BigDecimal getAmount();

    void deletePro(int proId);

    void updateCartItem(int cartId,int productId, int colorId, int sizeId, int quantity);
}
