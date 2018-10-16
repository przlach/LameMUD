package Core.Sessions;

import Core.CommandLine.Messaging.MessageSender;
import Core.CommandLine.Platforms.PlatformMessage;
import Core.CommandLine.Platforms.PlatformMessageHeader;
import Core.CommandLine.VerifiedMessage;
import Core.CommandLine.User.User;
import Core.Database.API.DatabaseAPI;
import Core.Database.API.DatabaseHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ActiveUsers {

    public static VerifiedMessage AuthenticateMessage(PlatformMessage msg)
    {
        User msgOwningUser = null;
        for (PlatformMessageHeader header : activeUserSessions.keySet() ) {
            if (AreMessageHeaderEqual(msg.GetHeader(), header)) {
                msgOwningUser = activeUserSessions.get(header);
                break;
            }
        }
        VerifiedMessage verifiedMessage = new VerifiedMessage();
        verifiedMessage.SetUser(msgOwningUser);
        verifiedMessage.SetMessage(msg.GetMessage());
        verifiedMessage.setSenderPlatformHeader(msg.GetHeader());

        return verifiedMessage;
}
    
    private static boolean AreMessageHeaderEqual(PlatformMessageHeader one, PlatformMessageHeader other)
    {
        if(!one.GetPlatformIdentifier().equals(other.GetPlatformIdentifier()))
        {
            return false;
        }
        if(!one.GetUserIdentifier().equals(other.GetUserIdentifier()))
        {
            return false;
        }
        if(!one.GetSessionIdentifier().equals(other.GetSessionIdentifier()))
        {
            return false;
        }
        return true;
    }

    private static boolean AreUsersEqual(User one, User other)
    {
        return one.getId() == other.getId();
    }

    public static boolean IsHeaderActive(PlatformMessageHeader header)
    {
        for (PlatformMessageHeader activeHeader : activeUserSessions.keySet() ) {
            if (AreMessageHeaderEqual(activeHeader, header)) {
                return true;
            }
        }
        return false;
    }

    public static boolean IsUserActive(User usr)
    {
        for (User activeUser : activeUserSessions.values() ) {
            if (AreUsersEqual(activeUser, usr)) {
                return true;
            }
        }
        return false;
    }

    public static boolean SetHeaderActive(PlatformMessageHeader header, String username, String authPassword)
    {
        if(IsHeaderActive(header))
        {
            return false;  // TODO not enough failure results information, return other more complicated type than boolean
        }


        // case of multiple platforms logged at the same time, for one user

        // case if you try to add another header, of the same platform to the same user, determined by header.AllowsMultipleSessionsFromSamePlatform()

        DatabaseAPI database = DatabaseHandler.Get();
        User verifyResult;
        verifyResult = User.AuthenticateUser(username,authPassword);

        if(verifyResult != null)
        {
            if(!activeUserSessions.containsValue(verifyResult))
            {
                activeUserSessions.put(header,verifyResult);
                MessageSender.SendStashedMessages(verifyResult);   // send messages that user couldn't receive because they were send when he was offline
            }
            else
            {
                activeUserSessions.put(header,verifyResult);
            }

            return true;
        }
        else
        {
            return false;
        }

    }

    public static boolean SetHeaderActive(PlatformMessageHeader header, String username)
    {
        if(IsHeaderActive(header))
        {
            return false;  // TODO not enough failure results information, return other more complicated type than boolean
        }


        // case of multiple platforms logged at the same time, for one user

        // case if you try to add another header, of the same platform to the same user, determined by header.AllowsMultipleSessionsFromSamePlatform()

        DatabaseAPI database = DatabaseHandler.Get();
        User verifyResult;

        if(!header.AllowTrustedLogin())
        {
            return false;   // TODO another result type
        }
        else
        {
            verifyResult = User.AuthenticateUser(username, header);
        }


        if(verifyResult != null)
        {
            if(!activeUserSessions.containsValue(verifyResult))
            {
                activeUserSessions.put(header,verifyResult);
                MessageSender.SendStashedMessages(verifyResult);   // send messages that user couldn't receive because they were send when he was offline
            }
            else
            {
                activeUserSessions.put(header,verifyResult);
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    public static boolean SetHeaderInactive(PlatformMessageHeader header)
    {
        if(!IsHeaderActive(header))
        {
            return false;
        }

        for (PlatformMessageHeader activeHeader : activeUserSessions.keySet() ) {
            if (AreMessageHeaderEqual(activeHeader, header)) {
                activeUserSessions.remove(activeHeader);
                break;
            }
        }
        return true;
    }

    public static boolean SetUserInactive(User usr)
    {
        if(!IsUserActive(usr))
        {
            return false;
        }

        for (PlatformMessageHeader activeHeader : activeUserSessions.keySet() ) {
            if (AreUsersEqual(activeUserSessions.get(activeHeader), usr)) {
                activeUserSessions.remove(activeHeader);
            }
        }
        return true;
    }

    public static ArrayList<PlatformMessageHeader> GetUsersActivePlatforms(User user)
    {
        ArrayList<PlatformMessageHeader> activePlatforms = new ArrayList<PlatformMessageHeader>();

        for (PlatformMessageHeader header : activeUserSessions.keySet() ) {

            if(activeUserSessions.get(header).getId()==user.getId())
            {
                activePlatforms.add(header);
            }
        }
        return activePlatforms;

    }

    static
    {
        activeUserSessions = new HashMap<PlatformMessageHeader, User>();
    }

    private static Map<PlatformMessageHeader, User> activeUserSessions;
}
