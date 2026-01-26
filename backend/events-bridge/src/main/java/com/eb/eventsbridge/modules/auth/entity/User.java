
package com.eb.eventsbridge.modules.auth.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//@JoinColumn() -> to rename the column name
//mappedBy -> written on inver side entity

@Entity
@Table(name="users")
@Getter
@Setter
@NoArgsConstructor
public class User extends BaseAuditEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;

    @Column(unique = true, nullable = false)
    private String email;

    private String passwordHash;

    @ManyToOne(fetch = FetchType.EAGER) //default is eager
    @JoinColumn(name="role_id") //column name of fk
    private Role role;


}
