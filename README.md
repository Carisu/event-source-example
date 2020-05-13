## Purpose
This is based on the example given in [ddd-by-examples/event-source-cqrs-sample](https://github.com/ddd-by-examples/event-source-cqrs-sample) to represent adding ites into a list and either paying for the or having items drop from the list after a time-out period.

#### Event Sourcing
The following commands are allowed:
- **Select item** (called *buy* in original) The item is selected and added to the list
- **Purchase item** (called *pay* in orginal) The item is purchased from the list
- **Cancel item** The item is cancelled and removed from the list

These following events are created for changes to the item:
- **Item selected** The item is *selected* and a timeout set for when it will be automatically cancelled
- **Item purchased** The item is *purchased* and cannot be selected again
- **Item cancelled** The item is *cancelled*, either due to command or timeout, and can be selected again

Each event has a timestamp on it for when the event took place.

Processing takes places in the following order:
1. User issues command to command bus
1. Command bus read in sequential order
   1. The events are aggregated to get the current state
      1. If the event is for **item selected** and the item either does not exist or is *cancelled* state
         1. The item is set to *selected* state with timeout set to event timeout hours after event timestamp
      1. If the event is for **item purchased** and the item state is *selected*
         1. The item state is set to *purchased*
      1. If the event is for **item cancelled** and the item state is *selected*
         1. The item state is set to *cancelled*
   1. If the command is to **select item** and the item does not exist or is *cancelled* state
      1. An **item selected** event is issued with default timeout
   1. If the command is to **purchase item** and the item is *selected* state and the current time is before the item timeout
      1. An **item purchased** event is issued
   1. If the command is to **cancel item** and the ite is *selected* state
      1. An **item cancelled** event is issued

Every event issued is both stored for aggregation and sent to the event bus.

The following check takes place on a schedule (every minute):
1. The events are aggregated (as above)
1. For any item is *selected* state
   1. If the current time has passed the item timeout
      1. An **item cancelled** event is issued