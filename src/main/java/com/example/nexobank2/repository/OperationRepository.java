package com.example.nexobank2.repository;

import com.example.nexobank2.entity.Operation;
import com.example.nexobank2.enums.OperationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OperationRepository extends JpaRepository<Operation, UUID> {
    Optional<List<Operation>> findByInitiatorId(Long id);
    List<Operation> findByStatus(OperationStatus status);

    List<Operation> findByInitiatorIdOrderByCreatedAtDesc(Long userId);
    @Query("SELECT o from Operation o where o.initiator.id = :userId and o.createdAt between : startDate and :endDate order by o.createdAt desc ")
    List<Operation> findByInitiatorIdAndDateRange(@Param("userId") Long userId,@Param("startDate") LocalDate startDate,@Param("endDate") LocalDate endDate);
}
