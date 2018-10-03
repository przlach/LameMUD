package Core.CommandLine.Commands.Other;

import Core.CommandLine.Commands.Command;
import Core.CommandLine.Commands.Commands;
import Core.CommandLine.VerifiedMessage;

import java.util.HashSet;

public class Help extends Command {

    @Override
    protected String[] GetPatterns() {
        String[] avaiblePatterns = new String[1];
        avaiblePatterns[0] = "\\/help";

        return avaiblePatterns;
    }

    @Override
    protected boolean ExecutableWhenLoggedOut() {
        return true;
    }

    @Override
    public String GetShortDescription() {
        return "/help - prints this menu";
    }

    @Override
    public void ExecuteCommand(VerifiedMessage caller) {

        HashSet<Command> avaibleCommands = Commands.GetAvaibleCommands(caller);
        String helpOutput = "";

        for(Command command:avaibleCommands)
        {
            if(command.IsCommandVisibleToEverybody())
            {
                helpOutput += "<p> == ";
                helpOutput += command.GetShortDescription();
                helpOutput += "</p>";
            }
        }

        caller.Reply(helpOutput);
    }
}
