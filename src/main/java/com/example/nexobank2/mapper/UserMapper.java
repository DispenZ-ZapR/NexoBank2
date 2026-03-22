package com.example.nexobank2.mapper;

import com.example.nexobank2.dto.UserRequest;
import com.example.nexobank2.dto.UserResponse;
import com.example.nexobank2.entity.User;
import com.example.nexobank2.service.PassportService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = PassportService.class)
public interface UserMapper {
    @Mapping(source = "password", target = "passwordHash")
    @Mapping(target = "passport", source = "passportId", qualifiedByName = "getById")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "userType",source = "userType")
    User toEntity(UserRequest userRequest);
    @Mapping(target = "id", source = "id")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "userType", source = "userType")
    @Mapping(target = "phoneNumber", source = "phoneNumber")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "active", source = "isActive")
    @Mapping(target = "deletedAt", source = "deletedAt")
    UserResponse toResponse(User user);
}
