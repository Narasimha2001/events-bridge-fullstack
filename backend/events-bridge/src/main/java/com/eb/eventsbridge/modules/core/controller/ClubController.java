package com.eb.eventsbridge.modules.core.controller;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.eb.eventsbridge.modules.core.dto.club.ClubSummaryDto;
import com.eb.eventsbridge.modules.core.dto.club.CreateClubRequestDTO;
import com.eb.eventsbridge.modules.core.dto.club.UpdateClubRequestDTO;
import com.eb.eventsbridge.modules.core.dto.common.PagedResponse;
import com.eb.eventsbridge.modules.core.service.ClubService;
import com.eb.eventsbridge.shared.dto.SuccesssResponseDTO;
import com.eb.eventsbridge.shared.utils.SecurityUtils;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/club")
@RequiredArgsConstructor
@Tag(name = "CRUD on club", description = "Endpoints for doing CRUD on clubs")
public class ClubController {

	private final ClubService clubService;
	private final SecurityUtils securityUtils;

	@PostMapping("/create")
	@PreAuthorize("hasRole('ORGANIZER')")
	public ResponseEntity<SuccesssResponseDTO> createClub(@RequestBody CreateClubRequestDTO request) {
		String currentUserEmail = securityUtils.getCurrentUserEmail();
		SuccesssResponseDTO resp = clubService.createClub(request.getName(), request.getDescription(),
				currentUserEmail);
		return ResponseEntity.status(HttpStatus.CREATED).body(resp);
	}

	// Spring automatically maps query params → Pageable.
	// GET /club/all?page=0&size=10&sort=name,asc&sort=name,asc
	@GetMapping("/all")
	public PagedResponse<ClubSummaryDto> getAllClubs(
			@PageableDefault(size = 10, sort = "createdAt") Pageable pageable) {
		return clubService.getAllClubs(pageable);
	}

	@PutMapping("/update")
	@PreAuthorize("hasRole('ORGANIZER')")
	public ResponseEntity<SuccesssResponseDTO> updateClub(@Valid @RequestBody UpdateClubRequestDTO request) {
		String currentUserEmail = securityUtils.getCurrentUserEmail();
		return ResponseEntity.status(HttpStatus.OK).body(clubService.updateClub(request, currentUserEmail));
	}

	@DeleteMapping("/{clubId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PreAuthorize("hasRole('ORGANIZER')")
	public void deleteClub(@PathVariable Long clubId) {
		String email = securityUtils.getCurrentUserEmail();
		clubService.deleteClub(clubId, email);
	}
	
	@GetMapping("/my-clubs")
	@PreAuthorize("hasRole('ORGANIZER')")
	public ResponseEntity<List<ClubSummaryDto>> getMyClubs() {
	    String email = securityUtils.getCurrentUserEmail();
	    return ResponseEntity.ok(clubService.getMyClubs(email));
	}

}