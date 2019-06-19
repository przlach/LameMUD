package Core.CommandLine.Commands.Instances;

import Core.CommandLine.Commands.Command;

import java.util.ArrayList;

public class InstancesCommands {

    public static ArrayList<Command> Get()
    {
        ArrayList<Command> commands = new ArrayList<Command>();
        commands.add(new ListInstances());
        commands.add(new CreateInstance());

        return commands;
    }

}
