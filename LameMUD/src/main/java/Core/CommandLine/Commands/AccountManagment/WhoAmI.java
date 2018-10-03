package Core.CommandLine.Commands.AccountManagment;

import Core.CommandLine.Commands.Command;
import Core.CommandLine.VerifiedMessage;

public class WhoAmI extends Command{

    @Override
    protected String[] GetPatterns() {
        String[] avaiblePatterns = new String[1];
        avaiblePatterns[0] = "\\/whoami";

        return avaiblePatterns;
    }

    @Override
    public String GetShortDescription() {
        return "/whoami - returns information about your nickname and other id's currently associated with you.";
    }

    @Override
    public void ExecuteCommand(VerifiedMessage caller) {

        String commandResponse;

        commandResponse = "Your identity: "+caller.GetUser().getUsername()+"["+caller.getSenderPlatformHeader().GetSessionIdentifier()+" "+caller.getSenderPlatformHeader().GetUserIdentifier()+" "+caller.getSenderPlatformHeader().GetPlatformIdentifier()+" ";

        caller.Reply(commandResponse);

    }
}
