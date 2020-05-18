package carisu.events.command;

import carisu.events.bus.CommandBus;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CommandService {
    private final CommandBus commandBus;

    public Try<CommandService> selectItem(UUID itemId) {
        return issueCommand(new SelectCommand(itemId, Instant.now()));
    }

    public Try<CommandService> purchaseItem(UUID itemId) {
        return issueCommand(new PurchaseCommand(itemId, Instant.now()));
    }

    public Try<CommandService> cancelItem(UUID itemId) {
        return issueCommand(new CancelCommand(itemId, Instant.now()));
    }

    private Try<CommandService> issueCommand(Command command) {
        return commandBus.addCommand(command).map(c -> this);
    }
}
