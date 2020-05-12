package carisu.events.command;

import lombok.Value;

import java.time.Instant;
import java.util.UUID;

@Value
public class SelectCommand implements Command {
    UUID item;
    Instant when;
    int holdHours;
}
