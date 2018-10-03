package Core.CommandLine;

import Core.CommandLine.Commands.Command;
import Core.CommandLine.Commands.Commands;
import Core.CommandLine.Platforms.PlatformMessage;
import Core.Sessions.ActiveUsers;

import java.util.HashSet;

public class MainInterpreter {

    public synchronized static void HandleMessage(PlatformMessage msg)
    {
        FormatMessage(msg);
        VerifiedMessage authMsg = AuthenticateMessage(msg);
        HandleMessage(authMsg);
    }

    private static void FormatMessage(PlatformMessage msg)
    {
        if(msg.GetMessage().substring(msg.GetMessage().length()-1).charAt(0) == (char)10)
        {
            msg.SetMessage(msg.GetMessage().substring(0,msg.GetMessage().length()-1));
        }
    }

    private static VerifiedMessage AuthenticateMessage(PlatformMessage msg)
    {
        return ActiveUsers.AuthenticateMessage(msg);
    }

    private static void HandleMessage(VerifiedMessage msg)
    {
        HashSet<Command> commands = Commands.GetAvaibleCommands(msg);

        Command matching = Commands.GetMatchingCommand(commands,msg);

        matching.ExecuteCommandWithLoginCheck(msg);
    }

    static
    {
        Core.Config.MainConfig.SetConfig();
    }

}
