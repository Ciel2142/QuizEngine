package com.example.demo.utilitis.repositories;

import com.example.demo.utilitis.models.Completed;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CompletionRep extends JpaRepository<Completed, Long> {

    @Query(value = "SELECT u FROM Completed u WHERE u.user_id = ?1")
    Page<Completed> findAllByUser_id(long owner, Pageable pageable);
}
