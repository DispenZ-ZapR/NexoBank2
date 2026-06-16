package com.example.nexobank2.mapper;

import com.example.nexobank2.dto.AccountRequestRequest;
import com.example.nexobank2.dto.AccountRequestResponse;
import com.example.nexobank2.entity.AccountRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AccountRequestMapper {
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "accountType.id", source = "accountTypeId")
    @Mapping(target = "currency.id", source = "currencyId")
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "approvedBy", ignore = true)
    @Mapping(target = "rejectionReason", ignore = true)
    @Mapping(target = "requestedAt", ignore = true)
    @Mapping(target = "processedAt", ignore = true)
    AccountRequest toEntity(AccountRequestRequest request);
    
    @Mapping(target = "clientId", source = "client.id")
    @Mapping(target = "clientName", expression = "java(getClientFullName(accountRequest))")
    @Mapping(target = "accountTypeId", source = "accountType.id")
    @Mapping(target = "accountTypeName", source = "accountType.name")
    @Mapping(target = "currencyId", source = "currency.id")
    @Mapping(target = "currencyName", source = "currency.name")
    @Mapping(target = "approvedByEmployeeId", source = "approvedBy.id")
    @Mapping(source = "id", target = "id")
    @Mapping(target = "approvedByEmployeeName", expression = "java(getEmployeeFullName(accountRequest))")
    AccountRequestResponse toResponse(AccountRequest accountRequest);
    
    List<AccountRequestResponse> toResponseList(List<AccountRequest> accountRequests);
    
    default String getClientFullName(AccountRequest accountRequest) {
        if (accountRequest.getClient() == null || 
            accountRequest.getClient().getUser() == null || 
            accountRequest.getClient().getUser().getPassport() == null) {
            return null;
        }
        var passport = accountRequest.getClient().getUser().getPassport();
        return passport.getLastName() + " " + passport.getFirstName();
    }
    
    default String getEmployeeFullName(AccountRequest accountRequest) {
        if (accountRequest.getApprovedBy() == null || 
            accountRequest.getApprovedBy().getUser() == null || 
            accountRequest.getApprovedBy().getUser().getPassport() == null) {
            return null;
        }
        var passport = accountRequest.getApprovedBy().getUser().getPassport();
        return passport.getLastName() + " " + passport.getFirstName();
    }
}
