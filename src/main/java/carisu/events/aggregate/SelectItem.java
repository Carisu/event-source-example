package carisu.events.aggregate;

import carisu.events.command.CancelCommand;
import carisu.events.command.PurchaseCommand;
import carisu.events.command.SelectCommand;
import io.vavr.control.Option;

public class SelectItem extends Item {
    @Override
    protected Option<Item> select(SelectCommand command) {
        return Option.none();
    }

    @Override
    protected Option<Item> purchase(PurchaseCommand command) {
        return null;
    }

    @Override
    protected Option<Item> cancel(CancelCommand command) {
        return null;
    }
}
