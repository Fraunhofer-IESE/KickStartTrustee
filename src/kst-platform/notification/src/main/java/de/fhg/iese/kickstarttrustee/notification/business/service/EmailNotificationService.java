package de.fhg.iese.kickstarttrustee.notification.business.service;

import de.fhg.iese.kickstarttrustee.common.business.service.IReactiveNotificationService;
import de.fhg.iese.kickstarttrustee.notification.business.exception.NotificationSettingsNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class EmailNotificationService implements IReactiveNotificationService {
	private static final Logger log = LoggerFactory.getLogger(EmailNotificationService.class);
	private static final String SENDER_EMAIL = "noreply@kickstarttrustee.de";

	private final NotificationSettingsService notificationSettingsService;
	private final JavaMailSender javaMailSender;

	public EmailNotificationService(NotificationSettingsService notificationSettingsService, JavaMailSender javaMailSender) {
		this.notificationSettingsService = notificationSettingsService;
		this.javaMailSender = javaMailSender;
	}

	@Override
	public Mono<Void> notifyOwner(String ownerId, String title, String message) {
		return notificationSettingsService.getOwnerMail(ownerId)
				.doOnError(NotificationSettingsNotFoundException.class, throwable -> {
					log.info("Notification settings for owner not found: ownerId={}", ownerId);
				})
				.onErrorComplete(NotificationSettingsNotFoundException.class)
				.doOnNext(ownerMail -> {
					try {
						final SimpleMailMessage mailMessage = new SimpleMailMessage();
						mailMessage.setFrom(SENDER_EMAIL);
						mailMessage.setTo(ownerMail);
						mailMessage.setSubject(title);
						mailMessage.setText(message);
						javaMailSender.send(mailMessage);
					} catch (MailException e) {
						log.warn("Could not send email", e);
					}
				}).then();
	}
}
