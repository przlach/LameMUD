package Core.CommandLine.GameLogic;

import Core.CommandLine.Chatroom.ChatroomBuilder;

public class ItemClassStringToItemConverter {

    public static Item convert(String classString,int id)
    {
        if(classString.equals(ChatroomBuilder.Get().Build(-1).getItemClassString()))
        {
            return ChatroomBuilder.Get().Build(id);
        }
        else
        {
            return null;
        }
    }

}
