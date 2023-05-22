package com.example.practice.repository;

import com.example.practice.model.Lend;
import com.example.practice.model.LendStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.practice.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface LendRepository extends JpaRepository<Lend, Long> {
    Optional<Lend> findByBookAndStatus(Book book, LendStatus status);
}