package carisu.events.aggregate;

import carisu.events.event.ItemEvent;

public class PurchaseItem extends Item {
    public PurchaseItem(Item previous, ItemEvent event) {
        super(previous, event);
    }
}
