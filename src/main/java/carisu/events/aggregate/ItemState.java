package carisu.events.aggregate;

import carisu.events.command.CancelCommand;
import carisu.events.command.Command;
import carisu.events.command.PurchaseCommand;
import carisu.events.command.SelectCommand;
import carisu.events.event.EventStore;
import carisu.events.event.ItemEvent;
import io.vavr.Function2;
import io.vavr.Function3;
import io.vavr.collection.Stream;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import java.util.function.Predicate;

@RequiredArgsConstructor
public enum ItemState {
    SELECTED("item.selected", SelectCommand.class, Item::selectEvent, Item::selectCommand),
    PURCHASED("item.purchased", PurchaseCommand.class, Item::purchaseEvent, Item::purchaseCommand),
    CANCELLED("item.cancelled", CancelCommand.class, Item::cancelEvent, Item::cancelCommand);
    private final String code;
    private final Class<? extends Command> command;
    private final Function2<Item, ItemEvent, Option<Item>> applyEventFunction;
    private final Function3<Item, Command, EventStore, EventStore> applyCommandFunction;

    public static Try<ItemState> of(String code) {
        return of(i -> i.code.equals(code), code, "state code");
    }

    public static Try<ItemState> of(Class<? extends Command> command) {
        return of(i -> i.command.equals(command), command.getSimpleName(), "command");
    }

    private static Try<ItemState> of(Predicate<ItemState> check, String value, String thing) {
        return Stream.of(ItemState.values()).find(check).toTry(() -> new RuntimeException(value + " is not a valid " + thing));
    }

    public ItemEvent create(UUID itemId, Instant commandTimeStamp) {
        return new ItemEvent(itemId, UUID.randomUUID(), commandTimeStamp, Instant.now(), code, Option.none());
    }

    public ItemEvent create(UUID itemId, Instant commandTimeStamp, int hoursTimeout) {
        Instant eventTime = Instant.now();
        return new ItemEvent(itemId, UUID.randomUUID(), commandTimeStamp, eventTime, code, Option.of(eventTime.plus(hoursTimeout, ChronoUnit.HOURS)));
    }

    public Option<Item> apply(Item item, ItemEvent event) {
        return applyEventFunction.apply(item, event);
    }

    public EventStore apply(Item item, Command command, EventStore eventStore) {
        return applyCommandFunction.apply(item, command, eventStore);
    }
}
