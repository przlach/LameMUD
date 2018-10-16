package Core.Database.Impls.SQL;

import Core.CommandLine.Platforms.PlatformMessageHeader;
import Core.CommandLine.User.User;
import Core.Database.API.DatabaseAPI;
import Core.Database.API.DatabaseHandler;
import Core.Database.Impls.SQL.Connection.SQLSelectedServer;
import Core.Log.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

class SQLPlatforms {

    private static int AddPlatform(String platformName)
    {
        if(GetPlatformID(platformName) != -1)
        {
            return -1;
        }

        int idOfAddedPlatform = -1;

        try {

            Connection con = SQLSelectedServer.getConnection();
            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE , ResultSet.CONCUR_UPDATABLE);
            ResultSet uprs = stmt.executeQuery("SELECT * FROM platforms ;");

            uprs.moveToInsertRow();

            uprs.updateString("name",platformName);

            uprs.insertRow();

            uprs.close();
            stmt.close();
            con.close();

            idOfAddedPlatform = GetPlatformID(platformName);

            return idOfAddedPlatform;


        } catch (SQLException e) {
            Logger.Log(e.getMessage(),
                    "error","SQL");
            return idOfAddedPlatform;
        }
    }
    private static int GetPlatformID(String platformName)
    {
        int platformID = -1;
        try {

            Connection con = SQLSelectedServer.getConnection();
            Statement stmt = con.createStatement();
            ResultSet uprs = stmt.executeQuery("SELECT * FROM platforms WHERE name='" + platformName + "';");

            if(! uprs.next())
            {
                Logger.Log("No platform found in database named: " + platformName,
                        "SQL");
            }
            else
            {
                platformID = uprs.getInt("id");
            }

            if(uprs.next())
            {
                Logger.Log("There are more than one platform in database named: " + platformName,
                        "error","SQL");
            }

            uprs.close();
            stmt.close();
            con.close();

            return platformID;


        } catch (SQLException e) {
            Logger.Log(e.getMessage(),
                    "error","SQL");
            return platformID;
        }
    }
    private static boolean RemovePlatform(String platformName)
    {
        if(GetPlatformID(platformName) == -1)
        {
            return false;
        }

        try {

            Connection con = SQLSelectedServer.getConnection();
            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE , ResultSet.CONCUR_UPDATABLE);
            ResultSet uprs = stmt.executeQuery("SELECT * FROM platforms WHERE name='" + platformName + "';");

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

    static boolean AddTrustedPlatformToUser(PlatformMessageHeader header, String username)
    {
        DatabaseAPI database = DatabaseHandler.Get();
        User user = User.Get(username);

        if(user == null)
        {
            return false;
        }

        if(!header.AllowTrustedLogin())
        {
            return false;
        }

        if(UserHaveThisTrustedPlatform(header,username))
        {
            return false;
        }

        int userID = user.getId();

        int platformID = GetPlatformID(header.GetPlatformIdentifier());
        if(platformID == -1)
        {
            userID = AddPlatform(header.GetPlatformIdentifier());
        }

        boolean addingResult = false;
        try {

            Connection con = SQLSelectedServer.getConnection();
            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE , ResultSet.CONCUR_UPDATABLE);
            ResultSet uprs = stmt.executeQuery("SELECT * FROM trustedPlatforms ;");

            uprs.moveToInsertRow();

            uprs.updateInt("platformID",platformID);
            uprs.updateString("headerID",header.GetUserIdentifier());
            uprs.updateInt("userID",userID);

            uprs.insertRow();

            uprs.close();
            stmt.close();
            con.close();

            addingResult = true;
            return true;


        } catch (SQLException e) {
            Logger.Log(e.getMessage(),
                    "error","SQL");
            return addingResult;
        }

    }

    static boolean RemoveTrustedPlatformFromUser(PlatformMessageHeader header, String username)
    {
        DatabaseAPI database = DatabaseHandler.Get();
        User user = User.Get(username);

        if(user == null)
        {
            return false;
        }

        int userID = user.getId();

        int platformID = GetPlatformID(header.GetPlatformIdentifier());
        if(platformID == -1)
        {
            userID = AddPlatform(header.GetPlatformIdentifier());
        }

        try {

            Connection con = SQLSelectedServer.getConnection();
            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE , ResultSet.CONCUR_UPDATABLE);
            ResultSet uprs = stmt.executeQuery("SELECT * FROM trustedPlatforms WHERE platformID='" + platformID + "' AND userID='" + userID + "' AND headerID='" + header.GetUserIdentifier() + "';");

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

    static boolean UserHaveThisTrustedPlatform(PlatformMessageHeader header, String username)
    {
        DatabaseAPI database = DatabaseHandler.Get();
        User user = User.Get(username);

        if(user == null)
        {
            return false;
        }

        if(!header.AllowTrustedLogin())
        {
            return false;
        }

        int userID = user.getId();

        int platformID = GetPlatformID(header.GetPlatformIdentifier());
        if(platformID == -1)
        {
            userID = AddPlatform(header.GetPlatformIdentifier());
        }

        boolean result = false;
        try {

            Connection con = SQLSelectedServer.getConnection();
            Statement stmt = con.createStatement();
            ResultSet uprs = stmt.executeQuery("SELECT COUNT(*) FROM trustedPlatforms WHERE platformID='" + platformID + "' AND userID='" + userID + "' AND headerID='" + header.GetUserIdentifier() + "';");

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
                Logger.Log("User: " + username + " doesn't have trusted platform with platformID: " + platformID + " and userID: " + header.GetUserIdentifier(),
                        "SQL");
            }
            else if (count > 1)
            {
                Logger.Log("User: " + username + " has multiple rows of trusted platform with platformID: " + platformID + " and userID: " + header.GetUserIdentifier(),
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

    static PlatformMessageHeader[] GetTrustedHeadersOfUser(String username)
    {
        return null;  // TODO
    }

    static User[] GetUsersWhoTrustHeader(PlatformMessageHeader header)
    {
        return null; // TODO
    }

}
