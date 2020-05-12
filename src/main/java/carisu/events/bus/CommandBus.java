package carisu.events.bus;

import carisu.events.command.Command;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Tree;
import io.vavr.control.Option;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@NoArgsConstructor
public class CommandBus {
    private Map<UUID, List<Command>> commands = HashMap.empty();

    public CommandBus addCommand(Command command) {
        UUID item = command.getItem();
        commands = commands.put(item, commands.get(item).getOrElse(List.empty()).append(command));
        return this;
    }

    public Option<List<Command>> getCommands(UUID item) {
        return commands.get(item);
    }
}
