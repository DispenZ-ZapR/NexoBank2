package com.example.nexobank2.mapper;

import com.example.nexobank2.dto.UserRequest;
import com.example.nexobank2.dto.UserResponse;
import com.example.nexobank2.entity.User;
import com.example.nexobank2.service.impl.PassportServiceImpl;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = PassportServiceImpl.class)
public interface UserMapper {
    @Mapping(source = "firstName", target = "passport.firstName")
    @Mapping(source = "lastName", target = "passport.lastName")
    @Mapping(source = "middleName", target = "passport.middleName")
    @Mapping(source = "dateOfBirth", target = "passport.dateOfBirth")
    @Mapping(source = "personalNumber",target = "passport.personalNumber")
    @Mapping(source = "passportNumber",target = "passport.passportNumber")
    @Mapping(target = "id", ignore = true)
    User toEntity(UserRequest userRequest);
    @Mapping(source = "passport.firstName", target = "firstName")
    @Mapping(source = "passport.lastName", target = "lastName")
    @Mapping(source = "passport.middleName", target = "middleName")
    UserResponse toResponse(User user);
    List<UserResponse> toUserResponseList(List<User> users);
}
