package com.example.shopGiay.service;
import com.example.shopGiay.model.Color;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface ColorService {
    Page<Color> findAll(Pageable pageable);
    Optional<Color> findById(Integer id);
    Color save(Color color);
    void deleteById(Integer id);
}
