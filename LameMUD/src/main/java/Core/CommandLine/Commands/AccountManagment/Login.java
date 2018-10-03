package Core.CommandLine.Commands.AccountManagment;

import Core.CommandLine.Commands.Command;
import Core.CommandLine.VerifiedMessage;
import Core.Sessions.ActiveUsers;

import java.util.regex.Matcher;

public class Login extends Command{

    @Override
    protected String[] GetPatterns() {
        String[] avaiblePatterns = new String[1];
        avaiblePatterns[0] = "\\/login ([^\\s]+) ([^\\s]+)";

        return avaiblePatterns;
    }

    @Override
    public String GetShortDescription() {
        return "/login [username] [password] - works only when you are not logged in, [login] and [password] cannot contain spaces. Warning: during entering password it will be visible on your screen and currently don't vanish (so please, don't use passwords you wish to be save forever, this is just beta of chat).";
    }

    @Override
    protected boolean ExecutableWhenLoggedIn() {
        return false;
    }

    @Override
    protected boolean ExecutableWhenLoggedOut() {
        return true;
    }

    @Override
    public void ExecuteCommand(VerifiedMessage caller) {

        Matcher currentMatch = this.Match(caller.GetMessage());
        String login = currentMatch.group(1);
        String password = currentMatch.group(2);

        String commandResponse;

        boolean loggingInResult = ActiveUsers.SetHeaderActive(caller.getSenderPlatformHeader(),login,password);
        if(loggingInResult)
        {
            commandResponse = "<p>Logged in successfully</p>.";
        }
        else
        {
            commandResponse = "Logging in failed.";
        }

        caller.Reply(commandResponse);
    }
}
