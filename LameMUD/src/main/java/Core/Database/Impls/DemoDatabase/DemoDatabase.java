package Core.Database.Impls.DemoDatabase;

import Core.CommandLine.GameLogic.Item;
import Core.CommandLine.Platforms.PlatformMessageHeader;
import Core.CommandLine.User.User;
import Core.CommandLine.User.UserBuilder;
import Core.Database.API.DatabaseAPI;
import Core.Database.API.DatabaseChatrooms;
import Core.Database.API.Params.Param;
import Core.Database.API.Params.ParamOwner;

import java.util.HashMap;
import java.util.Map;

public class DemoDatabase implements DatabaseAPI {

    @Override
    public User AuthenticateUser(String username, String password) {

        for(UserTableRow usrRow:users.values())
        {
            if(usrRow.getUsername().equals(username))
            {
                if(usrRow.getPassword().equals(password))
                {
                    return UserBuilder.ObsoleteBuild(usrRow.getId(),usrRow.getUsername());
                }
            }
        }
        return null;

    }

    @Override
    public User AuthenticateUser(String username, PlatformMessageHeader header) {

        // note: this won't fucking work
        for(UserParamStringRow row:stringParams.values())
        {
            if(row.getParamName().equals(TRUSTED_PLATFORM_ID_PREFIX+header.GetPlatformIdentifier()) && row.getParamVal().equals(header.GetUserIdentifier()))
            {
                UserTableRow usrRow= users.get(row.userID);
                return UserBuilder.ObsoleteBuild(usrRow.getId(),usrRow.getUsername());
            }
        }
        return null;

    }

    @Override
    public User AddUser(String username, String password) {

        if(GetUser(username) == null)
        {
            UserTableRow row = new UserTableRow(username,password);
            users.put(row.getId(),row);
            User returnVal = UserBuilder.ObsoleteBuild(row.getId(),row.getUsername());
            return returnVal;
        }
        else
        {
            return null;
        }
    }

    @Override
    public boolean RemoveUser(String username) {

        User toBeDeleted = GetUser(username);
        if(toBeDeleted != null)
        {
            users.remove(toBeDeleted.getId());
            return true;
        }
        else
        {
            return false;
        }
    }

    @Override
    public User GetUser(String username) {

        User user = null;
        for(UserTableRow row:users.values())
        {
            if(row.getUsername().equals(username))
            {
                user = UserBuilder.ObsoleteBuild(row.getId(),row.getUsername());
                return user;
            }
        }
        return null;
    }

    // TODO implement everything below

    @Override
    public boolean IsUser(String username) {
        return false;
    }

    @Override
    public int AddItem(String classString) {
        return -1;
    }

    @Override
    public boolean RemoveItem(Item item) {
        return false;
    }

    @Override
    public boolean RemoveItem(int itemID) {
        return false;
    }

    @Override
    public String GetItemClassString(int id) {
        return null;
    }

    @Override
    public boolean IsItem(int id) {
        return false;
    }

    @Override
    public boolean SetParam(ParamOwner owner, Param param) {
        return false;
    }

    @Override
    public Object GetParam(ParamOwner owner, Param param) {
        return null;
    }

    @Override
    public boolean RemoveParam(ParamOwner owner, Param param) {
        return false;
    }


    public boolean SetUserDoubleParam(User user, String paramName, double paramValue) {

        for(UserParamDoubleRow row:doubleParams.values())
        {
            if(row.getUserID()==user.getId() && paramName.equals(row.getParamName()))
            {
                row.setParamVal(paramValue);
                return false;
            }
        }
        UserParamDoubleRow row = new UserParamDoubleRow(user.getId(),paramName,paramValue);
        doubleParams.put(row.getId(),row);
        return true;
    }

    public Double GetUserDoubleParam(User user, String paramName) {

        for(UserParamDoubleRow row:doubleParams.values())
        {
            if(row.getUserID()==user.getId() && paramName.equals(row.getParamName()))
            {
                return row.getParamVal();
            }
        }
        return null;
    }

