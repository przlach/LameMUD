package Core.CommandLine.Commands.Chatrooms;

import Core.CommandLine.Chatroom.Chatroom;
import Core.CommandLine.Commands.Command;
import Core.CommandLine.VerifiedMessage;

import java.util.regex.Matcher;

public class DeleteChatroom extends Command {

    @Override
    protected String[] GetPatterns() {

        String[] avaiblePatterns = new String[1];
        avaiblePatterns[0] = "\\/chatroom remove ([^\\s]+)";

        return avaiblePatterns;
    }

    @Override
    public String GetShortDescription() {
        return "/chatroom remove [chatroom_name] - removes chatroom with [chatroom_name]. Only chatroom owner can do it (currently only creator of chatroom is its owner).";
    }

    @Override
    public void ExecuteCommand(VerifiedMessage caller) {

        Matcher currentMatch = this.Match(caller.GetMessage());
        String chatroomName = currentMatch.group(1);

        String commandResponse;

        Chatroom chatroomToRemove = Chatroom.GetFromDatabase(chatroomName);
        if(chatroomToRemove == null)
        {
            commandResponse = "Chatroom with that name doesn't exist.";
        }
        else
        {
            if(!chatroomToRemove.IsUserOwner(caller.GetUser()))
            {
                commandResponse = "You need to be owner to remove that chatroom.";
            }
            else
            {
                if(chatroomToRemove.Remove()) {
                    commandResponse = "You successfully removed a channel.";
                }
                else
                {
                    commandResponse = "Something went wrong during removing a channel.";
                }
            }
        }
        caller.Reply(commandResponse);
    }
}
