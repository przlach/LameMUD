package Core.CommandLine.Commands.Chatrooms;

import Core.CommandLine.Commands.Command;
import Core.CommandLine.VerifiedMessage;
import Core.Database.API.DatabaseAPI;
import Core.Database.API.DatabaseHandler;

public class ListChatrooms extends Command {

    @Override
    protected String[] GetPatterns() {

        String[] avaiblePatterns = new String[1];
        avaiblePatterns[0] = "\\/chatroom list";

        return avaiblePatterns;
    }

    @Override
    public String GetShortDescription() {
        return "/chatroom list - lists every chatroom.";
    }

    @Override
    public void ExecuteCommand(VerifiedMessage caller) {

        String commandResponse;
        String[] getChannelListResult;

        DatabaseAPI database = DatabaseHandler.Get();

        if(caller.GetUser().CanSeeHiddenChannels())
        {
            getChannelListResult = database.chatrooms().GetEveryChatroomList();
        }
        else
        {
            getChannelListResult = database.chatrooms().GetPublicChatroomList();
        }

        StringBuilder builder = new StringBuilder();
        for(String s : getChannelListResult) {
            builder.append(s);
            builder.append(System.getProperty("line.separator"));
        }
        commandResponse = builder.toString();

        caller.Reply(commandResponse);
    }
}
