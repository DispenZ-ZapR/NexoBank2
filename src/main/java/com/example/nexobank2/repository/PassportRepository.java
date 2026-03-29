package com.example.nexobank2.repository;

import com.example.nexobank2.entity.Passport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PassportRepository extends JpaRepository<Passport,Long> {
    boolean existsByPassportNumber(String passportNumber);
}
