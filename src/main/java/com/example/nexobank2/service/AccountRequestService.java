package com.example.nexobank2.service;

import com.example.nexobank2.entity.AccountRequest;
import com.example.nexobank2.enums.AccountRequestStatus;

import java.util.List;

public interface AccountRequestService extends BaseService<AccountRequest> {
    
    AccountRequest createRequest(Long clientId, Long accountTypeId, Long currencyId);
    
    AccountRequest approveRequest(Long requestId, Long employeeId);
    
    void rejectRequest(Long requestId, Long employeeId, String reason);
    
    List<AccountRequest> findByStatus(AccountRequestStatus status);
    
    List<AccountRequest> findByClientId(Long clientId);
    
    List<AccountRequest> findByClientIdAndStatus(Long clientId, AccountRequestStatus status);

    List<AccountRequest> getPendingRequests();
    
    List<AccountRequest> findByApprovedById(Long employeeId);

    List<AccountRequest> getMyRequest(Long userId);

    List<AccountRequest> getMyCheckedRequest(Long userId);
}
