package de.fhg.iese.kickstarttrustee.consent.business.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;

import java.time.Clock;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.LenientStubber;
import org.springframework.data.domain.Sort;

import de.fhg.iese.kickstarttrustee.common.business.exception.DataOwnerNotFoundException;
import de.fhg.iese.kickstarttrustee.consent.business.model.ConsentOperationType;
import de.fhg.iese.kickstarttrustee.consent.business.model.ConsentRequest;
import de.fhg.iese.kickstarttrustee.consent.business.model.ConsentPurpose;
import de.fhg.iese.kickstarttrustee.consent.business.model.ConsentRequestStatus;
import de.fhg.iese.kickstarttrustee.consent.business.model.ConsentUser;
import de.fhg.iese.kickstarttrustee.consent.persistence.entity.ConsentRequestEntity;
import de.fhg.iese.kickstarttrustee.consent.persistence.repository.ConsentRequestRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
public class ConsentRequestServiceTest {
        @Mock
        private ConsentDataItemTypeService dataItemTypeServiceMock;
        @Mock
        private ConsentOwnerService consentOwnerServiceMock;
        @Mock
        private ConsentService consentServiceMock;
        @Mock
        private ConsentUserService consentUserServiceMock;
        @Mock
        private ConsentRequestRepository consentRequestRepositoryMock;

        @Test
        void requestConsentTest() {
                final String ownerId = "5a138af8-5d08-4a21-b07d-78b13558fdfb";
                final String consentUserId = "912f2121-c13d-40f6-b6fc-c2b75f740fbf";
                final ConsentUser consentUser = new ConsentUser(consentUserId, "testname");
                final Set<String> dataItemTypes = Set.of("location");
                final ConsentOperationType operationType = ConsentOperationType.DATA_CONSUMPTION;
                final String dataUsageStatement = "I will use your data for anything I like";

                final LenientStubber stubber = lenient();
                stubber.when(dataItemTypeServiceMock.existsAllDataItemTypes(eq(dataItemTypes))).thenReturn(Mono.just(Boolean.TRUE));
                stubber.when(consentOwnerServiceMock.existsByUserId(eq(ownerId))).thenReturn(Mono.just(Boolean.TRUE));
                stubber.when(consentServiceMock.noActiveConsentExists(eq(consentUser), eq(ownerId))).thenReturn(Mono.just(Boolean.TRUE));
                stubber.when(consentUserServiceMock.getCurrentUserAsConsentRequester())
                                .thenReturn(Mono.just(consentUser));
                stubber.when(consentUserServiceMock.hasUserRole(eq(ConsentUserService.CONSUMER_ROLE)))
                                .thenReturn(Mono.just(Boolean.TRUE));
				stubber.when(consentRequestRepositoryMock.existsByRequester_IdAndOwnerIdAndStatus(any(), any(), any()))
					.thenReturn(Mono.just(Boolean.FALSE));
                stubber.when(consentRequestRepositoryMock.save(any(ConsentRequestEntity.class)))
                                .thenAnswer(a -> Mono.just(a.getArgument(0)));

                final ConsentRequestService consentRequestService = new ConsentRequestService(
                                dataItemTypeServiceMock, consentOwnerServiceMock, consentServiceMock,
                                consentUserServiceMock, consentRequestRepositoryMock);
                final Mono<ConsentRequest> result = consentRequestService.requestConsentForRole(
                                ConsentUserService.CONSUMER_ROLE, ownerId, dataItemTypes,
                                operationType, dataUsageStatement, ConsentPurpose.RESEARCH);

                StepVerifier.create(result)
                                .consumeNextWith(consentRequest -> {
                                        assertNotNull(consentRequest.getCreatedAt());
                                        assertEquals(ownerId, consentRequest.getOwnerId());
										consentRequest.getConsentPermissions().forEach((key, value) -> {
											assertEquals("location", key);
											assertIterableEquals(Set.of(ConsentOperationType.DATA_CONSUMPTION), value);
										});
                                        assertEquals(Optional.of(dataUsageStatement),
                                                        consentRequest.getDataUsageStatement());
                                        assertEquals(Optional.of(ConsentPurpose.RESEARCH),
                                                        consentRequest.getPurpose());
                                        assertEquals(ConsentRequestStatus.PENDING, consentRequest.getStatus());
                                        assertEquals(consentUser, consentRequest.getRequester());
                                        assertEquals(0L, consentRequest.getVersion());
                                })
                                .verifyComplete();
        }

