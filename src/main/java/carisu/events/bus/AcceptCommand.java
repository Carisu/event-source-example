package carisu.events.bus;

import carisu.events.command.Command;
import io.vavr.control.Try;

public interface AcceptCommand<R> {
    Try<R> acceptCommand(Command command);
}
