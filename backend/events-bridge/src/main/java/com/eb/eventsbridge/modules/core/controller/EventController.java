package com.eb.eventsbridge.modules.core.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.eb.eventsbridge.modules.core.dto.common.PagedResponse;
import com.eb.eventsbridge.modules.core.dto.event.CreateEventRequestDTO;
import com.eb.eventsbridge.modules.core.dto.event.EventDetailsDTO;
import com.eb.eventsbridge.modules.core.dto.event.EventDto;
import com.eb.eventsbridge.modules.core.dto.event.EventSearchFilterDTO;
import com.eb.eventsbridge.modules.core.dto.event.EventSummaryDTO;
import com.eb.eventsbridge.modules.core.dto.event.UpdateEventRequestDTO;
import com.eb.eventsbridge.modules.core.entity.Event;
import com.eb.eventsbridge.modules.core.service.EventService;
import com.eb.eventsbridge.shared.dto.MessageResponse;
import com.eb.eventsbridge.shared.dto.SuccesssResponseDTO;
import com.eb.eventsbridge.shared.service.StorageService;
import com.eb.eventsbridge.shared.utils.SecurityUtils;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/event")
@RequiredArgsConstructor
@Tag(name = "CRUD on Event", description = "Endpoints for doing CRUD on events")
public class EventController {

	private final EventService eventService;
	private final SecurityUtils securityUtils;
	private final StorageService storageService;

	@PostMapping("/create")
	@PreAuthorize("hasRole('ORGANIZER')")
	public ResponseEntity<SuccesssResponseDTO> createEvent(@Validated @RequestBody CreateEventRequestDTO request) {
		String email = securityUtils.getCurrentUserEmail();
		return ResponseEntity.status(HttpStatus.CREATED).body(eventService.createEvent(request, email));
	}

	@GetMapping("/{clubId}/all")
	public PagedResponse<EventSummaryDTO> getAllEvents(@PathVariable Long clubId,
			@PageableDefault(size = 10, sort = "createdAt") Pageable pageable) {
		return eventService.getAllEventsOfClub(clubId, pageable);
	}

	@PutMapping("/update")
	@PreAuthorize("hasRole('ORGANIZER')")
	public ResponseEntity<SuccesssResponseDTO> updateEvent(@Valid @RequestBody UpdateEventRequestDTO request) {
		String currentUserEmail = securityUtils.getCurrentUserEmail();
		return ResponseEntity.status(HttpStatus.OK).body(eventService.updateEvent(request, currentUserEmail));
	}

	@DeleteMapping("/{clubId}/{eventId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PreAuthorize("hasRole('ORGANIZER')")
	public void deleteClub(@PathVariable Long clubId, @PathVariable Long eventId) {
		String email = securityUtils.getCurrentUserEmail();
		eventService.deleteEvent(clubId, eventId, email);
	}

	@GetMapping("/search")
    public ResponseEntity<Page<EventSummaryDTO>> searchEvents(EventSearchFilterDTO filter,
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "startTime") String sortBy) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        
        Page<Event> eventPage = eventService.searchEvents(filter, pageable);

        Page<EventSummaryDTO> dtoPage = eventPage.map(event -> new EventSummaryDTO(
                event.getId(),
                event.getTitle(),
                event.getLocation(),
                event.getStatus(),
                event.getStartTime(),
                event.getEndTime(),
                event.getClub().getName(),
                event.getDescription(),
                event.getCapacity()
        ));

        return ResponseEntity.ok(dtoPage);
    }
	
	@PostMapping("/{eventId}/banner")
	@PreAuthorize("hasRole('ORGANIZER')")
	public ResponseEntity<MessageResponse> uploadBanner(
	        @PathVariable Long eventId,
	        @RequestParam("file") MultipartFile file) {

	    if (!List.of("image/jpeg", "image/png").contains(file.getContentType())) {
	        throw new RuntimeException("Only JPG and PNG images are allowed");
	    }

	    String imageUrl = storageService.uploadFile(file, "events");

	    eventService.updateEventBanner(eventId, imageUrl, securityUtils.getCurrentUserEmail());

	    return ResponseEntity.ok(new MessageResponse("Banner uploaded successfully: " + imageUrl));
	}
	

	@GetMapping("/{eventId}")
    public ResponseEntity<EventDetailsDTO> getEventById(@PathVariable Long eventId) {
        return ResponseEntity.ok(eventService.getEventDetails(eventId));
    }
	
	@PutMapping("/{id}")
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity<EventDto> updateEvent(@PathVariable Long id, @RequestBody EventDto dto) {
        return ResponseEntity.ok(eventService.updateEvent(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity<Void> cancelEvent(@PathVariable Long id) {
        eventService.cancelEvent(id);
        return ResponseEntity.noContent().build();
    }
	

}
