package Core.CommandLine.Commands.Other;

import Core.CommandLine.Commands.Command;
import Core.CommandLine.VerifiedMessage;

public class SetDefaultCommand extends Command {

    @Override
    protected boolean ExecutableWhenLoggedOut() {
        return true;
    }

    @Override
    protected String[] GetPatterns() {

        return null;
    }

    @Override
    public String GetShortDescription() {
        return null;
    }

    @Override
    public void ExecuteCommand(VerifiedMessage caller) {

        String commandReply = "There is no command like that!";

        caller.Reply(commandReply);
    }
}
