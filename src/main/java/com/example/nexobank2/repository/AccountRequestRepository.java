package com.example.nexobank2.repository;

import com.example.nexobank2.entity.AccountRequest;
import com.example.nexobank2.enums.AccountRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRequestRepository extends JpaRepository<AccountRequest, Long> {
    
    List<AccountRequest> findByStatus(AccountRequestStatus status);
    
    List<AccountRequest> findByClientId(Long clientId);
    
    List<AccountRequest> findByClientIdAndStatus(Long clientId, AccountRequestStatus status);
    
    List<AccountRequest> findByApprovedById(Long employeeId);

    List<AccountRequest> findAccountRequestByApprovedBy_Id(Long approvedById);
}
