package com.example.shopGiay.repository;

import com.example.shopGiay.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Integer> {

    @Query(value = "select * from comment where product_id = :id",nativeQuery = true)
    Page<Comment> findAllByProductId(int id, Pageable pageable);
}
