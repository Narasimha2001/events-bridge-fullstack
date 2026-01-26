package com.eb.eventsbridge.modules.core.entity;

import java.time.LocalDateTime;

import com.eb.eventsbridge.modules.auth.entity.User;
import com.eb.eventsbridge.modules.core.dto.common.SoftDeletableEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
		name = "club_members",
		uniqueConstraints = {
		        @UniqueConstraint(columnNames = {"user_id", "club_id"})
		}
)
@Getter
@Setter
@NoArgsConstructor
public class ClubMember extends SoftDeletableEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id")
    private Club club;

    @Enumerated(EnumType.STRING)
    private ClubRole clubRole;

    private LocalDateTime joinedAt;
    
    @PrePersist
    void updateJoinedAt() {
        this.joinedAt = LocalDateTime.now();
    }
    
    
    
}