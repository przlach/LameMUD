package Core.CommandLine.Commands.Chatrooms;

import Core.CommandLine.Commands.Command;

import java.util.ArrayList;

public class ChatroomsCommands {

    public static ArrayList<Command> Get()
    {
        ArrayList<Command> commands = new ArrayList<Command>();
        commands.add(new CreateChatroom());
        commands.add(new DeleteChatroom());
        commands.add(new JoinChatroom());
        commands.add(new LeaveChatroom());
        commands.add(new ListChatrooms());
        commands.add(new ListChatroomsIAmIn());
        commands.add(new SetDefaultChatroom());

        return commands;
    }

}
