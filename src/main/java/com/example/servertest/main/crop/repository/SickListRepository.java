package com.example.servertest.main.crop.repository;

import com.example.servertest.main.crop.entity.SickList;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SickListRepository extends JpaRepository<SickList, String> {

    List<SickList> findByCropNameContainingAndSickNameKorContaining(
        String cropName, String sickName);
}