package com.eb.eventsbridge.shared.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class EmailService {

	private final JavaMailSender mailSender;
	private final TemplateEngine templateEngine;
	
	@Async // <---  Runs in a background thread.
	public void sendSimpleEmail(String to, String subject, String body) {
		try {
			log.info("Sending email to {}", to);

			SimpleMailMessage message = new SimpleMailMessage();
			message.setFrom("no-reply@eventsbridge.com");
			message.setTo(to);
			message.setSubject(subject);
			message.setText(body);

			mailSender.send(message);

			log.info("Email sent successfully to {}", to);
		} catch (Exception e) {
			log.error("Failed to send email to {}: {}", to, e.getMessage());
		}
	}

	@Async
	public void sendHtmlEmail(String to, String subject, String templateName, Map<String, Object> variables) {
		try {

			Context context = new Context();
			context.setVariables(variables);

			String htmlContent = templateEngine.process(templateName, context);

			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

			helper.setFrom("no-reply@eventsbridge.com");
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(htmlContent, true);

			mailSender.send(message);
			log.info("HTML Email sent successfully to {}", to);
		} catch (Exception e) {
			log.error("Failed to send HTML email to {}: {}", to, e.getMessage());
		}
	}
	
	
	@Async
    public void sendCancellationEmail(String toEmail, String studentName, String eventTitle) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("studentName", studentName);
        variables.put("eventTitle", eventTitle);

        
        sendHtmlEmail(
            toEmail, 
            "Important: Event Cancelled - " + eventTitle, 
            "event-cancellation", 
            variables
        );
    }
	

}