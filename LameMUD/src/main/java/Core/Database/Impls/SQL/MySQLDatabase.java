package Core.Database.Impls.SQL;

import Core.CommandLine.GameLogic.Item;
import Core.CommandLine.Platforms.PlatformMessageHeader;
import Core.CommandLine.User.User;
import Core.CommandLine.User.UserBuilder;
import Core.Database.API.DatabaseAPI;
import Core.Database.API.DatabaseChatrooms;
import Core.Database.API.Params.Param;
import Core.Database.API.Params.ParamOwner;
import Core.Database.Impls.SQL.Chatrooms.SQLDatabaseChatroom;
import Core.Database.Impls.SQL.Connection.SQLSelectedServer;
import Core.Database.Impls.SQL.Param.*;
import Core.Log.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQLDatabase implements DatabaseAPI {
    @Override
    public boolean AuthenticateUser(String username, String password) {
        boolean result = false;
        try {

            String hashedPwd = MySQLCommon.HashPassword(password);

            Connection con = SQLSelectedServer.getConnection();
            Statement stmt = con.createStatement();
            ResultSet uprs = stmt.executeQuery("SELECT * FROM usrs WHERE nickname='" + username + "' AND hashedPwd='" + hashedPwd+ "';");

            if(! uprs.next())
            {
                Logger.Log("No user found in database with nickname: " + username,
                        "SQL");
            }
            else
            {
                result = true;
            }

            if(uprs.next())
            {
                Logger.Log("There are more than one user in database with nickname: " + username,
                        "error","SQL");
            }

            uprs.close();
            stmt.close();
            con.close();

            return result;


        } catch (SQLException e) {
            Logger.Log(e.getMessage(),
                    "error","SQL");
            return false;
        }
    }

    @Override
    public boolean AuthenticateUser(String username, PlatformMessageHeader header) {

        if(!header.AllowTrustedLogin())
        {
            return false;
        }
        boolean isHeaderAuthenticated = SQLPlatforms.UserHaveThisTrustedPlatform(header,username);
        if(isHeaderAuthenticated)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    @Override
    public int AddUser(String username, String password) {

        if(IsUser(username))
        {
            return -1;
        }

        try {

            Connection con = SQLSelectedServer.getConnection();
            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE , ResultSet.CONCUR_UPDATABLE);
            ResultSet uprs = stmt.executeQuery("SELECT * FROM usrs ;");

            uprs.moveToInsertRow();

            uprs.updateString("nickname",username);
            uprs.updateString("hashedPwd",MySQLCommon.HashPassword(password));

            uprs.insertRow();

            uprs.close();
            stmt.close();
            con.close();

            return GetUserID(username);


        } catch (SQLException e) {
            Logger.Log(e.getMessage(),
                    "error","SQL");
            return -2;
        }

    }

    @Override
    public boolean RemoveUser(String username) {
        if(!IsUser(username))
        {
            return false;
        }

        try {

            Connection con = SQLSelectedServer.getConnection();
            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE , ResultSet.CONCUR_UPDATABLE);
            ResultSet uprs = stmt.executeQuery("SELECT * FROM usrs WHERE nickname='" + username + "';");

            uprs.first();
            uprs.deleteRow();

            uprs.close();
            stmt.close();
            con.close();

            return true;

        } catch (SQLException e) {
            Logger.Log(e.getMessage(),
                    "error","SQL");
            return false;
        }
    }

    @Override
    public int GetUserID(String username) {

        int result = -1;
        try {

            Connection con = SQLSelectedServer.getConnection();
            Statement stmt = con.createStatement();
            ResultSet uprs = stmt.executeQuery("SELECT * FROM usrs WHERE nickname='" + username + "';");

            if(! uprs.next())
            {
                Logger.Log("No user found in database with nickname: " + username,
                        "SQL");
            }
            else
            {
                result = uprs.getInt("id");
            }

            if(uprs.next())
            {
                Logger.Log("There are more than one user in database with nickname: " + username,
                        "error","SQL");
            }

            uprs.close();
            stmt.close();
            con.close();

            return result;


        } catch (SQLException e) {
            Logger.Log(e.getMessage(),
                    "error","SQL");
            return -2;
        }

    }

    @Override
    public String GetUsername(int id) {
        String result = null;
        try {

            Connection con = SQLSelectedServer.getConnection();
            Statement stmt = con.createStatement();
            ResultSet uprs = stmt.executeQuery("SELECT * FROM usrs WHERE id=" + id + ";");

            if(! uprs.next())
            {
                Logger.Log("No user found in database with id: " + id,
                        "SQL");
            }
            else
            {
                result = uprs.getString("nickname");
            }

            if(uprs.next())
            {
                Logger.Log("There are more than one user in database with id: " + id,
                        "error","SQL");
            }

            uprs.close();
            stmt.close();
            con.close();

            return result;


        } catch (SQLException e) {
            Logger.Log(e.getMessage(),
                    "error","SQL");
            return null;
        }
    }

    @Override
    public boolean IsUser(String username) {

        boolean result = false;
        try {

            Connection con = SQLSelectedServer.getConnection();
            Statement stmt = con.createStatement();
            ResultSet uprs = stmt.executeQuery("SELECT COUNT(*) FROM usrs WHERE nickname='" + username + "';");

            int count = -1;
            while(uprs.next())
            {
                count = uprs.getInt(1);
            }
            if(count == 1)
            {
                result = true;
            }
            else if (count == 0)
            {
                Logger.Log("No user found in database with nickname: " + username,
                        "SQL");
            }
            else if (count > 1)
            {
                Logger.Log("There are more than one user in database with nickname: " + username,
                        "error","SQL");
                result = true;
            }

            uprs.close();
            stmt.close();
            con.close();

            return result;

        } catch (SQLException e) {
            Logger.Log(e.getMessage(),
                    "error","SQL");
            return false;
        }
    }

    @Override
    public int AddItem(String classString) {

        try {

            Connection con = SQLSelectedServer.getConnection();
            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE , ResultSet.CONCUR_UPDATABLE);
            ResultSet uprs = stmt.executeQuery("SELECT * FROM items ;");

            uprs.moveToInsertRow();

            uprs.updateString("className",classString);

            uprs.insertRow();

            uprs.close();
            stmt.close();
            con.close();

            return GetIDOfNewestItem();// remake this shittoooo GetUser(username);


        } catch (SQLException e) {
            Logger.Log(e.getMessage(),
                    "error","SQL");
            return -1;
        }
    }

    private int GetIDOfNewestItem()
    {
        try {

            Connection con = SQLSelectedServer.getConnection();
            Statement stmt = con.createStatement();
            ResultSet uprs = stmt.executeQuery("SELECT max(id) FROM items ;");

            uprs.first();
            int result = uprs.getInt(1);

            uprs.close();
            stmt.close();
            con.close();

            return result;// remake this shittoooo GetUser(username);


        } catch (SQLException e) {
            Logger.Log(e.getMessage(),
                    "error","SQL");
            return -1;
        }
    }

    @Override
    public boolean RemoveItem(Item item) {

       return (RemoveItem(item.GetID()));

    }

    @Override
    public boolean RemoveItem(int itemID) {

        if(!IsItem(itemID))
        {
            return false;
        }

        try {

            Connection con = SQLSelectedServer.getConnection();
            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE , ResultSet.CONCUR_UPDATABLE);
            ResultSet uprs = stmt.executeQuery("SELECT * FROM items WHERE id="+itemID+";");

            uprs.first();
            uprs.deleteRow();

            uprs.close();
            stmt.close();
            con.close();

            return true;


        } catch (SQLException e) {
            Logger.Log(e.getMessage(),
                    "error","SQL");
            return false;
        }
    }

    @Override
    public String GetItemClassString(int id) {
        String result = null;
        try {

            Connection con = SQLSelectedServer.getConnection();
            Statement stmt = con.createStatement();
            ResultSet uprs = stmt.executeQuery("SELECT * FROM items WHERE id='" + id + "';");

            if(! uprs.next())
            {
                Logger.Log("No item found in database with id: " + id,
                        "SQL");
            }
            else
            {
                result = uprs.getString("className");
            }

            uprs.close();
            stmt.close();
            con.close();

            return result;


        } catch (SQLException e) {
            Logger.Log(e.getMessage(),
                    "error","SQL");
            return null;
        }
    }

    @Override
    public boolean IsItem(int id) {

        boolean result;
        try {

            Connection con = SQLSelectedServer.getConnection();
            Statement stmt = con.createStatement();
            ResultSet uprs = stmt.executeQuery("SELECT COUNT(*) FROM items WHERE id='" + id + "';");

            if(! uprs.next())
            {
                Logger.Log("No item found in database with id: " + id,
                        "SQL");
                result = false;
            }
            else
            {
                result = true;
            }

            uprs.close();
            stmt.close();
            con.close();

            return result;

        } catch (SQLException e) {
            Logger.Log(e.getMessage(),
                    "error","SQL");
            return false;
        }
    }

    @Override
    public boolean SetParam(ParamOwner owner, Param param) {
        SQLParamOwner properOwner = ParamToSQLParamConverter.convert(owner);
        SQLParam properParam = ParamToSQLParamConverter.convert(param);
        return MySQLCommon.SetTableParam(properOwner,properParam);
    }

    @Override
    public Object GetParam(ParamOwner owner, Param param) {
        SQLParamOwner properOwner = ParamToSQLParamConverter.convert(owner);
        SQLParam properParam = ParamToSQLParamConverter.convert(param);
        return MySQLCommon.GetTableParam(properOwner,properParam);
    }

    @Override
    public boolean RemoveParam(ParamOwner owner, Param param) {
        SQLParamOwner properOwner = ParamToSQLParamConverter.convert(owner);
        SQLParam properParam = ParamToSQLParamConverter.convert(param);
        return MySQLCommon.RemoveTableParam(properOwner,properParam);
    }

    @Override
    public boolean AddTrustedPlatformToUser(PlatformMessageHeader header, String username) {
        return SQLPlatforms.AddTrustedPlatformToUser(header,username);
    }

    @Override
    public boolean RemoveTrustedPlatformFromUser(PlatformMessageHeader header, String username) {
        return SQLPlatforms.RemoveTrustedPlatformFromUser(header,username);
    }

    @Override
    public DatabaseChatrooms chatrooms() {
        return chatrooms;
    }

    DatabaseChatrooms chatrooms = new SQLDatabaseChatroom();
}
