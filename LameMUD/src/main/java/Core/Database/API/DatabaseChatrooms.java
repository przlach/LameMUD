package Core.Database.API;

import Core.CommandLine.User.User;

import java.util.ArrayList;

public interface DatabaseChatrooms {
    
    public boolean AddUserToUsersArray(int chatroomID, int userID);
    public boolean IsUserInUsersArray(int chatroomID, int userID);
    public boolean RemoveUserFromUsersArray(int chatroomID, int userID);
    public boolean RemoveUsersArray(int chatroomID);
    public ArrayList<Integer> GetUsersArray(int chatroomID);
    public ArrayList<Integer> GetChatroomsUserBelongsTo(int userID);
    public boolean IsChatroom(String name);
    public int GetChatroomID(String name);
    public String[] GetPublicChatroomList();
    public String[] GetEveryChatroomList();
    public String[] GetUserBelongsToChatroomList(User user);

}
