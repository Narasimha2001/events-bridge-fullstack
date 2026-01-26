package com.eb.eventsbridge.modules.auth.entity;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Only Users with Role 'ORGANIZER' can do CRUD on Clubs / Events 
 * 
 */

@Entity
@Table(name="roles")
@Data
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

}
