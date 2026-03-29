package com.example.nexobank2.entity;

import com.example.nexobank2.enums.UserType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User extends BaseEntity{
    @NotNull
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "passport_id", nullable = false)
    private Passport passport;

    @Size(max = 255) @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Size(max = 35)
    @NotNull
    @Column(name = "email", nullable = false, length = 35)
    private String email;

    @Size(max = 20)
    @NotNull
    @Column(name = "user_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserType userType;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @NotNull
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = false;

    @Size(max = 20)
    @NotNull
    @Column(name = "phone_number", nullable = false, length = 20)
    private String phoneNumber;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt = null;

    @Column(name = "ac_token")
    private String acToken;

    @Column(name = "activation_token_expires_at")
    private LocalDateTime activationTokenExpiresAt;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Client client;
}