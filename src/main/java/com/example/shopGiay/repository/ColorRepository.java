package com.example.shopGiay.repository;

import org.springframework.stereotype.Repository;

import com.example.shopGiay.model.Color;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface ColorRepository extends JpaRepository<Color, Integer> {

}
