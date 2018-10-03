package Core.Database.API;

import Core.CommandLine.GameLogic.Item;
import Core.CommandLine.Platforms.PlatformMessageHeader;
import Core.CommandLine.User.User;
import Core.Database.API.Params.Param;
import Core.Database.API.Params.ParamOwner;

public interface DatabaseAPI {

    public User AuthenticateUser(String username, String password);
    public User AuthenticateUser(String username, PlatformMessageHeader header);

    public User AddUser(String username, String password);
    public boolean RemoveUser(String username);
    public User GetUser(String username);
    public boolean IsUser(String username);

    public int AddItem(String classString);
    public boolean RemoveItem(Item item);
    public boolean RemoveItem(int itemID);
    public String GetItemClassString(int id);
    public boolean IsItem(int id);

    public boolean SetParam(ParamOwner owner, Param param);
    public Object GetParam(ParamOwner owner, Param param);
    public boolean RemoveParam(ParamOwner owner, Param param);

    public boolean AddTrustedPlatformToUser(PlatformMessageHeader header, String username);
    public boolean RemoveTrustedPlatformFromUser(PlatformMessageHeader header, String username);

    public DatabaseChatrooms chatrooms();

}
