package Core.CommandLine.Chatroom;

import Core.Params.SmartObject.SmartObject;
import Core.Params.SmartDouble;
import Core.Params.SmartString;
import Core.CommandLine.Messaging.MessageModifyExecutor;
import Core.CommandLine.Messaging.MessageSender;
import Core.CommandLine.User.User;
import Core.Database.API.DatabaseAPI;
import Core.Database.API.DatabaseHandler;

import java.util.ArrayList;
import java.util.LinkedList;

public class Chatroom extends SmartObject {

    public Chatroom()
    {
        super();
    }

    public Chatroom(int id)
    {
        super(id);
    }

    protected void InitChatroom(String name, String password, User owner)
    {
        this.name.SetValue(name);
        this.password.SetValue(password);
        this.ownerID.SetValue(owner.getId());
        this.hidden.SetValue(false);
        DatabaseHandler.Get().chatrooms().AddUserToUsersArray(id,owner.getId());
    }

    public boolean IsUserOwner(User user)
    {
        // allow admin to remove any channel
        if(((int)ownerID.GetValue()) == user.getId())
        {
            return true;
        }
        return false;
    }

    public static Chatroom GetFromDatabase(String name)
    {
        int chatroomID = DatabaseHandler.Get().chatrooms().GetChatroomID(name);
        if(chatroomID > -1)
        {
            Chatroom gettedChatroom = (Chatroom) Chatroom.GetSmartObjectFromDatabase(chatroomID);
            return gettedChatroom;
        }
        return null;
    }

    @Override
    public String getSmartObjectClassString() {
        return "chatroom";
    }

    @Override
    protected void RegisterSmartParameters() {
        ownerID = new SmartDouble("ownerID");
        name = new SmartString("name");
        password = new SmartString("password");
        hidden = new SmartString("hidden");

        trackSmartParameter(ownerID);
        trackSmartParameter(name);
        trackSmartParameter(password);
        trackSmartParameter(hidden);
    }

    public static Chatroom CreateChatroom(String name, String password, User owner)
    {
        if(DatabaseHandler.Get().chatrooms().IsChatroom(name))
        {
            return null;
        }
        Chatroom created = (Chatroom)ChatroomBuilder.Get().Build();
        created.InitChatroom(name,password,owner);
        created.SetParametersValueInDatabase();
        return created;
    }

    public boolean AddUser(User user, String password)
    {
        DatabaseAPI database = DatabaseHandler.Get();
        if(!this.password.GetValue().equals(password))
        {
            return false;
        }
        return database.chatrooms().AddUserToUsersArray(GetID(),user.getId());
    }

    public boolean AddUser(User user)
    {
        return AddUser(user,"");
    }

    public boolean RemoveUser(User user)
    {
        DatabaseAPI database = DatabaseHandler.Get();
        boolean removeResult = database.chatrooms().RemoveUserFromUsersArray(GetID(),user.getId());

        if(removeResult) {
            user.AutoSetDefaultChatroomAfterLeavingChatroom(name.GetValue());
        }

        return removeResult;
    }


    public boolean HasUser(User user)
    {
        DatabaseAPI database = DatabaseHandler.Get();
        return database.chatrooms().IsUserInUsersArray(GetID(),user.getId());
    }

    public LinkedList<User> GetUsers()
    {
        LinkedList<User> users = new LinkedList<User>();

        DatabaseAPI database = DatabaseHandler.Get();
        ArrayList<Integer> chatroomUsrsIds = database.chatrooms().GetUsersArray(this.GetID());

        for(int usrID: chatroomUsrsIds)
        {
            users.add(User.Get(usrID));
        }

        return users;
    }

    public void RemoveEveryUser()
    {
        DatabaseAPI database = DatabaseHandler.Get();

        ArrayList<Integer> chatroomUsrsIds = database.chatrooms().GetUsersArray(this.GetID());

        for(int usrID: chatroomUsrsIds)
        {
            RemoveUser(User.Get(usrID));
        }

    }

    @Override
    protected void PreRemove() {

        RemoveEveryUser();

    }

    public String GetName()
    {
        return name.GetValue();
    }

    private SmartDouble ownerID;
    private SmartString name;
    private SmartString password;
    private SmartString hidden;
}
