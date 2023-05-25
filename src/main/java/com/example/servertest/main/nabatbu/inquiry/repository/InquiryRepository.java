package com.example.servertest.main.nabatbu.inquiry.repository;

import com.example.servertest.main.nabatbu.category.entity.Category;
import com.example.servertest.main.nabatbu.inquiry.entity.Inquiry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InquiryRepository extends JpaRepository<Inquiry, Long> {

    List<Inquiry> findAllByUserId(Long userId);
}
