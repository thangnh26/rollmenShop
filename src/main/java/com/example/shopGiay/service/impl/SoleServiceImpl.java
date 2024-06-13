package com.example.shopGiay.service.impl;


import com.example.shopGiay.model.Sole;
import com.example.shopGiay.repository.SoleRepository;
import com.example.shopGiay.service.SoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SoleServiceImpl implements SoleService {
    @Autowired
    private SoleRepository soleRepository;

    @Override
    public List<Sole> getAllSoles() {
        return soleRepository.findAll();
    }

    @Override
    public Sole getSoleById(Integer id) {
        return soleRepository.findById(id).orElse(null);
    }

    @Override
    public Sole saveSole(Sole sole) {
        return soleRepository.save(sole);
    }

    @Override
    public void deleteSoleById(Integer id) {
        soleRepository.deleteById(id);
    }
}
