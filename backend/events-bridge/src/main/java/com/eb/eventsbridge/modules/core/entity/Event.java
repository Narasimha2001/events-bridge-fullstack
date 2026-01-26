package com.eb.eventsbridge.modules.core.entity;

import java.time.LocalDateTime;

import com.eb.eventsbridge.modules.auth.entity.BaseAuditEntity;
import com.eb.eventsbridge.modules.auth.entity.User;

import jakarta.persistence.Column;
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
import jakarta.persistence.Version;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Entity
@Table(name="event_tbl")
@Data
@EqualsAndHashCode(callSuper = true)
public class Event extends BaseAuditEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable=false)
	private String title;
	
	private String description;
	
	private String location;
	
	private Integer capacity;
	
	@Enumerated(EnumType.STRING)
    private EventStatus status = EventStatus.DRAFT;

    private String bannerImageUrl;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id", nullable=false)
	private Club club;
	
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_creator_id")
    private User eventCreator;
	
	@Column(name="start_time")
	private LocalDateTime startTime;
	
	@Column(name="end_time")
	private LocalDateTime endTime;
	
	@Version
	private Integer version; // For Optimistic Locking
	
	
}
