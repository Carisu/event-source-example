package carisu.events.aggregate;

import carisu.events.bus.AcceptCommand;
import carisu.events.command.Command;
import carisu.events.event.EventStore;
import carisu.events.event.ItemEvent;
import io.vavr.Tuple;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AggregateService implements AcceptCommand {
    private final EventStore internalEventStore = EventStore.init();

    public Try<EventStore> acceptCommand(Command command) {
        return acceptCommand(command, internalEventStore);
    }

    public Try<EventStore> acceptCommand(Command command, EventStore eventStore) {
        return aggregate(eventStore)
                .flatMap(m -> m.get(command.getItem())
                        .getOrElse(new NewItem(command.getItem()))
                        .apply(command, eventStore));
    }

    public Try<Map<UUID, Item>> aggregate(EventStore eventStore) {
        return eventStore.getEventStream()
                .map(as -> as.groupBy(ItemEvent::getItemId)
                .flatMap((u, es) -> List.of(Tuple.of(u,
                        es.collect(() -> new NewItem(u), Item::apply, (i1, i2) -> {
                            throw new RuntimeException("Cannot combine items");
                        })))));
    }
}
