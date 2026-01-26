package com.eb.eventsbridge.modules.core.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.eb.eventsbridge.modules.auth.entity.User;
import com.eb.eventsbridge.modules.core.entity.Event;
import com.eb.eventsbridge.modules.core.entity.EventFeedback;

@Repository
public interface FeedbackRepository extends JpaRepository<EventFeedback, Long> {

    boolean existsByUserAndEvent(User user, Event event);

    Page<EventFeedback> findByEventId(Long eventId, Pageable pageable);

    @Query("SELECT AVG(f.rating) FROM EventFeedback f WHERE f.event.id = :eventId")
    Double getAverageRatingForEvent(@Param("eventId") Long eventId);

    // Join path: Feedback -> Event -> Club
    @Query("SELECT AVG(f.rating) FROM EventFeedback f WHERE f.event.club.id = :clubId")
    Double getAverageRatingForClub(@Param("clubId") Long clubId);
    
    Optional<EventFeedback> findByUserAndEvent(User user, Event event);
}