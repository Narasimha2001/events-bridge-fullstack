package com.eb.eventsbridge.modules.core.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eb.eventsbridge.modules.auth.entity.User;
import com.eb.eventsbridge.modules.core.entity.Club;
import com.eb.eventsbridge.modules.core.entity.ClubMember;

public interface ClubMemberRepository extends SoftDeleteRepository<ClubMember, Long> {

	Optional<ClubMember> findByUserAndClubAndDeletedFalse(User user, Club club);


}
