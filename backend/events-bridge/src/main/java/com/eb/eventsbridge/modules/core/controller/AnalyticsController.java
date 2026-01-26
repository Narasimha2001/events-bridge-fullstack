package com.eb.eventsbridge.modules.core.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eb.eventsbridge.modules.core.dto.analytics.ClubAnalyticsResponse;
import com.eb.eventsbridge.modules.core.service.ClubAnalyticsService;
import com.eb.eventsbridge.shared.utils.SecurityUtils;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/analytics")
@RequiredArgsConstructor
@Tag(name = "Club Analytics", description = "Endpoints to see Club Analytics")
public class AnalyticsController {

	private final ClubAnalyticsService analyticsService;
	private final SecurityUtils securityUtils;

	@GetMapping("/clubs/{clubId}")
	@PreAuthorize("hasRole('ORGANIZER')")
	public ResponseEntity<ClubAnalyticsResponse> getDashboard(@PathVariable Long clubId) {
		String email = securityUtils.getCurrentUserEmail();
		return ResponseEntity.ok(analyticsService.getClubDashboard(clubId, email));
	}
}