        @Test
        void requestConsentNoUserTest() {
                final String consentUserId = "912f2121-c13d-40f6-b6fc-c2b75f740fbf";
                final String ownerId = "5a138af8-5d08-4a21-b07d-78b13558fdfb";
                final ConsentUser consentUser = new ConsentUser(consentUserId, "testname");
                final Set<String> dataItemTypes = Set.of("location");
                final String dataUsageStatement = "I will use your data for anything I like";

                final LenientStubber stubber = lenient();
                stubber.when(dataItemTypeServiceMock.existsAllDataItemTypes(eq(dataItemTypes))).thenReturn(Mono.just(Boolean.TRUE));
                stubber.when(consentUserServiceMock.getCurrentUserAsConsentRequester())
                                .thenReturn(Mono.just(consentUser));
                stubber.when(consentUserServiceMock.hasUserRole(eq(ConsentUserService.CONSUMER_ROLE)))
                                .thenReturn(Mono.just(Boolean.TRUE));
                stubber.when(consentOwnerServiceMock.existsByUserId(eq(ownerId))).thenReturn(Mono.just(Boolean.FALSE));

                final ConsentRequestService consentRequestService = new ConsentRequestService(dataItemTypeServiceMock,
                                consentOwnerServiceMock, consentServiceMock, consentUserServiceMock,
                                consentRequestRepositoryMock);
                final Mono<ConsentRequest> result = consentRequestService.requestConsentForRole(
                                ConsentUserService.CONSUMER_ROLE, ownerId, dataItemTypes,
                                ConsentOperationType.DATA_CONSUMPTION, dataUsageStatement, ConsentPurpose.OTHER);

                StepVerifier.create(result)
                                .verifyError(DataOwnerNotFoundException.class);
        }

        @Test
        void getAllRequestedConsentRequestsEmptyTest() {
                final String consentUserId = "0e0f7945-b8ef-4fa9-b888-d7a2d1b6be99";

                final LenientStubber stubber = lenient();
                stubber.when(consentUserServiceMock.getCurrentClientId()).thenReturn(Mono.just(consentUserId));
                stubber.when(consentUserServiceMock.hasUserRole(eq(ConsentUserService.CONSUMER_ROLE))).thenReturn(Mono.just(Boolean.TRUE));
                stubber.when(consentRequestRepositoryMock.findAllByRequester_Id(eq(consentUserId), any(Sort.class))).thenReturn(Flux.empty());

                final ConsentRequestService consentRequestService = new ConsentRequestService(dataItemTypeServiceMock,
						consentOwnerServiceMock, consentServiceMock, consentUserServiceMock,
						consentRequestRepositoryMock);
                final Flux<ConsentRequest> result = consentRequestService.getAllConsentRequestsForRole(ConsentUserService.CONSUMER_ROLE, Optional.empty());

                StepVerifier.create(result)
                                .verifyComplete();
        }

        @Test
        void getAllRequestedConsentRequestsTest() {
                final String consentUserId = "0e0f7945-b8ef-4fa9-b888-d7a2d1b6be99";
                final ConsentUser consentUser = new ConsentUser(consentUserId, "testname");
                final String ownerId = "33c09d97-43e5-467c-9356-8a99747aa020";
                final ConsentOperationType operationType = ConsentOperationType.DATA_CONSUMPTION;
                final String consentId = "48226de5-26c7-48c5-8848-bebc08f9e01c";
                final ConsentRequestStatus status = ConsentRequestStatus.PENDING;
                final String dataUsageStatement = "I will use your data for a research study";
                final ConsentPurpose purpose = ConsentPurpose.RESEARCH;
                final Instant currentTime = Instant.now(Clock.systemUTC());

                final LenientStubber stubber = lenient();
                stubber.when(consentUserServiceMock.getCurrentClientId()).thenReturn(Mono.just(consentUserId));
                stubber.when(consentUserServiceMock.hasUserRole(eq(ConsentUserService.CONSUMER_ROLE)))
                                .thenReturn(Mono.just(Boolean.TRUE));
                stubber.when(consentRequestRepositoryMock.findAllByRequester_Id(eq(consentUserId), any(Sort.class)))
                                .thenReturn(Flux.just(
                                                new ConsentRequestEntity(consentId, consentUser.toEntity(), ownerId,
														Map.of("email", Set.of(operationType.name())),
														dataUsageStatement, purpose.name(), currentTime, 0,
														status.name())));

                final ConsentRequestService consentRequestService = new ConsentRequestService(dataItemTypeServiceMock,
                                consentOwnerServiceMock, consentServiceMock, consentUserServiceMock,
                                consentRequestRepositoryMock);
                final Flux<ConsentRequest> result = consentRequestService.getAllConsentRequestsForRole(
                                ConsentUserService.CONSUMER_ROLE, Optional.empty());

                StepVerifier.create(result)
                                .consumeNextWith(consentRequest -> {
                                        assertEquals(consentId, consentRequest.getId());
                                        assertEquals(consentUser, consentRequest.getRequester());
                                        assertEquals(ownerId, consentRequest.getOwnerId());
										consentRequest.getConsentPermissions().forEach((key, value) -> {
											assertEquals("email", key);
											assertIterableEquals(Set.of(operationType), value);
										});
										assertEquals(currentTime, consentRequest.getCreatedAt());
                                        assertEquals(status, consentRequest.getStatus());
                                        assertEquals(0L, consentRequest.getVersion());
                                })
                                .verifyComplete();
        }
}
