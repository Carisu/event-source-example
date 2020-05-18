package carisu.events.event;

import io.vavr.control.Option;
import lombok.Value;

import java.time.Instant;
import java.util.UUID;

@Value
public class ItemEvent {
    UUID itemId;
    UUID eventId;
    Instant commandTimestamp;
    Instant eventTimestamp;
    String eventCode;
    Option<Instant> eventTimeoutTimestamp;
}
