package Core.CommandLine.Messaging;

import Core.CommandLine.Chatroom.Chatroom;
import Core.CommandLine.User.User;

public interface MessageModifier {

    public String ModifyMessage(Chatroom chatroom, User sender, String entry);

}
