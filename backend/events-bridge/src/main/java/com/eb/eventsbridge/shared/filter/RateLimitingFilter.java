package com.eb.eventsbridge.shared.filter;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RateLimitingFilter extends OncePerRequestFilter {

	private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

	private Bucket createNewBucket() {
	    return Bucket.builder()
	            .addLimit(Bandwidth.builder()
	                    .capacity(10) 
	                    .refillGreedy(10, Duration.ofMinutes(1))
	                    .build())
	            .build();
	}
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		// Use Client IP as the unique key
		String clientIp = request.getRemoteAddr();
		Bucket bucket = buckets.computeIfAbsent(clientIp, k -> createNewBucket());

		if (bucket.tryConsume(1)) {
			filterChain.doFilter(request, response);
		} else {
			response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
			response.setContentType("application/json");
			response.getWriter().write("{ \"error\": \"Too many requests. Please try again in a minute.\" }");
		}
	}
}