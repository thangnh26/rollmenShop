package com.example.shopGiay.service.impl;

import com.example.shopGiay.model.Comment;
import com.example.shopGiay.model.Product;
import com.example.shopGiay.model.User;
import com.example.shopGiay.repository.CommentRepository;
import com.example.shopGiay.repository.ProductRepository;
import com.example.shopGiay.repository.UserRepository;
import com.example.shopGiay.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public Page<Comment> findAllByProductId(int id, Pageable pageable) {
        return commentRepository.findAllByProductId(id, pageable);
    }

    @Override
    public Comment createComment(int productId, int userId, String content) {
        Comment comment = new Comment();
        Product product = productRepository.getById(productId);
        User user = userRepository.getById(userId);
//        comment.setCreatedAt(new Timestamp(System.currentTimeMillis()));
//        comment.setProduct(product);
//        comment.setUser(user);
        comment.setContent(content);

        commentRepository.save(comment);
        return comment;
    }
}
