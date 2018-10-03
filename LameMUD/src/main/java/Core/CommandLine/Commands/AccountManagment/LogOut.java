package Core.CommandLine.Commands.AccountManagment;

import Core.CommandLine.Commands.Command;
import Core.CommandLine.VerifiedMessage;
import Core.Sessions.ActiveUsers;

public class LogOut extends Command{

    @Override
    protected String[] GetPatterns() {
        String[] avaiblePatterns = new String[1];
        avaiblePatterns[0] = "\\/logout";

        return avaiblePatterns;
    }

    @Override
    public String GetShortDescription() {
        return "/logout - works only when you are logged in.";
    }

    @Override
    public void ExecuteCommand(VerifiedMessage caller) {

        String commandResponse;

        boolean logoutResult = ActiveUsers.SetHeaderInactive(caller.getSenderPlatformHeader());

        if(logoutResult)
        {
            caller.SetUser(null);
            commandResponse = "Logged out successfully.";
        }
        else
        {
            commandResponse = "Logging out failed.";
        }

        caller.Reply(commandResponse);

    }
}
