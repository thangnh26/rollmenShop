package com.example.shopGiay.service;

import com.example.shopGiay.model.Color;
import com.example.shopGiay.model.Material;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ColorService {
    List<Color> getAllColors();
    Color getColorById(Integer id);
    Color saveColor(Color color);
    void deleteColorById(Integer id);
    Page<Color> getColorByStatusNot2(Pageable pageable);
    Page<Color> searchColorsByName(String name, Pageable pageable);
}
