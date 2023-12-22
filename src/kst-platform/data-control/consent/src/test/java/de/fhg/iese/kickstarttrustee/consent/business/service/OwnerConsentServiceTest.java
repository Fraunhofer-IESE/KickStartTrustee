package de.fhg.iese.kickstarttrustee.consent.business.service;

import de.fhg.iese.kickstarttrustee.consent.business.model.Consent;
import de.fhg.iese.kickstarttrustee.consent.business.model.ConsentOperationType;
import de.fhg.iese.kickstarttrustee.consent.business.model.ConsentPermissions;
import de.fhg.iese.kickstarttrustee.consent.business.model.ConsentPurpose;
import de.fhg.iese.kickstarttrustee.consent.business.model.ConsentStatus;
import de.fhg.iese.kickstarttrustee.consent.business.model.ConsentUser;
import de.fhg.iese.kickstarttrustee.consent.persistence.entity.ConsentEntity;
import de.fhg.iese.kickstarttrustee.consent.persistence.repository.ConsentRepository;
import de.fhg.iese.kickstarttrustee.event.IEventbus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.LenientStubber;
import org.springframework.data.domain.Sort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Clock;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
public class OwnerConsentServiceTest {
	@Mock
	private IEventbus eventbusMock;
	@Mock
	private ConsentUserService consentUserServiceMock;
	@Mock
	private ConsentRepository consentRepositoryMock;

	@Test
	void getAllConsentsEmptyTest() {
		final String ownerId = "5b944f44-e3df-401e-9f37-0bd4813b4e2d";
		final LenientStubber stubber = lenient();
		stubber.when(consentUserServiceMock.getCurrentUserId()).thenReturn(Mono.just(ownerId));
		stubber.when(consentRepositoryMock.findAllByOwnerId(eq(ownerId), any(Sort.class)))
				.thenReturn(Flux.empty());

		final OwnerConsentService consentService = new OwnerConsentService(eventbusMock, consentUserServiceMock,
				consentRepositoryMock);
		final Flux<Consent> result = consentService.getAllConsents(Optional.empty());

		StepVerifier.create(result)
				.verifyComplete();
	}

	@Test
	void getAllConsents() {
		final String consentRequestId = "3e7648d8-5fb7-49a3-980e-8d66e43c46d9";
		final String ownerId = "5b944f44-e3df-401e-9f37-0bd4813b4e2d";
		final ConsentUser consentUser = new ConsentUser("685d489a-0856-46eb-983e-2ae31aae66e8", "testuser");
		final String dataUsageStatement = "I will use your data for a research study";
		final ConsentOperationType operationType = ConsentOperationType.DATA_CONSUMPTION;
		final String dataItemType = "location";
		final Consent consent = new Consent(consentRequestId, consentUser, ownerId,
				new ConsentPermissions(Set.of(dataItemType), operationType), dataUsageStatement, ConsentPurpose.RESEARCH);

		final LenientStubber stubber = lenient();
		stubber.when(consentUserServiceMock.getCurrentUserId()).thenReturn(Mono.just(ownerId));
		stubber.when(consentRepositoryMock.findAllByOwnerId(eq(ownerId), any(Sort.class)))
				.thenReturn(Flux.just(consent.toEntity()));

		final OwnerConsentService consentService = new OwnerConsentService(eventbusMock, consentUserServiceMock,
				consentRepositoryMock);
		final Flux<Consent> result = consentService.getAllConsents(Optional.empty());

		StepVerifier.create(result)
				.consumeNextWith(c -> {
					assertEquals(consentRequestId, c.getConsentRequestId());
					assertEquals(consentUser, c.getActor());
					assertEquals(ownerId, c.getOwnerId());
					c.getConsentPermissions().forEach((key, value) -> {
						assertEquals(dataItemType, key);
						assertIterableEquals(Set.of(operationType), value);
					});
					assertEquals(Optional.of(dataUsageStatement), c.getDataUsageStatement());
					assertEquals(Optional.of(ConsentPurpose.RESEARCH), c.getPurpose());
					assertEquals(ConsentStatus.ACTIVE, c.getStatus());
					assertEquals(0L, c.getVersion());
				})
				.verifyComplete();
	}

	@Test
	void getConsentByIdTest() {
		final String consentId = "83c0025a-3596-4f84-8c20-2a2da1873b28";
		final String consentRequestId = "3e7648d8-5fb7-49a3-980e-8d66e43c46d9";
		final String ownerId = "5b944f44-e3df-401e-9f37-0bd4813b4e2d";
		final ConsentUser consentUser = new ConsentUser("685d489a-0856-46eb-983e-2ae31aae66e8", "testuser");
		final String dataUsageStatement = "I will use your data for a research study";
		final ConsentOperationType operationType = ConsentOperationType.DATA_CONSUMPTION;
		final ConsentStatus status = ConsentStatus.ACTIVE;
		final Instant currentTime = Instant.now(Clock.systemUTC());
		final String dataItemType = "location";

		final ConsentEntity entity = new ConsentEntity(consentId, consentRequestId, consentUser.toEntity(),
				ownerId, Map.of(dataItemType, Set.of(operationType.name())), dataUsageStatement,
				ConsentPurpose.RESEARCH.name(), currentTime, 0L, status.name());

		final LenientStubber stubber = lenient();
		stubber.when(consentUserServiceMock.getCurrentUserId()).thenReturn(Mono.just(ownerId));
		stubber.when(consentRepositoryMock.findFirstByIdAndOwnerId(eq(consentId), eq(ownerId)))
				.thenReturn(Mono.just(entity));

		final OwnerConsentService consentService = new OwnerConsentService(eventbusMock, consentUserServiceMock,
				consentRepositoryMock);
		final Mono<Consent> result = consentService.getConsentById(consentId);

		StepVerifier.create(result)
				.consumeNextWith(c -> {
					assertEquals(consentId, c.getId());
					assertEquals(consentRequestId, c.getConsentRequestId());
					assertEquals(consentUser, c.getActor());
					assertEquals(ownerId, c.getOwnerId());
					c.getConsentPermissions().forEach((key, value) -> {
						assertEquals(dataItemType, key);
						assertIterableEquals(Set.of(operationType), value);
					});
					assertEquals(Optional.of(dataUsageStatement), c.getDataUsageStatement());
					assertEquals(Optional.of(ConsentPurpose.RESEARCH), c.getPurpose());
					assertEquals(ConsentStatus.ACTIVE, c.getStatus());
					assertEquals(0L, c.getVersion());
				})
				.verifyComplete();
	}

}
