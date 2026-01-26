package com.eb.eventsbridge.modules.core.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.eb.eventsbridge.modules.auth.entity.User;
import com.eb.eventsbridge.modules.auth.repository.UserRepository;
import com.eb.eventsbridge.modules.core.dto.registration.AttendeeDTO;
import com.eb.eventsbridge.modules.core.dto.registration.RegistrationListDTO;
import com.eb.eventsbridge.modules.core.dto.registration.RegistrationResponseDTO;
import com.eb.eventsbridge.modules.core.entity.Event;
import com.eb.eventsbridge.modules.core.entity.Registration;
import com.eb.eventsbridge.modules.core.entity.RegistrationStatus;
import com.eb.eventsbridge.modules.core.repository.EventRepository;
import com.eb.eventsbridge.modules.core.repository.RegistrationRepository;
import com.eb.eventsbridge.shared.service.EmailService;
import com.eb.eventsbridge.shared.utils.SecurityUtils;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RegistrationService {

	private final RegistrationRepository registrationRepo;
	private final EventRepository eventRepo;
	private final UserRepository userRepo;
	private final EmailService emailService;
	private final SecurityUtils securityUtils;

	@Transactional
	public void registerForEvent(Long eventId, String email) {
		User user = userRepo.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

		Event event = eventRepo.findById(eventId).orElseThrow(() -> new RuntimeException("Event not found"));

		Optional<Registration> existingReg = registrationRepo.findByEventAndUser(event, user);

		if (existingReg.isPresent()) {
			Registration reg = existingReg.get();

			if (reg.getStatus() == RegistrationStatus.CONFIRMED || reg.getStatus() == RegistrationStatus.WAITLISTED) {
				throw new RuntimeException("You are already registered for this event");
			}

			if (reg.getStatus() == RegistrationStatus.CANCELLED) {
				reg.setStatus(RegistrationStatus.CONFIRMED);
				reg.setRegisteredAt(LocalDateTime.now()); // Update timestamp to now
				registrationRepo.save(reg);

				Map<String, Object> emailVariables = new HashMap<>();
				emailVariables.put("studentName", user.getFullName());
				emailVariables.put("eventTitle", event.getTitle());
				emailVariables.put("eventLocation", event.getLocation());
				emailVariables.put("eventDate", event.getStartTime().toString().replace("T", " "));

				emailService.sendHtmlEmail(user.getEmail(), "Ticket Confirmed: " + event.getTitle(),
						"registration-confirmation",
						emailVariables);

				return; 
			}
		}

		if (event.getCapacity() <= 0) { 
			throw new RuntimeException("Event is full");
		}

		Registration newReg = new Registration();
		newReg.setEvent(event);
		newReg.setUser(user);
		newReg.setStatus(RegistrationStatus.CONFIRMED);
		newReg.setRegisteredAt(LocalDateTime.now());

		registrationRepo.save(newReg);
		/*
		 * When registrationRepo.save(registration) is called, because we added @Version
		 * to the Event entity, JPA will check if the Event version is still the same.
		 * If another user registered successfully while this method was running, this
		 * save will fail.
		 */


		Map<String, Object> emailVariables = new HashMap<>();
		emailVariables.put("studentName", user.getFullName());
		emailVariables.put("eventTitle", event.getTitle());
		emailVariables.put("eventLocation", event.getLocation());
		emailVariables.put("eventDate", event.getStartTime().toString().replace("T", " "));

		emailService.sendHtmlEmail(user.getEmail(), "Ticket Confirmed: " + event.getTitle(),
				"registration-confirmation",
				emailVariables);

	}

	@Transactional
	public Page<RegistrationResponseDTO> getMyRegistrations(String email, Pageable pageable) {
		Page<Registration> registrations = registrationRepo.findByUserEmailAndStatus(
	            email, 
	            RegistrationStatus.CONFIRMED, 
	            pageable
	        );
		return registrations.map(reg -> RegistrationResponseDTO.builder().registrationId(reg.getId())
				.eventId(reg.getEvent().getId()).eventTitle(reg.getEvent().getTitle())
				.eventStartTime(reg.getEvent().getStartTime()).venue(reg.getEvent().getLocation())
				.status(reg.getStatus().name()).registeredAt(reg.getRegisteredAt()).build());
	}

	@Transactional
	public void cancelRegistration(Long registrationId, String userEmail) {
		Registration registration = registrationRepo.findById(registrationId)
				.orElseThrow(() -> new RuntimeException("Registration not found"));

		if (registration.getEvent().getStartTime().isBefore(LocalDateTime.now().plusHours(24))) {
			throw new RuntimeException("Cancellations must be made at least 24 hours in advance");
		}

		if (!registration.getUser().getEmail().equals(userEmail)) {
			throw new RuntimeException("You are not authorized to cancel this registration");
		}

		// IDEMPOTENCY: Check if already cancelled
		if (registration.getStatus() == RegistrationStatus.CANCELLED) {
			throw new RuntimeException("Registration is already cancelled");
		}

		registration.setStatus(RegistrationStatus.CANCELLED);

		registrationRepo.save(registration);
	}

	@Transactional 
	public Page<RegistrationListDTO> getMyRegistrations(Pageable pageable, String email) {

		Page<Registration> regs = registrationRepo.findActiveRegistrations(email, pageable);

		return regs.map(reg -> new RegistrationListDTO(reg.getId(), reg.getStatus(), reg.getRegisteredAt(),
				reg.getEvent().getId(), reg.getEvent().getTitle(), reg.getEvent().getLocation(),
				reg.getEvent().getStartTime(), reg.getEvent().getBannerImageUrl()));
	}

	public List<AttendeeDTO> getEventAttendees(Long eventId) {
		String currentUserEmail = securityUtils.getCurrentUserEmail();
		Event event = eventRepo.findById(eventId).orElseThrow(() -> new RuntimeException("Event not found"));

		if (!event.getClub().getCreator().getEmail().equals(currentUserEmail)) {
			throw new RuntimeException("Unauthorized");
		}

		return registrationRepo.findByEventId(eventId).stream().map(reg -> new AttendeeDTO(reg.getId(),
				reg.getUser().getFullName(), reg.getUser().getEmail(), reg.getRegisteredAt(), reg.getStatus(), reg.getUser().getId() ))
				.toList();
	}

}