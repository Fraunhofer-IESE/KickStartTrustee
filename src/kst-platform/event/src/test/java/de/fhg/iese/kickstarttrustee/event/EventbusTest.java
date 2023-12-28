package de.fhg.iese.kickstarttrustee.event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.time.Duration;

import org.junit.jupiter.api.Test;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class EventbusTest {
    
    @Test
    void testEventPublish() {
        ReactorEventbus eventbus = new ReactorEventbus();

        Mono<Void> result = eventbus.publishEvent(Mono.just(new TestEvent("test-publish")));

        StepVerifier.create(result)
            .verifyComplete();

        Flux<TestEvent> events = eventbus.eventsOfType(TestEvent.class);

        StepVerifier.create(events)
            .expectNoEvent(Duration.ofMillis(250L))
            .thenCancel();
    }

    @Test
    void testEventSubscribe() {
        ReactorEventbus eventbus = new ReactorEventbus();

        Flux<TestEvent> events = eventbus.eventsOfType(TestEvent.class);

        StepVerifier.create(events)
            .then(() -> eventbus.publishEventSync(new TestEvent("test-subscribe")))
            .assertNext(e -> {
                assertInstanceOf(TestEvent.class, e);
                assertEquals("test-subscribe", e.name());
            })
            .expectNoEvent(Duration.ofMillis(250L))
            .thenCancel();
    }

    @Test
    void testEventSubscribeDifferentEventType() {
        ReactorEventbus eventbus = new ReactorEventbus();

        Flux<TestEvent> events = eventbus.eventsOfType(TestEvent.class);

        StepVerifier.create(events)
            .then(() -> eventbus.publishEventSync(new TestEvent2()))
            .expectNoEvent(Duration.ofMillis(250L))
            .thenCancel();
    }

    record TestEvent(String name){}

    record TestEvent2(){}
}
