package Core.CommandLine.Messaging;

import Core.CommandLine.Chatroom.Chatroom;
import Core.CommandLine.User.User;

import java.util.ArrayList;

public class MessageModifyExecutor {

    public static String ModifyChatroomMessage(Chatroom chatroom, User sender, String entry)
    {
        String actualEntry = entry;
        for(MessageModifier modifier : activeModifiers)
        {
            actualEntry = modifier.ModifyMessage(chatroom,sender,actualEntry);
        }

        return actualEntry;
    }

    public static void AddModifier(MessageModifier modifier)
    {
        activeModifiers.add(modifier);
    }

    private static ArrayList<MessageModifier> activeModifiers;

    static
    {
        activeModifiers = new ArrayList<MessageModifier>();
    }

}
