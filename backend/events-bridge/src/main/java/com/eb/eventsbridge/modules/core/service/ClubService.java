package com.eb.eventsbridge.modules.core.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.eb.eventsbridge.modules.auth.entity.User;
import com.eb.eventsbridge.modules.auth.repository.UserRepository;
import com.eb.eventsbridge.modules.core.dto.club.ClubSummaryDto;
import com.eb.eventsbridge.modules.core.dto.club.UpdateClubRequestDTO;
import com.eb.eventsbridge.modules.core.dto.common.PagedResponse;
import com.eb.eventsbridge.modules.core.entity.Club;
import com.eb.eventsbridge.modules.core.entity.ClubMember;
import com.eb.eventsbridge.modules.core.entity.ClubRole;
import com.eb.eventsbridge.modules.core.mapper.ClubMapper;
import com.eb.eventsbridge.modules.core.repository.ClubMemberRepository;
import com.eb.eventsbridge.modules.core.repository.ClubRepository;
import com.eb.eventsbridge.shared.dto.ResponseStatus;
import com.eb.eventsbridge.shared.dto.SuccesssResponseDTO;
import com.eb.eventsbridge.shared.utils.AppConstants;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClubService {

	private final UserRepository userRepo;
	private final ClubMemberRepository clubMemberRepo;
	private final ClubRepository clubRepo;

	@Transactional
	public SuccesssResponseDTO createClub(String name, String description, String currentUserEmail) {

		Club club = new Club();
		club.setName(name);
		club.setDescription(description);

		User user = userRepo.findByEmail(currentUserEmail)
				.orElseThrow(() -> new RuntimeException("Creater is not registered"));

		club.setCreator(user);

		ClubMember clubMember = new ClubMember();
		clubMember.setClub(club);
		clubMember.setClubRole(ClubRole.OWNER);
		clubMember.setUser(user);

		clubMemberRepo.save(clubMember);

		club.getMembers().add(clubMember);

		clubRepo.save(club);

		return new SuccesssResponseDTO(AppConstants.CREATE_SUCCESS, ResponseStatus.SUCCESS);
	}

	public PagedResponse<ClubSummaryDto> getAllClubs(Pageable pageable) {

		Page<ClubSummaryDto> page = clubRepo.findByDeletedFalse(pageable).map(ClubMapper::toSummaryDto);

		return new PagedResponse<>(page.getContent(), page.getNumber(), page.getSize(), page.getTotalElements(),
				page.getTotalPages());
	}

	@Transactional
	public SuccesssResponseDTO updateClub(UpdateClubRequestDTO request, String userEmail) {

		Club club = clubRepo.findById(request.clubId()).orElseThrow(() -> new RuntimeException("Club not found"));

		validateOwnership(club, userEmail);

		if (request.name() != null) {
			club.setName(request.name());
		}

		if (request.description() != null) {
			club.setDescription(request.description());
		}

		if (request.logoUrl() != null) {
			club.setLogoUrl(request.logoUrl());
		}

		return new SuccesssResponseDTO(AppConstants.UPDATE_SUCCESS, ResponseStatus.SUCCESS);
	}

	@Transactional
	public void deleteClub(Long clubId, String email) {

		Club club = clubRepo.findByIdAndDeletedFalse(clubId)
				.orElseThrow(() -> new RuntimeException("Club not found"));

		validateOwnership(club, email);
		
		for (ClubMember member : club.getMembers()) {
	        member.softDelete();
	    }

		club.softDelete(); //dirty checked after tx commits.
	}

	
	private void validateOwnership(Club club, String email) {
		if (!club.getCreator().getEmail().equals(email)) {
			throw new RuntimeException("Only the club creator can modify this club");
		}
	}
	
	public List<ClubSummaryDto> getMyClubs(String creatorEmail) {
	    List<Club> clubs = clubRepo.findByCreatorEmailAndDeletedFalse(creatorEmail);
	    return clubs.stream().map(ClubMapper::toSummaryDto).toList();
	}

}
