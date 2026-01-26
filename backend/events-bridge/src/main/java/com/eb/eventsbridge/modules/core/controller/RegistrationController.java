package com.eb.eventsbridge.modules.core.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eb.eventsbridge.modules.core.dto.registration.AttendeeDTO;
import com.eb.eventsbridge.modules.core.dto.registration.RegistrationListDTO;
import com.eb.eventsbridge.modules.core.service.RegistrationService;
import com.eb.eventsbridge.shared.utils.SecurityUtils;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/registrations")
@RequiredArgsConstructor
@Tag(name = "Registering for Event", description = "Endpoints for registering for events")
public class RegistrationController {

	private final SecurityUtils securityUtils;
	private final RegistrationService registrationService;

	@PostMapping("/event/{eventId}")
	@PreAuthorize("hasRole('STUDENT')")
	public ResponseEntity<Map<String, String>> register(@PathVariable Long eventId) {
		String currentUserEmail = securityUtils.getCurrentUserEmail();

		registrationService.registerForEvent(eventId, currentUserEmail);

		Map<String, String> response = new HashMap<>();
		response.put("message", "Registration successful!");

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PostMapping("/{registrationId}/cancel")
	@PreAuthorize("hasRole('STUDENT')")
	public ResponseEntity<Map<String, String>> cancelRegistration(@PathVariable Long registrationId) {
		String currentUserEmail = securityUtils.getCurrentUserEmail();

		registrationService.cancelRegistration(registrationId, currentUserEmail);

		return ResponseEntity.ok(Map.of("message", "Registration cancelled successfully"));
	}
	
	// http://localhost:8080/api/registrations/my-registrations?page=0&size=10
	@GetMapping("/my-registrations")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Page<RegistrationListDTO>> getMyRegistrations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
		String currentUserEmail = securityUtils.getCurrentUserEmail();

        
        Pageable pageable = PageRequest.of(page, size, Sort.by("registeredAt").descending());
        return ResponseEntity.ok(registrationService.getMyRegistrations(pageable, currentUserEmail));
    }
	
	@GetMapping("/event/{eventId}/attendees")
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity<List<AttendeeDTO>> getEventAttendees(@PathVariable Long eventId) {
        return ResponseEntity.ok(registrationService.getEventAttendees(eventId));
    }

}
