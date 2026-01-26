package com.eb.eventsbridge.modules.core.service;

import org.springframework.stereotype.Service;

import com.eb.eventsbridge.modules.core.dto.dashboard.OrganizerStatsDto;
import com.eb.eventsbridge.modules.core.repository.ClubRepository;
import com.eb.eventsbridge.modules.core.repository.EventRepository;
import com.eb.eventsbridge.modules.core.repository.RegistrationRepository;
import com.eb.eventsbridge.shared.utils.SecurityUtils;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final EventRepository eventRepo;
    private final RegistrationRepository registrationRepo;
    private final ClubRepository clubRepo;
    private final SecurityUtils securityUtils;

    @Transactional
    public OrganizerStatsDto getOrganizerStats() {
        String email = securityUtils.getCurrentUserEmail();

        long totalEvents = eventRepo.countByCreatorEmail(email);
        long upcoming = eventRepo.countUpcomingByCreatorEmail(email);
        long totalRegs = registrationRepo.countTotalRegistrationsForOrganizer(email);
        long totalClubs = clubRepo.findByCreatorEmailAndDeletedFalse(email).size(); 

        return new OrganizerStatsDto(totalEvents, upcoming, totalRegs, totalClubs);
    }
}