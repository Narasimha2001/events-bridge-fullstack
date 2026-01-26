package com.eb.eventsbridge.shared.utils;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

/**

What is stored in SecurityContextHolder?

❌ Not your entity
✅ A UserDetails object (usually Spring’s User)

| Scenario      | principal         |
| ------------- | ----------------- |
| Not logged in | `"anonymousUser"` |
| Logged in     | `UserDetails`     |
| JWT           | `UserDetails`     |
| OAuth         | OAuth principal   |

*/


@Component
@Slf4j
public class SecurityUtils {
	
	@PostConstruct()
	public void init() {
		log.info("Inside securityUtils filter");
	}

    public String getCurrentUserEmail() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }
}
