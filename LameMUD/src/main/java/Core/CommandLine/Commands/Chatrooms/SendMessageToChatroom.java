package Core.CommandLine.Commands.Chatrooms;

import Core.CommandLine.Chatroom.Chatroom;
import Core.CommandLine.Commands.Command;
import Core.CommandLine.Messaging.ChatroomFormattedMessage;
import Core.CommandLine.Messaging.MessageSender;
import Core.CommandLine.User.User;
import Core.CommandLine.VerifiedMessage;
import Core.Database.API.DatabaseHandler;

import java.util.LinkedList;
import java.util.regex.Matcher;

public class SendMessageToChatroom extends Command {

    @Override
    protected String[] GetPatterns() {

        String[] avaiblePatterns = new String[2];
        avaiblePatterns[0] = "\\/sendto ([^\\s]+)\\s(.+)";
        avaiblePatterns[1] = "(.+)";

        return avaiblePatterns;
    }

    @Override
    protected boolean ExecutableWhenLoggedOut() {
        return true;
    }

    @Override
    public String GetShortDescription() {
        return "(/sendto [chatroom_name] optional) [message] - sends [message] to [chatroom_name]. You need to join a chatroom first. [chatroom_name] is optional if you have default chatroom set.";
    }

    @Override
    public void ExecuteCommand(VerifiedMessage caller) {

        Matcher currentMatch = this.Match(caller.GetMessage());

        Chatroom targetChatroom = null;
        String messageToSend="init message (wololo)";
        String commandResponse="";

        if(!caller.IsUserLoggedIn())
        {
            commandResponse += "You can't messages when being logged out (to change that use \"/register [login] [password]\" and \"/login [login] [password]\").";
            caller.Reply(commandResponse);
            return;
        }

        String[] chatroomsUserIsIn = DatabaseHandler.Get().chatrooms().GetUserBelongsToChatroomList(caller.GetUser());
        if(chatroomsUserIsIn.length == 0)
        {
            commandResponse += "You need to be in any chatroom first! (use \"/chatroom join [chatroom_name] [chatroom_password (optional)]\" or \"/chatroom create [chatroom_name] [chatroom_password (optional)]\").";
            caller.Reply(commandResponse);
            return;
        }

        if(currentMatch.groupCount()==1)
        {
            targetChatroom = caller.GetUser().GetDefaultChatroom();
            messageToSend = currentMatch.group(1);
            if(targetChatroom==null)
            {
                commandResponse = "No default chatroom set.";
            }
        }
        else if(currentMatch.groupCount()==2)
        {
            targetChatroom = Chatroom.GetFromDatabase(currentMatch.group(1));
            if(targetChatroom == null)
            {
                commandResponse = "There is no chatroom with that name!";
            }
            else
            {
                messageToSend = currentMatch.group(2);
            }
        }

        if(targetChatroom != null && !targetChatroom.HasUser(caller.GetUser()))
        {
            commandResponse = "You need to be part of this chatroom first!";
        }

        if(commandResponse.equals(""))
        {
            LinkedList<User> targets = targetChatroom.GetUsers();
            ChatroomFormattedMessage formattedMsg = new ChatroomFormattedMessage(messageToSend,caller.GetUser(),targetChatroom);
            MessageSender.SystemMessageToUser(targets,formattedMsg);
        }
        else
        {
            caller.Reply(commandResponse);
        }

    }
}
