package com.example.nexobank2.mapper;

import com.example.nexobank2.dto.PassportRequest;
import com.example.nexobank2.dto.PassportResponse;
import com.example.nexobank2.entity.Passport;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.jmx.export.annotation.ManagedAttribute;

@Mapper
public interface PassportMapper {

    Passport toEntity(PassportRequest request);
    @Mapping(target = "lost", source = "isLost")
    PassportResponse toResponse(Passport passport);

}
