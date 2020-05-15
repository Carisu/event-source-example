package carisu.events.event;

import io.vavr.control.Option;
import lombok.Value;

import java.time.Instant;
import java.util.UUID;

@Value
public class ItemEvent {
    UUID itemId;
    UUID eventId;
    Instant commandTimeStamp;
    Instant eventTimeStamp;
    String eventCode;
    Option<Instant> eventTimeoutHours;
}
