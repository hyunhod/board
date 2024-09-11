package com.example.demo1313.repository;

import com.example.demo1313.domain.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

@org.springframework.stereotype.Repository

public interface Repository extends JpaRepository<Board,Long> {

    Page<Board> findAll(Pageable pageable);
    Page<Board> findByTitleContaining(String searchK, Pageable pageable);
}
