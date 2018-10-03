package Core.CommandLine.Commands.AccountManagment;

import Core.CommandLine.Commands.Command;

import java.util.ArrayList;

public class AccountManagmentCommands {

    public static ArrayList<Command> Get()
    {
        ArrayList<Command> commands = new ArrayList<Command>();
        commands.add(new Login());
        commands.add(new LogOut());
        commands.add(new Register());
        commands.add(new WhoAmI());

        return commands;
    }

}
