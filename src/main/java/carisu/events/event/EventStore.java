package carisu.events.event;

import carisu.events.aggregate.Item;
import io.vavr.collection.List;
import io.vavr.collection.Stream;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

public class EventStore {
    private final List<ItemEvent> events;

    private EventStore(List<ItemEvent> events) {
        this.events = events;
    }

    public static EventStore init() {
        return new EventStore(List.empty());
    }

    public Try<EventStore> addEvent(ItemEvent event) {
        return Try.of(() -> new EventStore(events.append(event)));
    }

    public Try<Stream<ItemEvent>> getEventStream() {
        return Try.of(() -> events.toStream());
    }
}
