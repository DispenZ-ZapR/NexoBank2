package com.example.nexobank2.repository;

import com.example.nexobank2.entity.EmployeePosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeePositionRepository extends JpaRepository<EmployeePosition,Long> {
}
