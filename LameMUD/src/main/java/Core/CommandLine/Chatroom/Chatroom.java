package Core.CommandLine.Chatroom;

import Core.CommandLine.GameLogic.Item;
import Core.Params.SmartDouble;
import Core.Params.SmartString;
import Core.CommandLine.Messaging.MessageModifyExecutor;
import Core.CommandLine.Messaging.MessageSender;
import Core.CommandLine.User.User;
import Core.CommandLine.User.UserBuilder;
import Core.Database.API.DatabaseAPI;
import Core.Database.API.DatabaseHandler;

import java.util.ArrayList;

public class Chatroom extends Item {

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
            Chatroom gettedChatroom = (Chatroom) Chatroom.GetItemFromDatabase(chatroomID);
            return gettedChatroom;
        }
        return null;
    }

    @Override
    public String getItemClassString() {
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

    public void SendMessage(User sender, String message)
    {
        DatabaseAPI database = DatabaseHandler.Get();
        ArrayList<Integer> usersInChatroom = database.chatrooms().GetUsersArray(GetID());
        String formattedMessage = "|" + name.GetValue() + "|";
        if(sender != null)
        {
            formattedMessage += "[" + sender.getUsername() +"]";
        }

        String transformedMessage = MessageModifyExecutor.ModifyChatroomMessage(this,sender,message);

        formattedMessage += " " + transformedMessage;
        for(int userID: usersInChatroom)
        {
            MessageSender.SystemMessageToUser(UserBuilder.ObsoleteBuild(userID,""),formattedMessage);    // TODO very temp, is creating user here ok???
        }
    }
    public void SendMessage(String message)
    {
        SendMessage(null,message);
    }

    public void RemoveEveryUser()
    {
        DatabaseAPI database = DatabaseHandler.Get();

        ArrayList<Integer> chatroomUsrsIds = database.chatrooms().GetUsersArray(this.GetID());

        for(int usrID: chatroomUsrsIds)
        {
            RemoveUser(UserBuilder.ObsoleteBuild(usrID,"Chatroom:RemoveEveryUser() internat incomplete user"));  // not correct usage of creating incomplete user
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
