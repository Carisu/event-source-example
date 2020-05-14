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

    public Try<CommandService> selectItem(UUID item) {
        return issueCommand(new SelectCommand(item, Instant.now()));
    }

    public Try<CommandService> purchaseItem(UUID item) {
        return issueCommand(new PurchaseCommand(item, Instant.now()));
    }

    public Try<CommandService> cancelItem(UUID item) {
        return issueCommand(new CancelCommand(item, Instant.now()));
    }

    private Try<CommandService> issueCommand(Command command) {
        return commandBus.addCommand(command).map(c -> this);
    }
}
