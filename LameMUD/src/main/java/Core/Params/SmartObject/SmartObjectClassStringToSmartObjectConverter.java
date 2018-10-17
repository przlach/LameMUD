package Core.Params.SmartObject;

import Core.CommandLine.Chatroom.ChatroomBuilder;

public class SmartObjectClassStringToSmartObjectConverter {

    public static SmartObject convert(String classString, int id)
    {
        if(classString.equals(ChatroomBuilder.Get().Build(-1).getSmartObjectClassString()))
        {
            return ChatroomBuilder.Get().Build(id);
        }
        else
        {
            return null;
        }
    }

}
