package com.example.nexobank2.mapper;

import com.example.nexobank2.dto.ClientRequest;
import com.example.nexobank2.dto.ClientResponse;
import com.example.nexobank2.entity.Client;
import com.example.nexobank2.service.AccountService;
import com.example.nexobank2.service.UserService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserService.class, AccountService.class})
public interface ClientMapper {
    @Mapping(target = "user", source = "userId", qualifiedByName = "findById")
    @Mapping(target = "creditRating",source = "creditRating")
    Client toEntity(ClientRequest request);
    @Mapping(target = "id", source = "id")
    @Mapping(target = "userId",source = "user.id")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "creditRating", source = "creditRating")
    @Mapping(target = "accountId", source = "account.id")
    ClientResponse toResponse(Client client);
}
