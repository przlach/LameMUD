package Core.CommandLine.Commands.Chatrooms;

import Core.CommandLine.Commands.Command;
import Core.CommandLine.VerifiedMessage;

import java.util.regex.Matcher;

public class SetDefaultChatroom extends Command {

    @Override
    protected String[] GetPatterns() {

        String[] avaiblePatterns = new String[1];
        avaiblePatterns[0] = "\\/chatroom setdefault ([^\\s]+)";

        return avaiblePatterns;
    }

    @Override
    public String GetShortDescription() {
        return "/chatroom setdefault [chatroom_name] - sets [chatroom_name] as default. You need to be its member.";
    }

    @Override
    public void ExecuteCommand(VerifiedMessage caller) {

        Matcher matcher = this.Match(caller.GetMessage());
        String defaultChatroomName = matcher.group(1);

        String commandResponse;

        if(caller.GetUser().SetDefaultChatroom(defaultChatroomName))
        {
            commandResponse = "Default chatroom set successfully!";
        }
        else
        {
            commandResponse = "Something went wrong during setting default chatroom.";
        }

        caller.Reply(commandResponse);
    }
}
