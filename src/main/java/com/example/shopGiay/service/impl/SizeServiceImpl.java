package com.example.shopGiay.service.impl;

import com.example.shopGiay.model.Size;
import com.example.shopGiay.repository.SizeRepository;
import com.example.shopGiay.service.SizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SizeServiceImpl implements SizeService {
    @Autowired
    private SizeRepository sizeRepository;

    @Override
    public List<Size> getAllSizes() {
        return sizeRepository.findAll();
    }

    @Override
    public Size getSizeById(Integer id) {
        return sizeRepository.findById(id).orElse(null);
    }

    @Override
    public Size saveSize(Size size) {
        return sizeRepository.save(size);
    }

    @Override
    public void deleteSizeById(Integer id) {
        sizeRepository.deleteById(id);
    }
}
