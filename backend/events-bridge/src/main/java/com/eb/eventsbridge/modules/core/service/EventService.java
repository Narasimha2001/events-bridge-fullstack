package com.eb.eventsbridge.modules.core.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.eb.eventsbridge.modules.auth.entity.User;
import com.eb.eventsbridge.modules.auth.repository.UserRepository;
import com.eb.eventsbridge.modules.core.dto.club.ClubSummaryDto;
import com.eb.eventsbridge.modules.core.dto.common.PagedResponse;
import com.eb.eventsbridge.modules.core.dto.common.UserSummaryDto;
import com.eb.eventsbridge.modules.core.dto.event.CreateEventRequestDTO;
import com.eb.eventsbridge.modules.core.dto.event.EventDetailsDTO;
import com.eb.eventsbridge.modules.core.dto.event.EventDto;
import com.eb.eventsbridge.modules.core.dto.event.EventSearchFilterDTO;
import com.eb.eventsbridge.modules.core.dto.event.EventSummaryDTO;
import com.eb.eventsbridge.modules.core.dto.event.UpdateEventRequestDTO;
import com.eb.eventsbridge.modules.core.entity.Club;
import com.eb.eventsbridge.modules.core.entity.ClubRole;
import com.eb.eventsbridge.modules.core.entity.Event;
import com.eb.eventsbridge.modules.core.entity.EventStatus;
import com.eb.eventsbridge.modules.core.entity.Registration;
import com.eb.eventsbridge.modules.core.entity.RegistrationStatus;
import com.eb.eventsbridge.modules.core.repository.ClubMemberRepository;
import com.eb.eventsbridge.modules.core.repository.ClubRepository;
import com.eb.eventsbridge.modules.core.repository.EventRepository;
import com.eb.eventsbridge.modules.core.repository.RegistrationRepository;
import com.eb.eventsbridge.modules.core.specification.EventSpecifications;
import com.eb.eventsbridge.shared.dto.ResponseStatus;
import com.eb.eventsbridge.shared.dto.SuccesssResponseDTO;
import com.eb.eventsbridge.shared.service.EmailService;
import com.eb.eventsbridge.shared.service.StorageService;
import com.eb.eventsbridge.shared.utils.SecurityUtils;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EventService {

	private final EventRepository eventRepo;
	private final ClubRepository clubRepo;
	private final ClubMemberRepository clubMemberRepo;
	private final UserRepository userRepo;
	private final StorageService storageService;
	private final RegistrationRepository registrationRepo;
	private final SecurityUtils securityUtils;
	private final EmailService emailService;

	@Transactional
	public SuccesssResponseDTO createEvent(CreateEventRequestDTO request, String eventCreatorEmail) {
		if (request.getEndTime().isBefore(request.getStartTime())) {
			throw new RuntimeException("End time cannot be before start time");
		}

		Club club = clubRepo.findById(request.getClubId()).orElseThrow(() -> new RuntimeException("Club not found"));

		User user = userRepo.findByEmail(eventCreatorEmail).orElseThrow(() -> new RuntimeException("User not found"));

		boolean isAuthorized = clubMemberRepo.findByUserAndClubAndDeletedFalse(user, club)
				.map(member -> member.getClubRole() == ClubRole.OWNER || member.getClubRole() == ClubRole.ADMIN)
				.orElse(false);

		if (!isAuthorized) {
			throw new RuntimeException("You do not have permission to create events for this club");
		}

		Event event = new Event();
		event.setTitle(request.getTitle());
		event.setDescription(request.getDescription());
		event.setStartTime(request.getStartTime());
		event.setEndTime(request.getEndTime());
		event.setLocation(request.getLocation());
		event.setCapacity(request.getCapacity());
		event.setClub(club);
		event.setEventCreator(user);
		event.setStatus(EventStatus.PUBLISHED);

		eventRepo.save(event);

		return new SuccesssResponseDTO("Event created Successfully", ResponseStatus.SUCCESS);
	}

	public PagedResponse<EventSummaryDTO> getAllEventsOfClub(Long clubId, Pageable pageable) {

		Page<Event> page = eventRepo.findByClubId(clubId, pageable);

		List<EventSummaryDTO> content = page.getContent().stream().map(event -> new EventSummaryDTO(event.getId(),
				event.getTitle(), event.getLocation(), event.getStatus(), event.getStartTime(), event.getEndTime(), event.getClub().getName(),
				event.getDescription(), event.getCapacity()
				))
				.toList();

		return new PagedResponse<>(content, page.getNumber(), page.getSize(), page.getTotalElements(),
				page.getTotalPages());
	}

	@Transactional
	public SuccesssResponseDTO updateEvent(UpdateEventRequestDTO request, String currentUserEmail) {
		User user = userRepo.findByEmail(currentUserEmail).orElseThrow(() -> new RuntimeException("User not found"));

		Club club = clubRepo.findByIdAndDeletedFalse(request.getClubId())
				.orElseThrow(() -> new RuntimeException("Club not found"));

		validateClubAdminAccess(club, user);

		Event event = eventRepo.findByIdAndClubId(request.getEventId(), request.getClubId())
				.orElseThrow(() -> new RuntimeException("Event not found"));

		if (event.getStatus() == EventStatus.CANCELLED) {
            throw new RuntimeException("Cannot edit a cancelled event.");
        }
		
		if (request.getTitle() != null)
			event.setTitle(request.getTitle());
		if (request.getDescription() != null)
			event.setDescription(request.getDescription());
		if (request.getLocation() != null)
			event.setLocation(request.getLocation());
		if (request.getCapacity() != null)
			event.setCapacity(request.getCapacity());
		if (request.getStartTime() != null)
			event.setStartTime(request.getStartTime());
		if (request.getEndTime() != null)
			event.setEndTime(request.getEndTime());

		return new SuccesssResponseDTO("Event updated successfully", ResponseStatus.SUCCESS);
	}

	private void validateClubAdminAccess(Club club, User user) {
		boolean allowed = club.getMembers().stream().anyMatch(m -> m.getUser().getId().equals(user.getId())
				&& (m.getClubRole() == ClubRole.OWNER || m.getClubRole() == ClubRole.ADMIN));

		if (!allowed) {
			throw new RuntimeException("Not authorized to manage events");
		}
	}

	@Transactional
	public void deleteEvent(Long clubId, Long eventId, String email) {

		User user = userRepo.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

		Club club = clubRepo.findByIdAndDeletedFalse(clubId).orElseThrow(() -> new RuntimeException("Club not found"));

		validateClubAdminAccess(club, user);

		Event event = eventRepo.findByIdAndClubId(eventId, clubId)
				.orElseThrow(() -> new RuntimeException("Event not found"));

		club.removeEvent(event); // orphanRemoval = true
	}

	@Transactional
    public Page<Event> searchEvents(EventSearchFilterDTO filter, Pageable pageable) {
        
        EventStatus statusToSearch = (filter.getStatus() != null) 
                                     ? filter.getStatus() 
                                     : EventStatus.PUBLISHED;

        Specification<Event> spec = Specification.where(EventSpecifications.hasKeyword(filter.getTitle()))
                .and(EventSpecifications.hasClubId(filter.getClubId()))
                .and(EventSpecifications.hasStatus(statusToSearch))
                .and(EventSpecifications.filterByDate(filter.getStartDate(), filter.getEndDate()));

        return eventRepo.findAll(spec, pageable);
    }

	private void validateClubAuthority(Club club, String email) {
		User user = userRepo.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

		boolean isAuthorized = clubMemberRepo.findByUserAndClubAndDeletedFalse(user, club)
				.map(member -> member.getClubRole() == ClubRole.OWNER || member.getClubRole() == ClubRole.ADMIN)
				.orElse(false);

		if (!isAuthorized) {
			throw new RuntimeException("Access Denied: You do not have permission to modify this event.");
		}
	}

	@Transactional
	public void updateEventBanner(Long eventId, String imageUrl, String userEmail) {
		Event event = eventRepo.findById(eventId).orElseThrow(() -> new RuntimeException("Event not found"));

	
		validateClubAuthority(event.getClub(), userEmail);

		if (event.getBannerImageUrl() != null) {
			storageService.deleteFile(event.getBannerImageUrl());
		}

		event.setBannerImageUrl(imageUrl);

		eventRepo.save(event);
	}
	
	
	
	
	
	
	@Transactional
    public EventDetailsDTO getEventDetails(Long eventId) {
        Event event = eventRepo.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        
        UserSummaryDto creatorDto = new UserSummaryDto(
                event.getClub().getCreator().getId(),
                event.getClub().getCreator().getFullName(),
                event.getClub().getCreator().getEmail(),
                event.getClub().getCreator().getRole().getName()
        );

        ClubSummaryDto clubDto = new ClubSummaryDto(
                event.getClub().getId(),
                event.getClub().getName(),
                event.getClub().getDescription(),
                event.getClub().getLogoUrl(),
                creatorDto,
                event.getClub().getMembers().size(), // Safe access inside Transaction
                event.getClub().getCreatedAt()
        );

        Long currentRegs = registrationRepo.countByEventAndStatus(event, RegistrationStatus.CONFIRMED);

        return new EventDetailsDTO(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getLocation(),
                event.getCapacity(),
                currentRegs,
                event.getStatus(),
                event.getBannerImageUrl(),
                event.getStartTime(),
                event.getEndTime(),
                clubDto
        );
    }
	
	
	@Transactional
    public EventDto updateEvent(Long eventId, EventDto input) {
        String currentUserEmail = securityUtils.getCurrentUserEmail();
        
        Event event = eventRepo.findById(eventId)
            .orElseThrow(() -> new RuntimeException("Event not found"));

        if (!event.getClub().getCreator().getEmail().equals(currentUserEmail)) {
            throw new RuntimeException("Unauthorized: You do not own this event");
        }

        event.setTitle(input.title());
        event.setDescription(input.description());
        event.setLocation(input.location());
        event.setStartTime(input.startTime());
        event.setEndTime(input.endTime());
        event.setCapacity(input.capacity());
        
        Event updated = eventRepo.save(event);
        return mapToDto(updated);
    }

    @Transactional
    public void cancelEvent(Long eventId) {
        String currentUserEmail = securityUtils.getCurrentUserEmail();
        
        Event event = eventRepo.findById(eventId)
            .orElseThrow(() -> new RuntimeException("Event not found"));

        if (!event.getClub().getCreator().getEmail().equals(currentUserEmail)) {
            throw new RuntimeException("Unauthorized");
        }
        
        if (event.getStatus() == EventStatus.CANCELLED) {
            throw new RuntimeException("Event is already cancelled.");
        }
        
        event.setStatus(EventStatus.CANCELLED);
        eventRepo.save(event);

		List<Registration> registrations = registrationRepo.findByEventId(eventId);

		for (Registration reg : registrations) {
			if (reg.getStatus() == RegistrationStatus.CONFIRMED) {

				emailService.sendCancellationEmail(reg.getUser().getEmail(), reg.getUser().getFullName(),
						event.getTitle());

				reg.setStatus(RegistrationStatus.CANCELLED);
				registrationRepo.save(reg);
			}
		}
    }
    
    private EventDto mapToDto(Event event) {
        return new EventDto(
            event.getId(),
            event.getTitle(),
            event.getDescription(),
            event.getLocation(),
            event.getStartTime(),
            event.getEndTime(),
            event.getCapacity(),
            event.getBannerImageUrl()
        );
    }

}