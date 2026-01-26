package com.eb.eventsbridge.modules.core.dto.dashboard;


public record OrganizerStatsDto(
    long totalEvents,
    long upcomingEvents,
    long totalRegistrations,
    long totalClubs
) {}