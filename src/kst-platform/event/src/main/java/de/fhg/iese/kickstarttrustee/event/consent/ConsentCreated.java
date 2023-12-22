package de.fhg.iese.kickstarttrustee.event.consent;

import java.util.Map;
import java.util.Set;

public record ConsentCreated(String id, String actorId, String consentActorId, String ownerId,
							 Map<String, Set<String>> consentPermissions) {
    public ConsentCreated {
		consentPermissions = Map.copyOf(consentPermissions);
    }
}
