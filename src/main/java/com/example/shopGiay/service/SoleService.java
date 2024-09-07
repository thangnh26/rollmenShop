package com.example.shopGiay.service;

import com.example.shopGiay.model.Material;
import com.example.shopGiay.model.Sole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SoleService {
    List<Sole> getAllSoles();
    Sole getSoleById(Integer id);
    Sole saveSole(Sole sole);
    void deleteSoleById(Integer id);
    Page<Sole> getSoleByStatusNot2(Pageable pageable);
    Page<Sole> searchSoleByName(String keyword, Pageable pageable);
    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, Integer id);
}
