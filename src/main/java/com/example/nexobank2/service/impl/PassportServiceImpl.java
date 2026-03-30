package com.example.nexobank2.service.impl;

import com.example.nexobank2.entity.BaseEntity;
import com.example.nexobank2.entity.Passport;
import com.example.nexobank2.repository.PassportRepository;
import com.example.nexobank2.service.PassportService;
import lombok.AllArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PassportServiceImpl implements PassportService {
   private final PassportRepository passportRepository;

    @Override
    public List<Passport> findAll() {
        return passportRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public Passport save(Passport entity) {
        if(passportRepository.existsPassportByPassportNumber(entity.getPassportNumber())){
            throw new RuntimeException("Passport already exists");
        }
        return passportRepository.save(entity);
    }

    @Override
   @Named("getById")
   public Passport findById(Long id){
       return passportRepository.findById(id).orElseThrow(()-> new RuntimeException("Passport not found"));
   }
}
