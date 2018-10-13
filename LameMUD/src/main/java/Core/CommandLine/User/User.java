package Core.CommandLine.User;

import Core.CommandLine.Chatroom.Chatroom;
import Core.Database.API.DatabaseAPI;
import Core.Database.API.Params.ItemParam;
import Core.Params.SmartDouble;
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

    public User(String username,String password)
    {
        DatabaseAPI database = DatabaseHandler.Get();
        this.id = database.AddUser(username,password);
        this.username = username;
        smartParams = new SmartParamUpdaterInput(new ArrayList<SmartDouble>(),
                new ArrayList<SmartString>(),
                new UserParam(this));
        RegisterSmartParameters();
    }

    public User(int id, String username) {
        this.id = id;
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public boolean CanSeeHiddenChannels()
    {
        String paramResult = (String)DatabaseHandler.Get().GetParam(new UserParam(this),new StringParam("seeHiddenChannels","sendVal"));
        if(paramResult == null)
        {
            return false;
        }
        else if(paramResult.equals("sendVal"))
        {
            try {
                throw new Exception("GetParam() returned value set in param constructor");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if(paramResult.equals("true"))
        {
            return true;
        }
        return false;
    }

    public boolean IsSuperUser()
    {
        String paramResult = (String)DatabaseHandler.Get().GetParam(new UserParam(this),new StringParam("superUser","sendVal3"));
        if(paramResult == null)
        {
            return false;
        }
        else if(paramResult.equals("sendVal3"))
        {
            try {
                throw new Exception("IsBabbling() returned value set in param constructor");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if(paramResult.equals("true"))
        {
            return true;
        }
        return false;
    }

    public Chatroom GetDefaultChatroom()
    {
        String defaultChatroomName = (String)DatabaseHandler.Get().GetParam(new UserParam(this),new StringParam("defaultChatroom","sendVal"));
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

        boolean setParamResult = DatabaseHandler.Get().SetParam(new UserParam(this),new StringParam("defaultChatroom",chatroomName));

        if(setParamResult)
        {
            MessageSender.SystemMessageToUser(this,chatroomName+" set as default chatroom.");
        }

        return setParamResult;
    }

    public boolean RemoveDefaultChatroom()
    {
        boolean removeResult = DatabaseHandler.Get().RemoveParam(new UserParam(this),new StringParam("defaultChatroom","sendVal"));
        return removeResult;
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

    protected SmartParamUpdaterInput smartParams;

    private SmartString seeHiddenChannels;
    private SmartString superUser;
    private SmartString defaultChatroom;
}
