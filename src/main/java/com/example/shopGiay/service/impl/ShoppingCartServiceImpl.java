package com.example.shopGiay.service.impl;

import com.example.shopGiay.dto.AddCart;
import com.example.shopGiay.dto.CartRequesst;
import com.example.shopGiay.dto.OrderRequest;
import com.example.shopGiay.dto.Result;
import com.example.shopGiay.model.Cart;
import com.example.shopGiay.model.CartItem;
import com.example.shopGiay.model.Customer;
import com.example.shopGiay.model.ProductDetail;
import com.example.shopGiay.repository.*;
import com.example.shopGiay.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@SessionScope
public class ShoppingCartServiceImpl implements ShoppingCartService {

    Map<Integer, CartItem> maps = new HashMap<Integer, CartItem>();

    @Autowired
    ProductRepository productRepository;
    @Autowired
    ProductDetailRepository productDetailRepository;
    @Autowired
    SizeRepository sizeRepository;

    @Autowired
    CartItemRepository cartItemRepository;
    private final UserRepository userRepo;
    private final CartRepository cartRepository;

    public ShoppingCartServiceImpl(UserRepository userRepo, CartRepository cartRepository) {
        this.userRepo = userRepo;
        this.cartRepository = cartRepository;
    }

    //    @Autowired
//    ProductSizeRepository productSizeRepository;
    @Override
    public Result add(AddCart addCart, int cusId) {
        if (cartItemRepository.checkSP(productDetailRepository.getOneProductDetail(addCart.getProId(), addCart.getColorId(), addCart.getSizeId()).getId()) > 0) {
            if(cartItemRepository.getOneByCusId(cusId)==null){
                cartItemRepository.addCart(cusId);
                return null;
            }else {
                CartItem cartItem = cartItemRepository.getCartItemByProIdAndCartId(productDetailRepository.getOneProductDetail(addCart.getProId(), addCart.getColorId(), addCart.getSizeId()).getId(), cartItemRepository.getOneByCusId(cusId));
                if (addCart.getQuantity() > productDetailRepository.checkQuantity(productDetailRepository.getOneProductDetail(addCart.getProId(), addCart.getColorId(), addCart.getSizeId()).getId())) {
                    return new Result(false, "Không đủ số lượng");
                } else {
                    cartItemRepository.updateQuantity(addCart.getQuantity() + cartItem.getQuantity(), cartItem.getCart().getId(), cartItem.getProductDetail().getId());
                    return new Result(true, "Cart item quantity updated successfully");
                }
            }
        } else {
            if (addCart.getQuantity() > productDetailRepository.checkQuantity(productDetailRepository.getOneProductDetail(addCart.getProId(), addCart.getColorId(), addCart.getSizeId()).getId())) {
                return new Result(false, "Không đủ số lượng");
            } else {
                if(cartItemRepository.getOneByCusId(cusId)==null){
                    cartItemRepository.addCart(cusId);
                    int cartId = cartItemRepository.getOneByCusId(cusId);
                    addCart.setCartId(cartId);
                    int proDetailid = productDetailRepository.getOneProductDetail(addCart.getProId(),addCart.getColorId(),addCart.getSizeId()).getId();

                    CartRequesst cartRequesst = new CartRequesst();
                    cartRequesst.setCartId(cartId);
                    cartRequesst.setQuantity(addCart.getQuantity());
                    cartRequesst.setProId(proDetailid);
                    cartItemRepository.create(cartRequesst);
                    return new Result(true, "New cart item added successfully");
//                    return null;
                }
                else {
                    int cartId = cartItemRepository.getOneByCusId(cusId);
                    int proDetailid = productDetailRepository.getOneProductDetail(addCart.getProId(),addCart.getColorId(),addCart.getSizeId()).getId();
                    addCart.setCartId(cartId);
                    CartRequesst cartRequesst = new CartRequesst();
                    cartRequesst.setCartId(cartId);
                    cartRequesst.setQuantity(addCart.getQuantity());
                    cartRequesst.setProId(proDetailid);
                    cartItemRepository.create(cartRequesst);
                    return new Result(true, "New cart item added successfully");
                }
            }
        }
    }

    @Override
    public void buyNow(OrderRequest orderRequest) {

    }


    @Override
    public void remove(int id, int size) {
        maps.remove(size);
    }

    @Override
    public CartItem update(int productId, int quantity, int size) {
//        CartItem cartItem = maps.get(productId);
//        CartItem cartItem1 = maps.getOrDefault(size,cartItem);
//        Size size1 = sizeRepository.getByName(size);
//        Product_size product_size = productSizeRepository.findByProductIdAndSizeId(productId,size1.getId());
//
//        int qty = product_size.getQuantity();
//        if( qty >= quantity){
//            cartItem1.setQuantity(quantity);
//        }else {
//            cartItem1.setQuantity(1);
//        }

        return null;
    }

    @Override
    public void clear(int id) {
        cartItemRepository.clearCartItem(id);
//        cartItemRepository.clear(id);
    }
    @Override
    public List<CartItem> getAllItems(int cusId) {
        return cartItemRepository.finAllByUser(cusId);
    }

    @Override
    public List<CartItem> getAllItemsByCartId(int cartId) {
        return cartItemRepository.findAllByCartId(cartId);
    }

    @Override
    public int getCount() {
        return maps.values().size();
    }

    @Override
    public BigDecimal getAmount() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = null;
        if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername();
        } else if (principal instanceof String) {
            email = (String) principal;
        }

        // Lấy User từ email
        Customer user = userRepo.findByEmail(email);
        Cart cart = cartRepository.getOneByCusId(user.getId());
        return cartItemRepository.total(cart.getId());
    }

    @Override
    public void deletePro(int proId) {
        cartItemRepository.deleteByIdPro(proId);
    }

    @Override
    public void updateCartItem(int cartId,int productId, int colorId, int sizeId, int quantity) {

        ProductDetail productDetail = productDetailRepository.getOneProductDetail(productId,colorId,sizeId);
        // Fetch the cart item from the database based on productId, colorId, and sizeId
        CartItem cartItem = cartItemRepository.findByProductIdAndColorIdAndSizeId(productDetail.getId(), cartId)
                .orElseThrow(() -> new IllegalArgumentException("Cart item not found"));

        // Update the quantity
        cartItem.setQuantity(quantity);
        if (productDetail != cartItem.getProductDetail()){
            cartItem.setProductDetail(productDetail);
            cartItemRepository.save(cartItem);
        }
//        cartItem.setProductDetail(productDetail);

        // Save the updated cart item
        cartItemRepository.save(cartItem);
    }
}
