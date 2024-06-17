package com.example.shopGiay.service;

import com.example.shopGiay.model.Sole;

import java.util.List;

public interface SoleService {
    List<Sole> getAllSoles();
    Sole getSoleById(Integer id);
    Sole saveSole(Sole sole);
    void deleteSoleById(Integer id);
}
