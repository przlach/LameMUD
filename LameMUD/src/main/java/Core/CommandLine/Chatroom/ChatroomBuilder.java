package Core.CommandLine.Chatroom;

import Core.CommandLine.GameLogic.ItemBuilder;

public class ChatroomBuilder extends ItemBuilder {

    static
    {
        SetInternalBuilder(new ChatroomBuilderInternal());
    }

}
