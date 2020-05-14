package carisu.events.aggregate;

import carisu.events.bus.AcceptCommand;
import carisu.events.command.CancelCommand;
import carisu.events.command.Command;
import carisu.events.command.PurchaseCommand;
import carisu.events.event.EventStore;
import carisu.events.event.ItemEvent;
import io.vavr.collection.HashSet;
import io.vavr.collection.List;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;


@Data
@EqualsAndHashCode(exclude={"state", "events"})
public abstract class Item {
    private final UUID id;
    private final List<ItemEvent> events;

    protected Item(UUID id) {
        this.id = id;
        this.events = List.empty();
    }

    public static Item init(UUID id) {
        return new NewItem(id);
    }

    public Try<Item> apply(ItemEvent event) {
        return ItemState.of(event.getEventCode())
                .map(s -> s.apply(this, event)
                .getOrElse(this));
    }

    public Try<EventStore> apply(Command command, EventStore store) {
        return ItemState.of(command.getClass())
                .map(s -> s.apply(this, command, store));
    }

    public abstract Option<Item> selectEvent(ItemEvent event);

    public abstract Option<Item> purchaseEvent(ItemEvent event);

    public abstract Option<Item> cancelEvent(ItemEvent event);

    public abstract EventStore selectCommand(Command command, EventStore eventStore);

    public abstract EventStore purchaseCommand(Command command, EventStore eventStore);

    public abstract EventStore cancelCommand(Command command, EventStore eventStore);
}
