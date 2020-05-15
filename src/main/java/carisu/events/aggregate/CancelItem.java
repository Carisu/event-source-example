package carisu.events.aggregate;

import carisu.events.command.SelectCommand;
import carisu.events.event.EventStore;
import carisu.events.event.ItemEvent;
import io.vavr.control.Option;
import io.vavr.control.Try;

public class CancelItem extends Item {
    public CancelItem(Item previous, ItemEvent event) {
        super(previous, event);
    }

    @Override
    public Option<Item> selectEvent(ItemEvent event) {
        return Option.of(new SelectItem(this, event));
    }

    @Override
    protected Try<EventStore> selectCommand(SelectCommand command, EventStore eventStore) {
        return eventStore.addEvent(ItemState.SELECTED.create(command.getItem(), command.getWhen(), DEFAULT_TIMEOUT_HOURS));
    }
}
