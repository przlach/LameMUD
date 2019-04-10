package Core.CommandLine.Commands.Chatrooms;

import Core.CommandLine.Chatroom.Chatroom;
import Core.CommandLine.Commands.Command;
import Core.CommandLine.Messaging.ChatroomFormattedMessage;
import Core.CommandLine.Messaging.MessageSender;
import Core.CommandLine.User.User;
import Core.CommandLine.VerifiedMessage;

import java.util.LinkedList;
import java.util.regex.Matcher;

public class LeaveChatroom extends Command {

    @Override
    protected String[] GetPatterns() {

        String[] avaiblePatterns = new String[1];
        avaiblePatterns[0] = "\\/chatroom leave ([^\\s]+)";

        return avaiblePatterns;
    }

    @Override
    public String GetShortDescription() {
        return "/chatroom leave [chatroom_name] - leaves chatroom with name [chatroom_name].";
    }

    @Override
    public void ExecuteCommand(VerifiedMessage caller) {

        Matcher currentMatch = this.Match(caller.GetMessage());
        String chatroomName = currentMatch.group(1);

        String commandResponse;

        Chatroom leavedChatroom = Chatroom.GetFromDatabase(chatroomName);
        if(leavedChatroom == null)
        {
            commandResponse = "Chatroom with that name doesn't exists.";
        }
        else
        {
            if(leavedChatroom.RemoveUser(caller.GetUser()))
            {
                commandResponse = "<p>You successfully leaved chatroom.</p>";

                String messageToChatroom = caller.GetUser().getUsername() + " leaved chatroom.";

                LinkedList<User> targets = leavedChatroom.GetUsers();
                ChatroomFormattedMessage formattedMsg = new ChatroomFormattedMessage(messageToChatroom,caller.GetUser(),leavedChatroom);
                MessageSender.SystemMessageToUser(targets,formattedMsg);
            }
            else
            {
                commandResponse = "You can't leave a chatroom you don't belong to.";
            }
        }
        caller.Reply(commandResponse);

    }
}
