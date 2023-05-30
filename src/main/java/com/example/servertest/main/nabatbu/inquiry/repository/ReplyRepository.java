package com.example.servertest.main.nabatbu.inquiry.repository;

import com.example.servertest.main.nabatbu.inquiry.entity.Inquiry;
import com.example.servertest.main.nabatbu.inquiry.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReplyRepository extends JpaRepository<Reply, Long> {

    List<Reply> findAllByUserId(Long userId);

    int countByInquiryId(Long inquiryId);
}
