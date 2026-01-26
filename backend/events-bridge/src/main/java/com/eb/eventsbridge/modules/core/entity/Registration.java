package com.eb.eventsbridge.modules.core.entity;

import java.time.LocalDateTime;

import com.eb.eventsbridge.modules.auth.entity.BaseAuditEntity;
import com.eb.eventsbridge.modules.auth.entity.User;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "registrations", uniqueConstraints = { @UniqueConstraint(columnNames = { "user_id", "event_id" }) })
@Data
@EqualsAndHashCode(callSuper = true)
public class Registration extends BaseAuditEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "event_id", nullable = false)
	private Event event;

	@Enumerated(EnumType.STRING)
	private RegistrationStatus status = RegistrationStatus.CONFIRMED;

	private LocalDateTime registeredAt = LocalDateTime.now();
}