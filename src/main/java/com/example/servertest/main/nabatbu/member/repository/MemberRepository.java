package com.example.servertest.main.nabatbu.member.repository;

import com.example.servertest.main.nabatbu.member.entity.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

	Optional<Member> findByEmail(String email);
	Optional<Member> findByName(String name);
	Slice<Member> findByEmailContainingAndNameContainingOrderByRegDtAsc(String email, String name, Pageable pageable);
}