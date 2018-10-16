package Core.CommandLine.Commands.AccountManagment;

import Core.CommandLine.Commands.Command;
import Core.CommandLine.User.User;
import Core.CommandLine.VerifiedMessage;
import Core.Database.API.DatabaseAPI;
import Core.Database.API.DatabaseHandler;

import java.util.regex.Matcher;

public class Register extends Command{

    @Override
    protected String[] GetPatterns() {
        String[] avaiblePatterns = new String[1];
        avaiblePatterns[0] = "\\/register ([^\\s]+) ([^\\s]+)";

        return avaiblePatterns;
    }

    @Override
    public String GetShortDescription() {
        return "/register [username] [password] - creates new user if user with [username] doesn't exists yet. Both [username] and [password] cannot contain space.";
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

        User registeredUser = User.Create(login,password);

        if(registeredUser != null)
        {
            commandResponse = "New user registered! (you can now login via command \"/login [login] [password]\").";
        }
        else
        {
            commandResponse = "Adding new user failed.";
        }

        caller.Reply(commandResponse);
    }
}
