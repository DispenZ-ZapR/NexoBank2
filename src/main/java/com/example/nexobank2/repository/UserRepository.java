package com.example.nexobank2.repository;

import com.example.nexobank2.entity.User;
import com.example.nexobank2.enums.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByEmail(String email);
    Optional<List<User>> findUsersByUserType(UserType userType);
    Optional<User> findUsersByPassportId(Long passportId);
    Optional<User> findUsersByPhoneNumber(String phoneNumber);
    boolean existsByEmailAndDeletedAtIsNull(String email);

    Optional<User> findUsersByAcToken(String acToken);
}
