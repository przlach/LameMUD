package Core.CommandLine.Commands.Chatrooms;

import Core.CommandLine.Commands.Command;
import Core.CommandLine.VerifiedMessage;
import Core.Database.API.DatabaseAPI;
import Core.Database.API.DatabaseHandler;

public class ListChatroomsIAmIn extends Command {

    @Override
    protected String[] GetPatterns() {

        String[] avaiblePatterns = new String[1];
        avaiblePatterns[0] = "\\/chatroom iamin list";

        return avaiblePatterns;
    }

    @Override
    public String GetShortDescription() {
        return "/chatroom iamin list - lists chatrooms you are currently member of.";
    }

    @Override
    public void ExecuteCommand(VerifiedMessage caller) {

        String commandResponse;
        String[] getChannelListResult;

        DatabaseAPI database = DatabaseHandler.Get();

        getChannelListResult = database.chatrooms().GetUserBelongsToChatroomList(caller.GetUser());

        StringBuilder builder = new StringBuilder();
        for(String s : getChannelListResult) {
            builder.append("<p>");
            builder.append(s);
            builder.append("</p>");
        }
        commandResponse = builder.toString();

        caller.Reply(commandResponse);
    }
}
