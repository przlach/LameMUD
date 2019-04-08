package Core.CommandLine.Commands.Instances;

import Core.CommandLine.Commands.Command;
import Core.CommandLine.VerifiedMessage;

public class ListInstances extends Command {

    @Override
    protected String[] GetPatterns() {

        String[] avaiblePatterns = new String[1];
        avaiblePatterns[0] = "\\/instance list";

        return avaiblePatterns;
    }

    @Override
    public String GetShortDescription() {
        return "TBD";   // TODO
    }

    @Override
    public void ExecuteCommand(VerifiedMessage caller) {

        caller.Reply("Chuck testa");

    }
}
