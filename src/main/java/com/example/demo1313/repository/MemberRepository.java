package com.example.demo1313.repository;

import com.example.demo1313.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface MemberRepository extends JpaRepository<Member,Long> {
    Optional<Member> findByLoginId(String loginId);
    Optional<Member> findByNameAndEmail(String name,String email);
    Optional<Member> findByLoginIdAndEmail(String loginId,String email);

}
