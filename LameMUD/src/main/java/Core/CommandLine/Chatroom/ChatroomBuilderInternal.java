package Core.CommandLine.Chatroom;

import Core.CommandLine.GameLogic.Item;
import Core.CommandLine.GameLogic.ItemBuilderInternal;

public class ChatroomBuilderInternal implements ItemBuilderInternal {
    @Override
    public Item Build() {
        return new Chatroom();
    }

    @Override
    public Item Build(int id) {
        return new Chatroom(id);
    }
}
