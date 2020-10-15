package com.example.demo.utilitis.repositories;

import com.example.demo.utilitis.models.Quiz;
import com.example.demo.utilitis.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {
    Page<Quiz> findAll(Pageable pageable);

    Optional<Quiz> findById(long id);

    boolean deleteQuizByUserAndId(User user, long id);

    Optional<Quiz> getOne(long id);
}
