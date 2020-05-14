package carisu.events.bus;

import carisu.events.command.Command;
import io.vavr.control.Try;

public interface AcceptCommand {
    Try<?> acceptCommand(Command command);
}
