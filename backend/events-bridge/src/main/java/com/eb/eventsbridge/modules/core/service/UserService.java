package com.eb.eventsbridge.modules.core.service;

import org.springframework.stereotype.Service;

import com.eb.eventsbridge.modules.auth.entity.User;
import com.eb.eventsbridge.modules.auth.repository.UserRepository;
import com.eb.eventsbridge.modules.core.dto.user.UserProfileDto;
import com.eb.eventsbridge.modules.core.repository.EventRepository;
import com.eb.eventsbridge.modules.core.repository.RegistrationRepository;
import com.eb.eventsbridge.shared.utils.SecurityUtils;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	
	private final SecurityUtils securityUtils;
	private final UserRepository userRepo;
	private final RegistrationRepository registrationRepo;
	private final EventRepository eventRepo;

	@Transactional
    public UserProfileDto getCurrentUserProfile() {
        String email = securityUtils.getCurrentUserEmail();
        User user = userRepo.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

        long activityCount = 0;
        System.out.println(user.getRole().getName().equals("ORGANIZER"));
        if (user.getRole().getName().equals("STUDENT")) {
            activityCount = registrationRepo.countByUser(user);
		} else if (user.getRole().getName().equals("ORGANIZER")) {
			System.out.println("hbjdhs");
            activityCount = eventRepo.countByCreatorEmail(email);
        }

        return new UserProfileDto(
            user.getId(),
            user.getFullName(),
            user.getEmail(),
            user.getRole().getName(),
            activityCount
        );
    }

}
