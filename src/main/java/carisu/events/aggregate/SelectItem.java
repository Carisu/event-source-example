package carisu.events.aggregate;

import carisu.events.command.CancelCommand;
import carisu.events.command.PurchaseCommand;
import carisu.events.event.EventStore;
import carisu.events.event.ItemEvent;
import io.vavr.control.Option;
import io.vavr.control.Try;

public class SelectItem extends Item {
    public SelectItem(Item previous, ItemEvent event) {
        super(previous, event);
    }

    @Override
    public Option<Item> purchaseEvent(ItemEvent event) {
        return Option.of(new PurchaseItem(this, event));
    }

    @Override
    public Option<Item> cancelEvent(ItemEvent event) {
        return Option.of((new CancelItem(this, event)));
    }

    @Override
    protected Try<EventStore> purchaseCommand(PurchaseCommand command, EventStore eventStore) {
        return eventStore.addEvent(ItemState.PURCHASED.create(command.getItem(), command.getWhen()));
    }

    @Override
    protected Try<EventStore> cancelCommand(CancelCommand command, EventStore eventStore) {
        return eventStore.addEvent(ItemState.CANCELLED.create(command.getItem(), command.getWhen()));
    }
}
