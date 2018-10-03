package Core.CommandLine.Commands;

import Core.CommandLine.Commands.Chatrooms.SendMessageToChatroom;
import Core.CommandLine.VerifiedMessage;

import java.util.Collection;
import java.util.HashSet;

public class Commands {

    public static void AddAvaibleCommand(Command command)
    {
        avaibleCommands.add(command);

        if(!VerifyCommandsUnique())
        {
            // TODO throw any kind of exception or something
        }
    }

    public static void AddAvaibleCommands(Collection<? extends Command> commands)
    {
        avaibleCommands.addAll(commands);

        if(!VerifyCommandsUnique())
        {
            // TODO throw any kind of exception or something
        }
    }

    public static HashSet<Command> GetAvaibleCommands(VerifiedMessage message)
    {
        return avaibleCommands;
    }

    public static boolean VerifyCommandsUnique()
    {
        return true;
    }

    public static Command GetMatchingCommand(HashSet<Command> array, VerifiedMessage message)
    {
        for(Command cmd:array)
        {
            if(cmd.Match(message.GetMessage()) != null)
            {
                return cmd;
            }
        }
        return defaultCommand;
    }

    static
    {
        defaultCommand = new SendMessageToChatroom();
        avaibleCommands = new HashSet<Command>();
    }

    private static Command defaultCommand;
    private static HashSet<Command> avaibleCommands;

}
