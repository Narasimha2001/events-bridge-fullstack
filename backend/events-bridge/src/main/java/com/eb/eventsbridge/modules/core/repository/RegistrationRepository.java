package com.eb.eventsbridge.modules.core.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.eb.eventsbridge.modules.auth.entity.User;
import com.eb.eventsbridge.modules.core.entity.Event;
import com.eb.eventsbridge.modules.core.entity.Registration;
import com.eb.eventsbridge.modules.core.entity.RegistrationStatus;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long> {

	boolean existsByUserAndEvent(User user, Event event);

	long countByEventAndStatus(Event event, RegistrationStatus status);

	@Query("SELECT r FROM Registration r WHERE r.user.email = :email " +
	           "AND r.status = 'CONFIRMED' " +
	           "AND r.event.status = 'PUBLISHED'")
	    Page<Registration> findActiveRegistrations(
	        @Param("email") String email, 
	        Pageable pageable
	    );
	
	Page<Registration> findByUserEmailAndStatus(String email, RegistrationStatus status, Pageable pageable);

	Page<Registration> findByEventId(Long eventId, Pageable pageable);

	Optional<Registration> findByUserAndEvent(User user, Event event);
	
	@Query("SELECT COUNT(r) FROM Registration r WHERE r.event.club.id = :clubId AND r.status = 'CONFIRMED'")
	long countTotalRegistrationsByClubId(@Param("clubId") Long clubId);
	
	Page<Registration> findByUserEmailAndStatusNot(String email, RegistrationStatus status, Pageable pageable);
	
	Optional<Registration> findByEventAndUser(Event event, User user);
	
    List<Registration> findByEventId(Long eventId);
    
    @Query("SELECT COUNT(r) FROM Registration r WHERE r.event.club.creator.email = :email")
    Long countTotalRegistrationsForOrganizer(@Param("email") String email);
    
    Long countByUser(User user);

}