package com.example.shopGiay.repository;

import com.example.shopGiay.model.Sole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SoleRepository extends JpaRepository<Sole, Integer> {
    @Query(nativeQuery = true, value = "SELECT * FROM sole WHERE id = ?1")
    Sole getById(Integer id);}
