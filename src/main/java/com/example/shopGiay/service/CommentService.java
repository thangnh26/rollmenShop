package com.example.shopGiay.service;

import com.example.shopGiay.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentService {
    Page<Comment> findAllByProductId(int id, Pageable pageable);

    Comment createComment(int productId, int userId, String content);
}
