package de.fhg.iese.kickstarttrustee.event;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.springframework.stereotype.Component;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@Component
public class ReactorEventbus implements IEventbus {
    private static final long TIMEOUT_MS = 500L;

    private final Sinks.Many<Envelope<?>> sink;
    private final Flux<Envelope<?>> flux;

    public ReactorEventbus() {
        this.sink = Sinks.many().multicast().onBackpressureBuffer();
        this.flux = sink.asFlux();
    }

    public <E> Mono<Void> publishEvent(Mono<E> event) {
        return event.map(Envelope::new)
                .doOnNext(e -> sink.emitNext(e, Sinks.EmitFailureHandler.busyLooping(Duration.ofMillis(TIMEOUT_MS))))
                .then();
    }

    public <E> void publishEventAsync(E event) {
        Objects.requireNonNull(event, "Event must not be null!");
        this.publishEvent(Mono.just(event)).subscribe();
    }

    public <E> void publishEventSync(E event) {
        Objects.requireNonNull(event, "Event must not be null!");
        this.publishEvent(Mono.just(event)).block();
    }

    public <E> Flux<E> eventsOfType(Class<E> type) {
        return flux.filter(event -> type.isInstance(event.payload())).map(Envelope::payload).cast(type);
    }

    public <E> void registerConsumer(Class<E> type, Consumer<E> consumer) {
        Objects.requireNonNull(consumer, "Consumer must not be null!");
        eventsOfType(type).subscribe(consumer);
    }

    public <E> void registerConsumer(Class<E> type, Predicate<E> filter, Consumer<E> consumer) {
        Objects.requireNonNull(filter, "Filter must not be null!");
        Objects.requireNonNull(consumer, "Consumer must not be null!");
        eventsOfType(type).filter(filter).subscribe(consumer);
    }

    public record Envelope<T>(String id, Instant timestamp, T payload) {
        public Envelope(T payload) {
            this(UUID.randomUUID().toString(), Instant.now(), payload);
        }
    }
}