    public boolean RemoveUserDoubleParam(User user, String paramName) {
        for(UserParamDoubleRow row:doubleParams.values())
        {
            if(row.getUserID()==user.getId() && paramName.equals(row.getParamName()))
            {
                doubleParams.remove(row.getId());
                return true;
            }
        }
        return false;
    }

    public boolean SetUserStringParam(User user, String paramName, String paramValue) {

        for(UserParamStringRow row:stringParams.values())
        {
            if(row.getUserID()==user.getId() && paramName.equals(row.getParamName()))
            {
                row.setParamVal(paramValue);
                return false;
            }
        }
        UserParamStringRow row = new UserParamStringRow(user.getId(),paramName,paramValue);
        stringParams.put(row.getId(),row);
        return true;
    }

    public String GetUserStringParam(User user, String paramName) {
        for(UserParamStringRow row:stringParams.values())
        {
            if(row.getUserID()==user.getId() && paramName.equals(row.getParamName()))
            {
                return row.getParamVal();
            }
        }
        return null;
    }

    public boolean RemoveUserStringParam(User user, String paramName) {
        for(UserParamStringRow row:stringParams.values())
        {
            if(row.getUserID()==user.getId() && paramName.equals(row.getParamName()))
            {
                doubleParams.remove(row.getId());
                return true;
            }
        }
        return false;
    }


    public boolean SetItemDoubleParam(Item item, String paramName, double paramValue) {
        return false;
    }


    public Double GetItemDoubleParam(Item item, String paramName) {
        return null;
    }


    public boolean RemoveItemDoubleParam(Item item, String paramName) {
        return false;
    }

    public boolean SetItemStringParam(Item item, String paramName, String paramValue) {
        return false;
    }

    public String GetItemStringParam(Item item, String paramName) {
        return null;
    }


    public boolean RemoveItemStringParam(Item item, String paramName) {
        return false;
    }

    @Override
    public boolean AddTrustedPlatformToUser(PlatformMessageHeader header, String username) {

        User usr = GetUser(username);

        if(usr == null)
        {
            return false;
        }

        if(GetUserStringParam(usr,TRUSTED_PLATFORM_ID_PREFIX+header.GetPlatformIdentifier()) != null)
        {
            return false;
        }

        if(IsHeaderAlreadyTrusted(header))
        {
            return false;
        }

        SetUserStringParam(usr,TRUSTED_PLATFORM_ID_PREFIX+header.GetPlatformIdentifier(),header.GetUserIdentifier());
        return true;
    }

    private boolean IsHeaderAlreadyTrusted(PlatformMessageHeader header)
    {
        for(UserParamStringRow row:stringParams.values())
        {
            if(row.paramName.equals(TRUSTED_PLATFORM_ID_PREFIX+header.GetPlatformIdentifier()))
            {
                if(row.paramVal.equals(header.GetUserIdentifier()))
                {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean RemoveTrustedPlatformFromUser(PlatformMessageHeader header, String username) {

        User usr = GetUser(username);

        if(usr == null)
        {
            return false;
        }

        if(GetUserStringParam(usr,TRUSTED_PLATFORM_ID_PREFIX+header.GetPlatformIdentifier()) == null)
        {
            return false;
        }
        RemoveUserStringParam(usr,TRUSTED_PLATFORM_ID_PREFIX+header.GetPlatformIdentifier());
        return true;
    }

    @Override
    public DatabaseChatrooms chatrooms() {
        return null;
    }

    public DemoDatabase()
    {
        users = new HashMap<Integer, UserTableRow>();
        doubleParams = new HashMap<Integer, UserParamDoubleRow>();
        stringParams = new HashMap<Integer, UserParamStringRow>();
    }

    private final String TRUSTED_PLATFORM_ID_PREFIX = "USER_TRUSTED_ID_OF_";
    private Map<Integer,UserTableRow> users;
    private Map<Integer,UserParamDoubleRow> doubleParams;
    private Map<Integer,UserParamStringRow> stringParams;

}
