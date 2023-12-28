package de.fhg.iese.kickstarttrustee.owner.business.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.test.context.TestSecurityContextHolder;
import org.springframework.security.test.context.support.ReactorContextTestExecutionListener;
import org.springframework.test.context.TestExecutionListener;

import de.fhg.iese.kickstarttrustee.event.IEventbus;
import de.fhg.iese.kickstarttrustee.owner.business.model.IdpProfile;
import de.fhg.iese.kickstarttrustee.owner.business.model.OwnerProfile;
import de.fhg.iese.kickstarttrustee.owner.persistence.entity.OwnerProfileEntity;
import de.fhg.iese.kickstarttrustee.owner.persistence.repositories.OwnerProfileRepository;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
public class OwnerProfileServiceTest {
    private static final String AUTH0_TOKEN = "token";
    private static final String SUB = "sub";
    private static final String USER_ID = "testuser";

    @Mock
    private IEventbus eventbus;
    @Mock
    private OwnerIdpService ownerIdpService;
    @Mock
    private OwnerProfileRepository repository;
    @Mock
    private Authentication authentication;

    private final TestExecutionListener reactorContextTestExecutionListener = new ReactorContextTestExecutionListener();

    private Jwt createJwt() {
        Instant issuedAt = Instant.now();
        Instant expiresAt = Instant.now().plusSeconds(30);
        Map<String, Object> headers = Map.of("alg", "none");
        Map<String, Object> claims = Map.of(SUB, USER_ID);
        return new Jwt(AUTH0_TOKEN, issuedAt, expiresAt, headers, claims);
    }

    @BeforeEach
    public void setUp() throws Exception {
        when(authentication.getPrincipal()).thenReturn(createJwt());
        TestSecurityContextHolder.setAuthentication(authentication);
        reactorContextTestExecutionListener.beforeTestMethod(null);
    }

    @AfterEach
    public void tearDown() throws Exception {
        reactorContextTestExecutionListener.afterTestMethod(null);
    }

    @Test
    void test_createOwnProfile() {
        final String objectId = new ObjectId().toString();
        final Instant createdAt = Instant.now();

        when(ownerIdpService.getOwnIdpProfile())
                .thenReturn(Mono.just(new IdpProfile("testUserId", "test@fake.de", "test", "test")));
        when(repository.save(any(OwnerProfileEntity.class)))
                .thenReturn(Mono.just(new OwnerProfileEntity(objectId, USER_ID, "en-US", createdAt)));

        final OwnerProfileService service = new OwnerProfileService(eventbus, ownerIdpService, repository);
        final Mono<OwnerProfile> profile = service.createOwnProfile("en-US");

        StepVerifier.create(profile)
                .consumeNextWith(ownerProfile -> {
                    assertEquals(ownerProfile.getId(), Optional.of(objectId));
                    assertEquals(ownerProfile.getUserId(), USER_ID);
                    assertNotNull(ownerProfile.getIdpProfile());
                    assertEquals(ownerProfile.getPreferredLanguage(), Optional.of("en-US"));
                })
                .verifyComplete();

        verify(repository).save(any(OwnerProfileEntity.class));
    }

    @Test
    void test_updateOwnProfile() {
        final String profileId = new ObjectId().toString();
        final Instant createdAt = Instant.now();

        when(ownerIdpService.getOwnIdpProfile())
                .thenReturn(Mono.just(new IdpProfile("testUserId", "test@fake.de", "test", "test")));
        when(repository.findById(any(String.class))).thenReturn(Mono.just(new OwnerProfileEntity(profileId, USER_ID, "en-US", createdAt)));
        when(repository.save(any(OwnerProfileEntity.class)))
                .thenAnswer(a -> {
                    final String preferredLanguage = a.getArgument(0, OwnerProfileEntity.class).preferredLanguage();
                    return Mono.just(new OwnerProfileEntity(profileId, USER_ID, preferredLanguage, createdAt));
                });

        final OwnerProfileService service = new OwnerProfileService(eventbus, ownerIdpService, repository);
        final Mono<OwnerProfile> profile = service.updateOwnProfile(profileId, "de-DE");

        StepVerifier.create(profile)
                .consumeNextWith(ownerProfile -> {
                    assertEquals(ownerProfile.getId(), Optional.of(profileId));
                    assertEquals(ownerProfile.getUserId(), USER_ID);
                    assertNotNull(ownerProfile.getIdpProfile());
                    assertEquals(ownerProfile.getPreferredLanguage(), Optional.of("de-DE"));
                })
                .verifyComplete();
    }
}
