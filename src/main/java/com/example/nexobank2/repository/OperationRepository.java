package com.example.nexobank2.repository;

import com.example.nexobank2.entity.Operation;
import com.example.nexobank2.enums.OperationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OperationRepository extends JpaRepository<Operation, UUID> {
    Optional<List<Operation>> findByInitiatorId(Long id);
    List<Operation> findByStatus(OperationStatus status);
}
