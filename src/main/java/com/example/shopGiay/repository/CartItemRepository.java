package com.example.shopGiay.repository;

import com.example.shopGiay.dto.AddCart;
import com.example.shopGiay.model.Cart;
import com.example.shopGiay.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem,Integer> {
    @Query(value = "SELECT sum(ci.quantity * pd.price) FROM cart_item ci join product_detail pd on ci.product_detail_id = pd.id", nativeQuery = true)
    BigDecimal total();

    @Query(value = "select sum(:quantity * pd.price) from product_detail pd where pd.id=:id",nativeQuery = true)
    BigDecimal totalByNow(int quantity, int id);

    @Query(value = "SELECT COUNT(*) FROM cart_item where product_detail_id = :productDetailId", nativeQuery = true)
    int checkSP(int productDetailId);

    @Query(value = "select c from CartItem c where c.productDetail.id = :productDetailId and c.cart.id = :cartId")
    CartItem getCartItemByProIdAndCartId(int productDetailId, int cartId);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO `dbshopgiay`.`cart_item` (`quantity`, `cart_id`, `product_detail_id`) VALUES (:#{#addCart.quantity}, :#{#addCart.cartId}, :#{#addCart.proId})", nativeQuery = true)
    void create(AddCart addCart);

    @Transactional
    @Modifying
    @Query(value = "UPDATE `dbshopgiay`.`cart_item` SET `quantity` = :quantity WHERE `cart_id`=:cartId and `product_detail_id`=:proId", nativeQuery = true)
    void updateQuantity(int quantity, int cartId, int proId);
//
//    @Transactional
//    @Modifying
//    @Query(value = "delete from `dbshopgiay`.`cart_item` where `cart_id`=:id", nativeQuery = true)
//    void clear(int id);

    @Transactional
    @Modifying
    @Query(value = "delete from `dbshopgiay`.`cart_item` where `cart_id`=:cartId", nativeQuery = true)
    void clearCartItem(int cartId);

    @Query(value = "select ci from CartItem ci where ci.cart.id = :cartId")
    List<CartItem> findAllByCartId(int cartId);

    @Query(value = "select ci from CartItem ci join Cart c on ci.cart.id = c.id where c.customer.id = :cusId")
    List<CartItem> finAllByUser(int cusId);

    @Query(value = "select c.id from Cart c where c.customer.id = :cusId")
    Integer getOneByCusId(int cusId);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO `dbshopgiay`.`cart` (`customer_id`, `is_check`) VALUES (:cusId, '0')", nativeQuery = true)
    void addCart(int cusId);

    @Transactional
    @Modifying
    @Query(value = "delete from `dbshopgiay`.`cart_item` where `product_detail_id`=:proId", nativeQuery = true)
    void deleteByIdPro(int proId);
}
