package com.example.nexobank2.mapper;

import com.example.nexobank2.dto.ClientRequest;
import com.example.nexobank2.dto.ClientResponse;
import com.example.nexobank2.entity.Client;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ClientMapper {
    @Mapping(target = "user.email", source = "email")
    @Mapping(target = "user.phoneNumber", source = "phoneNumber")
    @Mapping(target = "clientStatus", constant = "UNVERIFIED")
    @Mapping(target = "creditRating", constant = "0")
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "user.passport.firstName", source = "firstName")
    @Mapping(target = "user.passport.lastName", source = "lastName")
    @Mapping(target = "user.passport.middleName", source = "middleName")
    @Mapping(target = "user.passport.dateOfBirth", source = "dateOfBirth")
    @Mapping(target = "user.passport.personalNumber", source = "personalNumber")
    @Mapping(target = "user.passport.passportNumber", source = "passportNumber")
    @Mapping(target = "id", ignore = true)
    Client toEntity(ClientRequest request);
    @Mapping(source = "user.passport.firstName", target = "firstName")
    @Mapping(source = "user.passport.lastName", target = "lastName")
    @Mapping(source = "user.passport.middleName", target = "middleName")
    @Mapping(source = "user.passport.dateOfBirth", target = "dateOfBirth")
    @Mapping(source = "user.email", target = "email")
    @Mapping(source = "user.phoneNumber", target = "phoneNumber")
    @Mapping(source = "user.createdAt", target = "createdAt")
    @Mapping(source = "user.deletedAt", target = "deletedAt")
    ClientResponse toResponse(Client client);

    List<ClientResponse> toResponseList(List<Client> clients);
}
