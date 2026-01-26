package com.eb.eventsbridge.modules.core.repository;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.eb.eventsbridge.modules.auth.entity.User;
import com.eb.eventsbridge.modules.auth.repository.UserRepository;
import com.eb.eventsbridge.modules.core.dto.feedback.FeedbackResponseDTO;
import com.eb.eventsbridge.modules.core.dto.feedback.SubmitFeedbackRequestDTO;
import com.eb.eventsbridge.modules.core.entity.Event;
import com.eb.eventsbridge.modules.core.entity.EventFeedback;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FeedbackService {

	private final FeedbackRepository feedbackRepo;
	private final RegistrationRepository registrationRepo;
	private final EventRepository eventRepo;
	private final UserRepository userRepo;

	@Transactional
	public void submitFeedback(Long eventId, String email, SubmitFeedbackRequestDTO request) {
		User user = userRepo.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

		Event event = eventRepo.findById(eventId).orElseThrow(() -> new RuntimeException("Event not found"));

		if (event.getEndTime().isAfter(LocalDateTime.now())) {
			throw new RuntimeException("Cannot leave feedback for an ongoing or future event");
		}

		boolean wasRegistered = registrationRepo.existsByUserAndEvent(user, event);
		if (!wasRegistered) {
			throw new RuntimeException("Only registered attendees can leave feedback");
		}

		if (feedbackRepo.existsByUserAndEvent(user, event)) {
			throw new RuntimeException("You have already submitted feedback for this event");
		}

		EventFeedback feedback = new EventFeedback();
		feedback.setUser(user);
		feedback.setEvent(event);
		feedback.setRating(request.getRating());
		feedback.setComment(request.getComment());

		feedbackRepo.save(feedback);
	}

	@Transactional
	public Page<FeedbackResponseDTO> getEventFeedback(Long eventId, Pageable pageable) {
		if (!eventRepo.existsById(eventId)) {
			throw new RuntimeException("Event not found");
		}

		Page<EventFeedback> feedbacks = feedbackRepo.findByEventId(eventId, pageable);

		return feedbacks
				.map(f -> FeedbackResponseDTO.builder().feedbackId(f.getId()).userName(f.getUser().getFullName())
						.rating(f.getRating()).comment(f.getComment()).createdAt(f.getCreatedAt()).build());
	}
}