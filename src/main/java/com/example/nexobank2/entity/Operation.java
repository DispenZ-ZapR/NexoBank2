package com.example.nexobank2.entity;

import com.example.nexobank2.enums.OperationStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "operation")
public class Operation {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "initiator_id")
    private User initiator;

    @Size(max = 20)
    @NotNull
    @ColumnDefault("'WEB'")
    @Column(name = "channel", nullable = false, length = 20)
    private String channel;

    @Size(max = 20)
    @NotNull
    @ColumnDefault("'PENDING'")
    @Column(name = "status", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private OperationStatus status;

    @Size(max = 100)
    @NotNull
    @Column(name = "reason", nullable = false, length = 100)
    private String reason;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

}