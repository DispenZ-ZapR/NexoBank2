package com.example.nexobank2.service.impl;

import com.example.nexobank2.entity.BaseEntity;
import com.example.nexobank2.entity.Passport;
import com.example.nexobank2.entity.User;
import com.example.nexobank2.enums.UserType;
import com.example.nexobank2.repository.PassportRepository;
import com.example.nexobank2.repository.UserRepository;
import com.example.nexobank2.service.UserService;
import lombok.AllArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PassportRepository passportRepository;
    private final EmailServiceImpl emailService;
    @Transactional
    @Override
    public User save(User entity) {
        if (userRepository.existsByEmailAndDeletedAtIsNull(entity.getEmail())){
            throw new RuntimeException("Email already exists");
        }
        Passport passport = entity.getPassport();
        passportRepository.save(passport);
        User user = new User();
        user.setEmail(entity.getEmail());
        user.setUserType(entity.getUserType());
        user.setPhoneNumber(entity.getPhoneNumber());
        user.setPassport(passport);
        user.setAcToken(UUID.randomUUID().toString());
        user.setPasswordHash(null);
        user.setCreatedAt(LocalDateTime.now());
        user.setActivationTokenExpiresAt(LocalDateTime.now().plusHours(72));

        return userRepository.save(user);

    }

    @Override
    public void activateAccount(String password, String token) {
        User user = userRepository.findUsersByAcToken(token).orElseThrow(()-> new RuntimeException("Токен не действителен"));
        if (user.getActivationTokenExpiresAt().isBefore(LocalDateTime.now())){
            throw new RuntimeException("Срок действия токена истек");
        }
        user.setPasswordHash(password);
        user.setAcToken(null);
        user.setActivationTokenExpiresAt(null);
        userRepository.save(user);
    }

    @Override
    public void delete(User user) {
       user.setDeletedAt(LocalDateTime.now());
       userRepository.save(user);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll().stream().filter(user -> user.getDeletedAt() == null).toList();
    }

    @Named("findById")
    public User findById(Long id){
        return userRepository.findById(id).orElseThrow(()-> new RuntimeException("User not found"));
    }

    @Override
    public void changePhoneNumber(Long userId, String phoneNumber) {
        User user =userRepository.findById(userId).orElseThrow(()-> new RuntimeException("User not found"));
        user.setPhoneNumber(phoneNumber);
        userRepository.save(user);
    }

    @Override
    public void changeEmail(Long userId, String email) {
        User user =userRepository.findById(userId).orElseThrow(()-> new RuntimeException("User not found"));
        user.setEmail(email);
        userRepository.save(user);
    }

    @Override
    public User findByPhoneNumber(String phoneNumber) {
        return userRepository.findUsersByPhoneNumber(phoneNumber).orElseThrow(()-> new RuntimeException("User not found"));
    }

    @Override
    public User findByPassportId(Long passportId) {
        return userRepository.findUsersByPassportId(passportId).orElseThrow(()-> new RuntimeException("User not found"));
    }

    @Override
    public List<User> findByUsersType(UserType userType) {
        return userRepository.findUsersByUserType(userType).orElseThrow(()-> new RuntimeException("Users not found"));
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findUserByEmail(email).orElseThrow(()-> new RuntimeException("User not found"));
    }
}
