package carisu.events.aggregate;

import carisu.events.command.CancelCommand;
import carisu.events.command.Command;
import carisu.events.command.PurchaseCommand;
import carisu.events.command.SelectCommand;
import carisu.events.event.EventStore;
import carisu.events.event.ItemEvent;
import io.vavr.collection.List;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;


@Data
@EqualsAndHashCode(exclude={"events"})
public abstract class Item {
    private final UUID id;
    private final List<ItemEvent> events;

    protected static final long DEFAULT_TIMEOUT_HOURS = 24;

    protected Item(UUID id) {
        this.id = id;
        this.events = List.empty();
    }

    protected Item(Item previous, ItemEvent event) {
        this.id = previous.id;
        this.events = previous.events.append(event);
    }

    public Try<Item> apply(ItemEvent event) {
        return ItemState.of(event.getEventCode())
                .map(s -> s.apply(this, event)
                .getOrElse(this));
    }

    public Try<EventStore> apply(Command command, EventStore store) {
        return ItemState.of(command.getClass())
                .flatMap(s -> s.apply(this, command, store));
    }

    protected Option<Item> ignoreEvent() {
        return Option.none();
    }

    protected Try<EventStore> ignoreCommand(EventStore eventStore) {
        return Option.of(eventStore).toTry();
    }

    public Option<Item> selectEvent(ItemEvent event) {
        return ignoreEvent();
    }

    public Option<Item> purchaseEvent(ItemEvent event) {
        return ignoreEvent();
    }

    public Option<Item> cancelEvent(ItemEvent event) {
        return ignoreEvent();
    }

    public final Try<EventStore> selectCommand(Command command, EventStore eventStore) {
        return Try.of(() -> (SelectCommand)command)
                .flatMap(c -> selectCommand(c, eventStore));
    }

    public final Try<EventStore> purchaseCommand(Command command, EventStore eventStore) {
        return Try.of(() -> (PurchaseCommand)command)
                .flatMap(c -> purchaseCommand(c, eventStore));
    }

    public final Try<EventStore> cancelCommand(Command command, EventStore eventStore) {
        return Try.of(() -> (CancelCommand)command)
                .flatMap(c -> cancelCommand(c, eventStore));
    }

    protected Try<EventStore> selectCommand(SelectCommand command, EventStore eventStore) {
        return ignoreCommand(eventStore);
    }

    protected Try<EventStore> purchaseCommand(PurchaseCommand command, EventStore eventStore) {
        return ignoreCommand(eventStore);
    }

    protected Try<EventStore> cancelCommand(CancelCommand command, EventStore eventStore) {
        return ignoreCommand(eventStore);
    }
}
