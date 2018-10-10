package Core.Database.Impls.SQL.Chatrooms;

import Core.CommandLine.Chatroom.Chatroom;
import Core.CommandLine.Chatroom.ChatroomBuilder;
import Core.CommandLine.User.User;
import Core.Database.API.DatabaseChatrooms;
import Core.Database.Impls.SQL.Connection.SQLSelectedServer;
import Core.Log.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class SQLDatabaseChatroom implements DatabaseChatrooms {

    @Override
    public boolean AddUserToUsersArray(int chatroomID, int userID)
    {
        if(IsUserInUsersArray(chatroomID, userID))
        {
            return false;
        }

        try {

            Connection con = SQLSelectedServer.getConnection();
            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE , ResultSet.CONCUR_UPDATABLE);
            ResultSet uprs = stmt.executeQuery("SELECT * FROM usrsInChatrooms ;");

            uprs.moveToInsertRow();

            uprs.updateInt("chatroomID",chatroomID);
            uprs.updateInt("userID",userID);

            uprs.insertRow();

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
    public boolean IsUserInUsersArray(int chatroomID, int userID) {

        boolean result=false;
        try {

            Connection con = SQLSelectedServer.getConnection();
            Statement stmt = con.createStatement();
            ResultSet uprs = stmt.executeQuery("SELECT COUNT(*) FROM usrsInChatrooms WHERE chatroomID='" + chatroomID + "' AND userID='"+userID+"';");

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
                Logger.Log("There is no user with id: " + userID + " in chatroom with id: " + chatroomID,
                        "SQL");
            }
            else if (count > 1)
            {
                Logger.Log("There are more users with id: " + userID + " in chatroom with id: " + chatroomID,
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
    public boolean RemoveUserFromUsersArray(int chatroomID, int userID) {

        if(!IsUserInUsersArray(chatroomID,userID))
        {
            return false;
        }

        try {

            Connection con = SQLSelectedServer.getConnection();
            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE , ResultSet.CONCUR_UPDATABLE);
            ResultSet uprs = stmt.executeQuery("SELECT * FROM usrsInChatrooms WHERE chatroomID='" + chatroomID + "' AND userID='"+userID+"';");

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
    public boolean RemoveUsersArray(int chatroomID) {

        try {

            Connection con = SQLSelectedServer.getConnection();
            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE , ResultSet.CONCUR_UPDATABLE);
            stmt.executeUpdate("DELETE FROM usrsInChatrooms WHERE chatroomID='" + chatroomID + "';");

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
    public ArrayList<Integer> GetUsersArray(int chatroomID) {

        try {

            Connection con = SQLSelectedServer.getConnection();
            Statement stmt = con.createStatement();
            ResultSet uprs = stmt.executeQuery("SELECT userID FROM usrsInChatrooms WHERE chatroomID='" + chatroomID + "';");

            ArrayList<Integer> usersIDs = new ArrayList<Integer>();

            while(uprs.next())
            {
                usersIDs.add(uprs.getInt("userID"));
            }

            uprs.close();
            stmt.close();
            con.close();

            return usersIDs;

        } catch (SQLException e) {
            Logger.Log(e.getMessage(),
                    "error","SQL");
            return null;
        }
    }

    @Override
    public ArrayList<Integer> GetChatroomsUserBelongsTo(int userID) {
        try {

            Connection con = SQLSelectedServer.getConnection();
            Statement stmt = con.createStatement();
            ResultSet uprs = stmt.executeQuery("SELECT chatroomID FROM usrsInChatrooms WHERE userID='" + userID + "';");

            ArrayList<Integer> chatroomsIDs = new ArrayList<Integer>();

            while(uprs.next())
            {
                chatroomsIDs.add(uprs.getInt("chatroomID"));
            }

            uprs.close();
            stmt.close();
            con.close();

            return chatroomsIDs;

        } catch (SQLException e) {
            Logger.Log(e.getMessage(),
                    "error","SQL");
            return null;
        }
    }

    @Override
    public boolean IsChatroom(String name) {
        int chatroomID = GetChatroomID(name);
        if(chatroomID >= 0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    @Override
    public int GetChatroomID(String name) {

        try {

            Connection con = SQLSelectedServer.getConnection();
            Statement stmt = con.createStatement();

            Chatroom chatroom = (Chatroom)ChatroomBuilder.Get().Build(-4);
            String query = "SELECT items.id " +
                           "FROM items INNER JOIN itemsStringParams " +
                           "ON items.id=itemsStringParams.itemId " +
                           "WHERE items.className='"+chatroom.getItemClassString()+"' " +
                                 "AND itemsStringParams.paramName='name' " +
                                 "AND itemsStringParams.paramVal='"+name+"';";

            ResultSet uprs = stmt.executeQuery(query);

            int chatroomID = -1;

            if(uprs.next())
            {
                chatroomID = uprs.getInt("id");
            }

            if(uprs.next())
            {
                Logger.Log("There are multiple chatrooms with the name: " + name,
                        "error","SQL");
            }

            uprs.close();
            stmt.close();
            con.close();

            return chatroomID;

        } catch (SQLException e) {
            Logger.Log(e.getMessage(),
                    "error","SQL");
            return -1;
        }
    }

    @Override
    public String[] GetPublicChatroomList() {

        try {

            Connection con = SQLSelectedServer.getConnection();
            Statement stmt = con.createStatement();

            String query = "SELECT paramVal FROM "+
                           "(SELECT id FROM " +
                             "(SELECT itemID FROM " +
                               "itemsStringParams WHERE itemID NOT IN " +
                                 "(SELECT itemID FROM itemsStringParams " +
                                 "WHERE paramName='hidden'AND paramVal='true') " +
                               "GROUP BY itemID) " +
                             "table1 " +
                           "INNER JOIN (SELECT id FROM items WHERE className='chatroom') table2 ON table1.itemID=table2.id) " +
                         "visibleChatroomsIDs " +
                         "INNER join " +
                         "itemsStringParams " +
                         "ON visibleChatroomsIDs.id=itemsStringParams.itemID " +
                         "WHERE paramName='name';";

            ResultSet uprs = stmt.executeQuery(query);

            ArrayList<String> publicChatroomsNames = new ArrayList<String>();

            while(uprs.next())
            {
                publicChatroomsNames.add(uprs.getString("paramVal"));
            }

            uprs.close();
            stmt.close();
            con.close();

            return publicChatroomsNames.toArray(new String[0]);

        } catch (SQLException e) {
            Logger.Log(e.getMessage(),
                    "error", "SQL");
            return null;
        }
    }

    @Override
    public String[] GetEveryChatroomList() {

        try {

            Connection con = SQLSelectedServer.getConnection();
            Statement stmt = con.createStatement();

            Chatroom chatroom = (Chatroom)ChatroomBuilder.Get().Build(-4);
            String query = "SELECT itemsStringParams.paramVal " +
                    "FROM items INNER JOIN itemsStringParams " +
                    "ON items.id=itemsStringParams.itemId " +
                    "WHERE items.className='"+chatroom.getItemClassString()+"' AND itemsStringParams.paramName='name';";

            ResultSet uprs = stmt.executeQuery(query);

            ArrayList<String> publicChatroomsNames = new ArrayList<String>();

            while(uprs.next())
            {
                publicChatroomsNames.add(uprs.getString("paramVal"));
            }

            uprs.close();
            stmt.close();
            con.close();

            return publicChatroomsNames.toArray(new String[0]);

        } catch (SQLException e) {
            Logger.Log(e.getMessage(),
                    "error", "SQL");
            return null;
        }
    }

    @Override
    public String[] GetUserBelongsToChatroomList(User user) {

        try {

            Connection con = SQLSelectedServer.getConnection();
            Statement stmt = con.createStatement();

            String query = "SELECT paramVal FROM " +
                    "(SELECT chatroomID " +
                    "FROM usrsInChatrooms " +
                    "WHERE userID="+user.getId()+") " +
                    "userBelongsToChatrooms " +
                    "INNER join " +
                    "itemsStringParams " +
                    "ON userBelongsToChatrooms.chatroomID=itemsStringParams.itemID " +
                    "WHERE paramName='name';";

            ResultSet uprs = stmt.executeQuery(query);

            ArrayList<String> publicChatroomsNames = new ArrayList<String>();

            while(uprs.next())
            {
                publicChatroomsNames.add(uprs.getString("paramVal"));
            }

            uprs.close();
            stmt.close();
            con.close();

            return publicChatroomsNames.toArray(new String[0]);

        } catch (SQLException e) {
            Logger.Log(e.getMessage(),
                    "error", "SQL");
            return null;
        }
    }
}
