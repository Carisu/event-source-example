package carisu.events.command;

import lombok.Value;

import java.time.Instant;
import java.util.UUID;

@Value
public class PurchaseCommand implements Command {
    UUID itemId;
    Instant commandTimestamp;
}
