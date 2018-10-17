package Core.CommandLine.Chatroom;

import Core.Params.SmartObject.SmartObject;
import Core.Params.SmartObject.SmartObjectBuilderInternal;

public class ChatroomBuilderInternal implements SmartObjectBuilderInternal {
    @Override
    public SmartObject Build() {
        return new Chatroom();
    }

    @Override
    public SmartObject Build(int id) {
        return new Chatroom(id);
    }
}
