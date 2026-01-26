package com.eb.eventsbridge.modules.core.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eb.eventsbridge.modules.core.dto.feedback.FeedbackResponseDTO;
import com.eb.eventsbridge.modules.core.repository.FeedbackService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/feedback")
@RequiredArgsConstructor
@Tag(name = "Feedback-Events", description = "Endpoints posting feedback on events")
public class FeedbackController {

	private final FeedbackService feedbackService;

	@GetMapping("/events/{eventId}/")
	public ResponseEntity<Page<FeedbackResponseDTO>> getFeedback(@PathVariable Long eventId,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {

		Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

		Page<FeedbackResponseDTO> feedbackPage = feedbackService.getEventFeedback(eventId, pageable);

		return ResponseEntity.ok(feedbackPage);
	}
}
