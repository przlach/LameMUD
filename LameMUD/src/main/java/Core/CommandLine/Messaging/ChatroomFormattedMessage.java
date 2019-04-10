package Core.CommandLine.Messaging;

import Core.CommandLine.Chatroom.Chatroom;
import Core.CommandLine.User.User;

public class ChatroomFormattedMessage implements FormattedMessage{

    public ChatroomFormattedMessage(String msg, User sender, Chatroom chatroom) {
        this.msg = msg;
        this.sender = sender;
        this.chatroom = chatroom;
    }

    @Override
    public String Get() {

        String formattedMessage = "|" + chatroom.GetName() + "|";
        if(sender != null)
        {
            formattedMessage += "[" + sender.getUsername() +"]";
        }

        String transformedMessage = MessageModifyExecutor.ModifyChatroomMessage(chatroom,sender,msg);

        formattedMessage += " " + transformedMessage;

        return formattedMessage;
    }

    private String msg;
    private User sender;
    private Chatroom chatroom;


}
