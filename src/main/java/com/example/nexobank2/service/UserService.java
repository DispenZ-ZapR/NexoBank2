package com.example.nexobank2.service;

import com.example.nexobank2.entity.User;
import com.example.nexobank2.enums.UserType;

import java.util.List;

public interface UserService{
    User findByEmail(String email);
    List<User> findByUsersType(UserType UserType);
    User findByPassportId(Long passportId);
    User findByPhoneNumber(String phoneNumber);
    void changeEmail(Long userId,String email);
    void changePhoneNumber(Long userId,String phoneNumber);
    void activateAccount(String password, String token);
    User save(User entity);
    void delete(User id);
    User findById(Long id);
    List<User> findAll();
}
