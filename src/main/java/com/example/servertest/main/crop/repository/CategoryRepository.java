package com.example.servertest.main.crop.repository;

import com.example.servertest.main.crop.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Category findByNameAndUserId(String name, Long userId);

    List<Category> findAllByUserId(Long userId);

    Category findByIdAndUserId(Long id, Long userId);

    Category findByUserId(Long userId);

}
