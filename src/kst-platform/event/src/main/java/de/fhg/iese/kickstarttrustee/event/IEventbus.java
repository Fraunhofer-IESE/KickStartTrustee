package de.fhg.iese.kickstarttrustee.event;

import java.util.function.Consumer;
import java.util.function.Predicate;

public interface IEventbus {
    <E> void publishEventAsync(E event);
    <E> void publishEventSync(E event);
    <E> void registerConsumer(Class<E> type, Consumer<E> consumer);
    <E> void registerConsumer(Class<E> type, Predicate<E> filter, Consumer<E> consumer);
}
