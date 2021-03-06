package Core.CommandLine.User;

import Core.CommandLine.Chatroom.Chatroom;
import Core.CommandLine.Messaging.NoFormatMessage;
import Core.CommandLine.Platforms.PlatformMessageHeader;
import Core.Database.API.DatabaseAPI;
import Core.Params.SmartDouble;
import Core.Params.SmartParamUpdater;
import Core.Params.SmartParamUpdaterInput;
import Core.Params.SmartString;
import Core.CommandLine.Messaging.MessageSender;
import Core.Database.API.DatabaseHandler;
import Core.Database.API.Params.StringParam;
import Core.Database.API.Params.UserParam;

import java.util.ArrayList;

public class User {

    private final int id;
    private final String username;

    public User(int id, String username) {
        this.id = id;
        this.username = username;

        smartParams = new SmartParamUpdaterInput(new ArrayList<SmartDouble>(),
                new ArrayList<SmartString>(),
                new UserParam(this));

        RegisterSmartParameters();
    }

    public static User Create(String nickname, String password)
    {
        DatabaseAPI database = DatabaseHandler.Get();
        int returnedId = database.AddUser(nickname,password);
        if(returnedId < 0)
        {
            // creating user failed
            return null;
        }
        User createdUser = UserBuilder.Build(returnedId,nickname);
        createdUser.InitCreatedUser();
        return createdUser;
    }

    public static User Get(int id)
    {
        DatabaseAPI database = DatabaseHandler.Get();
        String returnedNickname = database.GetUsername(id);
        if(returnedNickname == null)
        {
            // there is no user with that id
            return null;
        }
        User gettedUser = UserBuilder.Build(id,returnedNickname);
        SmartParamUpdater.GetParametersValuesFromDatabase(gettedUser.smartParams);
        return gettedUser;
    }

    public static User Get(String nickname)
    {
        DatabaseAPI database = DatabaseHandler.Get();
        int returnedId = database.GetUserID(nickname);
        if(returnedId < 0)
        {
            // creating user failed
            return null;
        }
        User gettedUser = UserBuilder.Build(returnedId,nickname);
        SmartParamUpdater.GetParametersValuesFromDatabase(gettedUser.smartParams);
        return gettedUser;
    }

    private void InitCreatedUser()
    {
        seeHiddenChannels.SetValue(false);
        superUser.SetValue(false);
        defaultChatroom.SetValue("");
        SmartParamUpdater.SetParametersValueInDatabase(smartParams);
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public static User AuthenticateUser(String username, String password)
    {
        DatabaseAPI database = DatabaseHandler.Get();
        boolean authenticateResult = database.AuthenticateUser(username,password);
        if(authenticateResult)
        {
            return Get(username);
        }
        return null;
    }

    public static User AuthenticateUser(String username, PlatformMessageHeader header)
    {
        DatabaseAPI database = DatabaseHandler.Get();
        boolean authenticateResult = database.AuthenticateUser(username,header);
        if(authenticateResult)
        {
            return Get(username);
        }
        return null;
    }

    public boolean CanSeeHiddenChannels()
    {
        return seeHiddenChannels.GetBoolean();
    }

    public boolean IsSuperUser()
    {
        return superUser.GetBoolean();
    }

    public Chatroom GetDefaultChatroom()
    {
        String defaultChatroomName = defaultChatroom.GetValue();
        Chatroom defaultChatroom = Chatroom.GetFromDatabase(defaultChatroomName);
        return defaultChatroom;
    }

    public boolean SetDefaultChatroom(String chatroomName)
    {
        Chatroom defaultChatroomCandidate = Chatroom.GetFromDatabase(chatroomName);

        if(defaultChatroomCandidate == null)
        {
            return false;
        }

        if(!defaultChatroomCandidate.HasUser(this))
        {
            return false;
        }

        defaultChatroom.SetValue(chatroomName);

        if(defaultChatroom.SetValueInDatabase(smartParams.getOwner()))
        {
            NoFormatMessage message = new NoFormatMessage(chatroomName+" set as default chatroom.");
            MessageSender.SystemMessageToUser(this,message);
            return true;
        }

        return false;
    }

    public boolean RemoveDefaultChatroom()
    {
        return defaultChatroom.RemoveValueFromDatabase(smartParams.getOwner());
    }

    public boolean AutoSetDefaultChatroomAfterLeavingChatroom(String leavedChatroomName)
    {
        Chatroom userDefaultChatroom = GetDefaultChatroom();
        if(userDefaultChatroom != null && leavedChatroomName.equals(userDefaultChatroom.GetName()))
        {
            RemoveDefaultChatroom();
            boolean newDefaultChatroomNameWasSet = AutoSetDefaultChatroomIfNone();
            return newDefaultChatroomNameWasSet;
        }
        return false;
    }

    public boolean AutoSetDefaultChatroomIfNone()
    {
        if(GetDefaultChatroom() != null)
        {
            return false;
        }
        String[] myChatrooms = DatabaseHandler.Get().chatrooms().GetUserBelongsToChatroomList(this);
        if(myChatrooms.length == 0)
        {
            return false;
        }
        SetDefaultChatroom(myChatrooms[0]);
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (id != user.id) return false;
        return username.equals(user.username);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + username.hashCode();
        return result;
    }

    protected void RegisterSmartParameters()
    {
        seeHiddenChannels = new SmartString("seeHiddenChannels","");
        superUser = new SmartString("superUser","");
        defaultChatroom = new SmartString("defaultChatroom","");

        smartParams.trackSmartParameter(seeHiddenChannels);
        smartParams.trackSmartParameter(superUser);
        smartParams.trackSmartParameter(defaultChatroom);
    }

    public boolean Remove()
    {
        PreRemove();

        SmartParamUpdater.RemoveParametersFromDatabase(smartParams);
        smartParams = null;
        DatabaseHandler.Get().RemoveUser(this.username);

        AfterRemove();
        return true;
    }

    protected void PreRemove()
    {

    }

    protected void AfterRemove()
    {

    }

    protected SmartParamUpdaterInput smartParams;

    private SmartString seeHiddenChannels;
    private SmartString superUser;
    private SmartString defaultChatroom;
}
