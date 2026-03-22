package com.example.nexobank2.service;

import com.example.nexobank2.entity.Passport;
import com.example.nexobank2.repository.PassportRepository;
import lombok.AllArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PassportService {
   private final PassportRepository passportRepository;

   @Named("getById")
   public Passport getById(Long id){
       return passportRepository.findById(id).orElseThrow(()-> new RuntimeException("Passport not found"));
   }
}
