package com.example.Conference.repository;

import com.example.Conference.domain.Talk;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TalkRepository extends JpaRepository<Talk, Long> {

    boolean existsByTitle(String title);

}
