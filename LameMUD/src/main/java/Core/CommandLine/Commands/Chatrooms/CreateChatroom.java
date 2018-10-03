package Core.CommandLine.Commands.Chatrooms;

import Core.CommandLine.Chatroom.Chatroom;
import Core.CommandLine.Commands.Command;
import Core.CommandLine.VerifiedMessage;

import java.util.regex.Matcher;

public class CreateChatroom extends Command {

    @Override
    protected String[] GetPatterns() {

        String[] avaiblePatterns = new String[2];
        avaiblePatterns[0] = "\\/chatroom create ([^\\s]+) ([^\\s]+)";
        avaiblePatterns[1] = "\\/chatroom create ([^\\s]+)";

        return avaiblePatterns;
    }

    @Override
    public String GetShortDescription() {
        return "/chatroom create [chatroom_name] [chatroom_password] - creates new chatroom with [chatroom_name] (has to be unique) and [chatroom_password] (its optional).";
    }

    @Override
    public void ExecuteCommand(VerifiedMessage caller) {

        Matcher currentMatch = this.Match(caller.GetMessage());
        String chatroomProposedName = currentMatch.group(1);
        String proposedPassword = "";

        if(currentMatch.groupCount()==2)
        {
            proposedPassword = currentMatch.group(2);
        }

        Chatroom createChatroomResult = Chatroom.CreateChatroom(chatroomProposedName,proposedPassword,caller.GetUser());

        String commandResponse;
        if(createChatroomResult == null)
        {
            commandResponse = "Creating chatroom failed, maybe its name is not unique?";
        }
        else
        {
            commandResponse = "Chatroom created successfully!";

            caller.GetUser().AutoSetDefaultChatroomIfNone();
        }
        caller.Reply(commandResponse);

    }
}
