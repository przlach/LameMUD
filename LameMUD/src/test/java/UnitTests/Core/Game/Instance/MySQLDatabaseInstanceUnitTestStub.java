package UnitTests.Core.Game.Instance;

import Core.CommandLine.Platforms.PlatformMessageHeader;
import Core.Database.API.DatabaseAPI;
import Core.Database.API.DatabaseChatrooms;
import Core.Database.API.DatabaseGameInstances;
import Core.Database.API.Params.Param;
import Core.Database.API.Params.ParamOwner;
import Core.Params.SmartObject.SmartObject;

public class MySQLDatabaseInstanceUnitTestStub implements DatabaseAPI {

    public MySQLDatabaseInstanceUnitTestStub(DatabaseGameInstances gameInstances)
    {
        //instances = gameInstances; TODO REMOVE
    }

    @Override
    public boolean AuthenticateUser(String username, String password) {
        return false;
    }

    @Override
    public boolean AuthenticateUser(String username, PlatformMessageHeader header) {
        return false;
    }

    @Override
    public int AddUser(String username, String password) {
        return 0;
    }

    @Override
    public boolean RemoveUser(String username) {
        return false;
    }

    @Override
    public int GetUserID(String username) {
        return 0;
    }

    @Override
    public String GetUsername(int id) {
        return null;
    }

    @Override
    public boolean IsUser(String username) {
        return false;
    }

    @Override
    public int AddSmartObject(String classString) {
        return 0;
    }

    @Override
    public boolean RemoveSmartObject(SmartObject smartObject) {
        return false;
    }

    @Override
    public boolean RemoveSmartObject(int smartObjectID) {
        return false;
    }

    @Override
    public String GetSmartObjectClassString(int id) {
        return null;
    }

    @Override
    public boolean IsSmartObject(int id) {
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

    @Override
    public boolean AddTrustedPlatformToUser(PlatformMessageHeader header, String username) {
        return false;
    }

    @Override
    public boolean RemoveTrustedPlatformFromUser(PlatformMessageHeader header, String username) {
        return false;
    }

    @Override
    public DatabaseChatrooms chatrooms() {
        return null;
    }

//    @Override TODO REMOVE
//    public DatabaseGameInstances instances() {
//        return instances;
//    }

//    DatabaseGameInstances instances;
}
