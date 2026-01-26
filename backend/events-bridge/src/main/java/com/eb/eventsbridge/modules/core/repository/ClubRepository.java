package com.eb.eventsbridge.modules.core.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;

import com.eb.eventsbridge.modules.core.entity.Club;

/**
 * 
 * Why @EntityGraph?
 * 
 * @EntityGraph tells Hibernate:
 *	“For THIS query, eagerly fetch these relationships in the SAME SQL query.”
 *
 * Ex: 	@EntityGraph(attributePaths = {"creator"}) means,
 * 	
 * Fetch Club, Fetch creator using join. Don't touch events or members.
 * Query generated:
 *  SELECT c.*, u.*
 *	FROM clubs c
 *	LEFT JOIN users u ON c.creator_id = u.id
 *	LIMIT ?, ?
 *
 * What happens if you REMOVE @EntityGraph?
 * Fetch Club only, creator is LAZY, Accessing club.getCreator() triggers another query PER club
 * 
 * N + 1 Problem:
 * -- 1 query
 * SELECT * FROM clubs LIMIT 10;
 *
 * -- N queries (for each club)
 * SELECT * FROM users WHERE id = ?;
 * SELECT * FROM users WHERE id = ?;
 * SELECT * FROM users WHERE id = ?;
 *...
 * 
 */


public interface ClubRepository extends SoftDeleteRepository<Club,Long> {

	@EntityGraph(attributePaths = {"creator"})
    Page<Club> findByDeletedFalse(Pageable pageable);

	Optional<Club> findByIdAndDeletedFalse(Long clubId);
	
	List<Club> findByCreatorEmailAndDeletedFalse(String email);
	
}
