package Core.CommandLine.Commands.Other;

import Core.CommandLine.Commands.Command;
import Core.CommandLine.VerifiedMessage;
import Core.Config.MainConfig;

public class SetWelcomeMessage extends Command {

    @Override
    public boolean IsCommandVisibleToEverybody() {
        return false;
    }

    @Override
    protected String[] GetPatterns() {
        String[] avaiblePatterns = new String[1];
        avaiblePatterns[0] = "\\/welcomemsg (.+)";

        return avaiblePatterns;
    }

    @Override
    public String GetShortDescription() {
        return "a czo cie to????";
    }

    @Override
    public void ExecuteCommand(VerifiedMessage caller) {

        String newWelcomeMessage = Match(caller.GetMessage()).group(1);

        if(!caller.GetUser().IsSuperUser())
        {
            caller.Reply("You need to be speshull for that!");
        }
        else
        {
            MainConfig.SetOnSuccessfullConnectionMessage(newWelcomeMessage);
            caller.Reply("New welcome msg set successfully!");
        }
    }
}
