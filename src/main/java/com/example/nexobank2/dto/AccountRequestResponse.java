package com.example.nexobank2.dto;

import com.example.nexobank2.enums.AccountRequestStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AccountRequestResponse {
    
    private Long id;
    
    private Long clientId;
    
    private String clientName;
    
    private Long accountTypeId;
    
    private String accountTypeName;
    
    private Long currencyId;
    
    private String currencyName;
    
    private AccountRequestStatus status;
    
    private Long approvedByEmployeeId;
    
    private String approvedByEmployeeName;
    
    private String rejectionReason;
    
    private LocalDateTime requestedAt;
    
    private LocalDateTime processedAt;
}
