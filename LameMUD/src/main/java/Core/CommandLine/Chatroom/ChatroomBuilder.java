package Core.CommandLine.Chatroom;

import Core.Params.SmartObject.SmartObjectBuilder;

public class ChatroomBuilder extends SmartObjectBuilder {

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
