package com.eb.eventsbridge.modules.core.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.eb.eventsbridge.modules.core.dto.analytics.ClubAnalyticsResponse;
import com.eb.eventsbridge.modules.core.dto.analytics.EventPerformanceDTO;
import com.eb.eventsbridge.modules.core.entity.Club;
import com.eb.eventsbridge.modules.core.repository.ClubRepository;
import com.eb.eventsbridge.modules.core.repository.EventRepository;
import com.eb.eventsbridge.modules.core.repository.FeedbackRepository;
import com.eb.eventsbridge.modules.core.repository.RegistrationRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClubAnalyticsService {

	private final ClubRepository clubRepo;
	private final EventRepository eventRepo;
	private final RegistrationRepository registrationRepo;
	private final FeedbackRepository feedbackRepo;

	@Transactional
	public ClubAnalyticsResponse getClubDashboard(Long clubId, String ownerEmail) {
		Club club = clubRepo.findById(clubId).orElseThrow(() -> new RuntimeException("Club not found"));

		if (!club.getCreator().getEmail().equals(ownerEmail)) {
			throw new RuntimeException("Access denied: You are not the owner of this club");
		}

		long totalEvents = eventRepo.countByClubId(clubId);
		long totalRegs = registrationRepo.countTotalRegistrationsByClubId(clubId);

		Double avgRating = feedbackRepo.getAverageRatingForClub(clubId);

		List<Object[]> topEventsRaw = eventRepo.findTopEventsByRegistrations(clubId);
		List<EventPerformanceDTO> topEvents = topEventsRaw.stream().map(obj -> EventPerformanceDTO.builder()
				.eventId((Long) obj[0]).title((String) obj[1]).registrationCount((Long) obj[2]).build())
				.collect(Collectors.toList());

		return ClubAnalyticsResponse.builder().clubId(clubId).clubName(club.getName()).totalEvents(totalEvents)
				.totalRegistrations(totalRegs).averageRating(avgRating != null ? avgRating : 0.0).topEvents(topEvents)
				.build();
	}
}