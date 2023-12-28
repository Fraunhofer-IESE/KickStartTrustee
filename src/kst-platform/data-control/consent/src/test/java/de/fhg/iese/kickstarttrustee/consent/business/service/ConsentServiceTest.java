package de.fhg.iese.kickstarttrustee.consent.business.service;

import de.fhg.iese.kickstarttrustee.consent.business.model.Consent;
import de.fhg.iese.kickstarttrustee.consent.business.model.ConsentOperationType;
import de.fhg.iese.kickstarttrustee.consent.business.model.ConsentPurpose;
import de.fhg.iese.kickstarttrustee.consent.business.model.ConsentRequest;
import de.fhg.iese.kickstarttrustee.consent.business.model.ConsentRequestStatus;
import de.fhg.iese.kickstarttrustee.consent.business.model.ConsentStatus;
import de.fhg.iese.kickstarttrustee.consent.business.model.ConsentUser;
import de.fhg.iese.kickstarttrustee.consent.persistence.entity.ConsentEntity;
import de.fhg.iese.kickstarttrustee.consent.persistence.entity.ConsentRequestEntity;
import de.fhg.iese.kickstarttrustee.consent.persistence.repository.ConsentRepository;
import de.fhg.iese.kickstarttrustee.event.IEventbus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.LenientStubber;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Clock;
import java.time.Instant;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
public class ConsentServiceTest {
        @Mock
        private IEventbus eventbusMock;
        @Mock
        private ConsentUserService consentUserServiceMock;
        @Mock
        private ConsentRepository consentRepositoryMock;


        @Test
        void createConsentFromConsentRequestTest() {
                final String consentRequestId = "3e7648d8-5fb7-49a3-980e-8d66e43c46d9";
                final String ownerId = "5b944f44-e3df-401e-9f37-0bd4813b4e2d";
                final String dataUsageStatement = "I will use your data for anything I like";
                final ConsentUser consentUser = new ConsentUser("685d489a-0856-46eb-983e-2ae31aae66e8", "testuser");
                final ConsentOperationType operationType = ConsentOperationType.DATA_CONSUMPTION;
                final ConsentRequestStatus status = ConsentRequestStatus.ACCEPTED;
                final ConsentPurpose purpose = ConsentPurpose.SERVICE;
                final Instant currentTime = Instant.now(Clock.systemUTC());
				final String dataItemType = "location";
                final ConsentRequestEntity consentRequestEntity = new ConsentRequestEntity(consentRequestId,
						consentUser.toEntity(), ownerId, Map.of(dataItemType, Set.of(operationType.name())),
						dataUsageStatement, purpose.name(), currentTime, 0L, status.name());
                final ConsentRequest consentRequest = new ConsentRequest(consentRequestEntity);

                final LenientStubber stubber = lenient();
                stubber.when(consentRepositoryMock.save(any(ConsentEntity.class)))
                                .thenAnswer(a -> Mono.just(a.getArgument(0)));
                stubber.when(consentUserServiceMock.getCurrentClientId()).thenReturn(Mono.just("test-client"));

                final ConsentService consentService = new ConsentService(eventbusMock, consentUserServiceMock,
                                consentRepositoryMock);
                final Mono<Consent> result = consentService.createConsentFromConsentRequest(consentRequest);

                StepVerifier.create(result)
                                .consumeNextWith(consent -> {
                                        assertEquals(consentRequestId, consent.getConsentRequestId());
                                        assertEquals(consentUser, consent.getActor());
                                        assertEquals(ownerId, consent.getOwnerId());
										consent.getConsentPermissions().forEach((key, value) -> {
											assertEquals(dataItemType, key);
											assertIterableEquals(Set.of(operationType), value);
										});
                                        assertEquals(ConsentStatus.ACTIVE, consent.getStatus());
                                        assertEquals(0L, consent.getVersion());
                                })
                                .verifyComplete();
        }
}
