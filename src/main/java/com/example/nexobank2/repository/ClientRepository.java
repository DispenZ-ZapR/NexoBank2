package com.example.nexobank2.repository;

import com.example.nexobank2.entity.Client;
import com.example.nexobank2.enums.ClientStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {
    List<Client> findByCreatedAt(LocalDateTime createdAt);
    List<Client> findByClientStatus(ClientStatus clientStatus);
    Optional<Client> findByUserId(Long userId);
    Page<Client> findByClientStatus(ClientStatus clientStatus, Pageable pageable);

    
    List<Client> findByClientStatusAndCreatedAtBetween(ClientStatus status, LocalDateTime startDate, LocalDateTime endDate);

    //TODO этот метод на подумать
//    @Query("SELECT c FROM Client c WHERE c.clientStatus = 'ACTIVE'")
//    List<Client> findActiveClients();
    
    @Query("SELECT c FROM Client c WHERE c.clientStatus = 'DELETED'")
    List<Client> findDeletedClients();
    
    @Query("SELECT COUNT(c) FROM Client c WHERE c.clientStatus = :status")
    Integer countByClientStatus(@Param("status") ClientStatus status);
}
