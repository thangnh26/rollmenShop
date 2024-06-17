package com.example.shopGiay.service.impl;

import com.example.shopGiay.model.Color;
import com.example.shopGiay.repository.ColorRepository;
import com.example.shopGiay.service.ColorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ColorServiceImpl implements ColorService {
    @Autowired
    private ColorRepository colorRepository;

    @Override
    public Page<Color> findAll(Pageable pageable) {
        return colorRepository.findAll(pageable);
    }

    @Override
    public Optional<Color> findById(Integer id) {
        return colorRepository.findById(id);
    }

    @Override
    public Color save(Color color) {
        return colorRepository.save(color);
    }

    @Override
    public void deleteById(Integer id) {
        colorRepository.deleteById(id);
    }
}
