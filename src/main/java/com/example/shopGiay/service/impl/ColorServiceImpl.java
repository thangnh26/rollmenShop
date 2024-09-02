package com.example.shopGiay.service.impl;

import com.example.shopGiay.model.Color;
import com.example.shopGiay.repository.ColorRepository;
import com.example.shopGiay.service.ColorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ColorServiceImpl implements ColorService {
    @Autowired
    private ColorRepository colorRepository;

    @Override
    public List<Color> getAllColors() {
        return colorRepository.findAll();
    }

    @Override
    public Color getColorById(Integer id) {
        return colorRepository.findById(id).orElse(null);
    }

    @Override
    public Color saveColor(Color color) {
        if (color.getId() == null) {
            color.setCreateDate(LocalDate.now());
        } else {
            color.setUpdateDate(LocalDate.now());
        }
        return colorRepository.save(color);
    }

    @Override
    public void deleteColorById(Integer id) {
        colorRepository.deleteById(id);
    }

    @Override
    public Page<Color> getColorByStatusNot2(Pageable pageable) {
        return colorRepository.findByStatusNot(2,pageable);
    }

    @Override
    public Page<Color> searchColorsByName(String name, Pageable pageable) {
        return colorRepository.findByNameContainingIgnoreCase(name, pageable);
    }

    @Override
    public boolean existsByName(String name)
    {
        return colorRepository.existsByName(name);
    }

    @Override
    public boolean existsByNameAndIdNot(String name, Integer id) {
        return colorRepository.existsByNameAndIdNot(name, id);
    }
}