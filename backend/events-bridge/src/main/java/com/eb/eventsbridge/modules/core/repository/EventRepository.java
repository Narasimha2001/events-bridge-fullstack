package com.eb.eventsbridge.modules.core.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.eb.eventsbridge.modules.core.entity.Event;

//JpaSpecificationExecutor=> This gives it the .findAll(Specification, Pageable) method.

public interface EventRepository extends SoftDeleteRepository<Event, Long>, JpaSpecificationExecutor<Event> {

	Page<Event> findByClubId(Long clubId, Pageable pageable);

	Optional<Event> findByIdAndClubId(Long eventId, Long clubId);

	Long countByClubId(Long clubId);

	// Get top 5 events based on registration count
	@Query("""
			    SELECT e.id, e.title, COUNT(r) as regCount
			    FROM Event e LEFT JOIN Registration r ON r.event.id = e.id
			    WHERE e.club.id = :clubId
			    GROUP BY e.id, e.title
			    ORDER BY regCount DESC
			    LIMIT 5
			""")
	List<Object[]> findTopEventsByRegistrations(@Param("clubId") Long clubId);
	
	// Count events created by a specific user (via Club)
	@Query("SELECT COUNT(e) FROM Event e WHERE LOWER(e.club.creator.email) = LOWER(:email)")
    long countByCreatorEmail(@Param("email") String email);
	

    @Query("SELECT COUNT(e) FROM Event e WHERE e.club.creator.email = :email " +
            "AND e.startTime > CURRENT_TIMESTAMP " +
            "AND e.status = 'PUBLISHED'")
     Long countUpcomingByCreatorEmail(@Param("email") String email);

}
