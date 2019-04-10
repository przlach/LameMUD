package Core.CommandLine.Commands.Chatrooms;

import Core.CommandLine.Chatroom.Chatroom;
import Core.CommandLine.Commands.Command;
import Core.CommandLine.Messaging.ChatroomFormattedMessage;
import Core.CommandLine.Messaging.MessageSender;
import Core.CommandLine.User.User;
import Core.CommandLine.VerifiedMessage;

import java.util.LinkedList;
import java.util.regex.Matcher;

public class JoinChatroom extends Command {

    @Override
    protected String[] GetPatterns() {

        String[] avaiblePatterns = new String[2];
        avaiblePatterns[0] = "\\/chatroom join ([^\\s]+) ([^\\s]+)";
        avaiblePatterns[1] = "\\/chatroom join ([^\\s]+)";

        return avaiblePatterns;
    }

    @Override
    public String GetShortDescription() {
        return "/chatroom join [chatroom_name] [chatroom_password] - join chatroom with name [chatroom_name]. [chatroom_password] is only required if chatroom has one. Using [chatroom_password] when chatroom doesn't have one will result in joining error.";
    }

    @Override
    public void ExecuteCommand(VerifiedMessage caller) {

        Matcher currentMatch = this.Match(caller.GetMessage());
        String chatroomName = currentMatch.group(1);
        String chatroomPassword = "";

        String commandResponse = "";

        if(currentMatch.groupCount()==2)
        {
            chatroomPassword = currentMatch.group(2);
        }

        Chatroom chatroom = Chatroom.GetFromDatabase(chatroomName);
        if(chatroom == null)
        {
            commandResponse = "Chatroom with that name doesn't exists.";
        }
        else
        {
            if(chatroom.AddUser(caller.GetUser(),chatroomPassword))
            {
                commandResponse = "<p>You are added to chatroom successfully.</p>";

                String messageToChatroom = caller.GetUser().getUsername() + " joined chatroom.";

                LinkedList<User> targets = chatroom.GetUsers();
                targets.remove(caller.GetUser());
                ChatroomFormattedMessage formattedMsg = new ChatroomFormattedMessage(messageToChatroom,caller.GetUser(),chatroom);
                MessageSender.SystemMessageToUser(targets,formattedMsg);

                caller.GetUser().AutoSetDefaultChatroomIfNone();
            }
            else
            {
                commandResponse = "You couldn't join that chatroom (maybe password is wrong)";
            }
        }
        caller.Reply(commandResponse);
    }
}
