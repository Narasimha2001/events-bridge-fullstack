package com.eb.eventsbridge.modules.auth.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Entity
@Data
@Table(name="refresh_token_tbl")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private boolean isRevoked;

    @ManyToOne()
    @JoinColumn(nullable = false)
    private User user;

    @Column(name="revoked_at")
    private Instant revokedAt;

}
