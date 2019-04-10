package Core.CommandLine.Messaging;

import Core.CommandLine.Platforms.PlatformMessageHeader;
import Core.CommandLine.User.User;
import Core.Sessions.ActiveUsers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

public class MessageSender {

    private static void SystemMessageToUser(User target, String msg)
    {
        if(ActiveUsers.IsUserActive(target))
        {
            SystemMessageToLoggedUserSending(target,msg);
        }
        else
        {
            StashMessage(target,msg);
        }
    }

    public static void SystemMessageToUser(User target, FormattedMessage msg)
    {
        SystemMessageToUser(target,msg.Get());
    }

    public static void SystemMessageToUser(Collection<User> targets, FormattedMessage msg)
    {
        SystemMessageToUser(targets,msg);
    }

    private static void StashMessage(User target,String msg)
    {
        LinkedList<String> userStashedMessages = GetUserMessagesToReceive(target);
        if(userStashedMessages != null) {
            userStashedMessages.add(msg);
            return;
        }
        else {
            messagesToSend.put(target, new LinkedList<String>());
            messagesToSend.get(target).add(msg);
        }
    }

    private static void SystemMessageToLoggedUserSending(User target,String msg)
    {
        ArrayList<PlatformMessageHeader> sendTargets = ActiveUsers.GetUsersActivePlatforms(target);
        for (PlatformMessageHeader header:sendTargets)
        {
            header.SendMessage(msg);
        }
    }

    private static LinkedList<String> GetUserMessagesToReceive(User target)
    {
        for(User inList:messagesToSend.keySet())
        {
            if(inList.getId()==target.getId())
            {
                return messagesToSend.get(inList);
            }
        }
        return null;
    }

    public static void SendStashedMessages(User user)
    {
        LinkedList<String> userStashedMessages = GetUserMessagesToReceive(user);

        if(userStashedMessages != null)
        {
            for (String message:userStashedMessages)
            {
                SystemMessageToLoggedUserSending(user,message);
            }
            RemoveUserStash(user);
        }
    }

    private static void RemoveUserStash(User user)
    {
        for(User inList:messagesToSend.keySet())
        {
            if(inList.getId()==user.getId())
            {
                messagesToSend.remove(inList);
                return;
            }
        }
    }

    static
    {
        messagesToSend = new HashMap<User,LinkedList<String>>();
    }

    private static HashMap<User,LinkedList<String>> messagesToSend;

}
