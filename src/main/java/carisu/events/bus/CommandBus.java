package carisu.events.bus;

import carisu.events.command.Command;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Stream;
import io.vavr.control.Try;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@NoArgsConstructor
public class CommandBus {
    private Map<UUID, List<Command>> commands = HashMap.empty();

    public Try<CommandBus> addCommand(Command command) {
        UUID item = command.getItemId();
        commands = commands.put(item, commands.get(item).getOrElse(List.empty()).append(command));
        return Try.of(() -> this);
    }

    // TODO: Turn this into subscription service
    public Stream<Command> getCommands(UUID item) {
        return commands.get(item).getOrElse(List.empty()).toStream();
    }
}
