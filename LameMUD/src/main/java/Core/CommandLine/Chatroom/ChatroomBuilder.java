package Core.CommandLine.Chatroom;

import Core.CommandLine.GameLogic.ItemBuilder;

public class ChatroomBuilder extends ItemBuilder {

    protected ChatroomBuilder()
    {
        SetInternalBuilder(new ChatroomBuilderInternal());
    }

    public static ChatroomBuilder Get()
    {
        if(singleton == null)
        {
            singleton = new ChatroomBuilder();
        }
        return singleton;
    }

    private static ChatroomBuilder singleton;

}
