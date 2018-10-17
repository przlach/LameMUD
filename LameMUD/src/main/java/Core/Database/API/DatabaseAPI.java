package Core.Database.API;

import Core.Params.SmartObject.SmartObject;
import Core.CommandLine.Platforms.PlatformMessageHeader;
import Core.Database.API.Params.Param;
import Core.Database.API.Params.ParamOwner;

public interface DatabaseAPI {

    public boolean AuthenticateUser(String username, String password);
    public boolean AuthenticateUser(String username, PlatformMessageHeader header);

    public int AddUser(String username, String password);
    public boolean RemoveUser(String username);
    public int GetUserID(String username);
    public String GetUsername(int id);
    public boolean IsUser(String username);

    public int AddSmartObject(String classString);
    public boolean RemoveSmartObject(SmartObject smartObject);
    public boolean RemoveSmartObject(int smartObjectID);
    public String GetSmartObjectClassString(int id);
    public boolean IsSmartObject(int id);

    public boolean SetParam(ParamOwner owner, Param param);
    public Object GetParam(ParamOwner owner, Param param);
    public boolean RemoveParam(ParamOwner owner, Param param);

    public boolean AddTrustedPlatformToUser(PlatformMessageHeader header, String username);
    public boolean RemoveTrustedPlatformFromUser(PlatformMessageHeader header, String username);

    public DatabaseChatrooms chatrooms();

}
