package de.fhg.iese.kickstarttrustee.diseasewarning.business.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimAccessor;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;

@Service
public class DiseaseWarningUserService {
    Mono<String> getCurrentUserId() {
        return ReactiveSecurityContextHolder.getContext().mapNotNull(context -> {
            Authentication authentication = context.getAuthentication();
            return authentication.getPrincipal();
        }).cast(Jwt.class).map(JwtClaimAccessor::getSubject);
    }
}
