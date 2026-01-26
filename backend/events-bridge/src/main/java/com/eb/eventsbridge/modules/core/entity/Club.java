package com.eb.eventsbridge.modules.core.entity;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.SQLDelete;

import com.eb.eventsbridge.modules.auth.entity.BaseAuditEntity;
import com.eb.eventsbridge.modules.auth.entity.User;
import com.eb.eventsbridge.modules.core.dto.common.SoftDeletableEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "clubs")
@Getter
@Setter
@NoArgsConstructor
public class Club extends SoftDeletableEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    @Size(min = 3, max = 100)
    private String name;

    @Column(columnDefinition = "TEXT")
    @Size(max = 500)
    private String description;

    private String logoUrl;

    // The person who created the club entry
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable=false)
    private User creator;

    @OneToMany(mappedBy = "club", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<ClubMember> members = new ArrayList<ClubMember>();
    
    @OneToMany(mappedBy="club", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Event> events = new ArrayList<Event>();
    
    
    public void addMember(ClubMember member) {
        members.add(member);
        member.setClub(this);
    }

    public void removeMember(ClubMember member) {
        members.remove(member);
        member.setClub(null);
    }
    
    public void addEvent(Event event) {
    	events.add(event);
    	event.setClub(this);
    }
    
    public void removeEvent(Event event) {
    	events.remove(event);
    	event.setClub(null);
    }
    
}