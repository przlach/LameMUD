package Core.CommandLine;

import Core.CommandLine.Messaging.MessageSender;
import Core.CommandLine.Platforms.PlatformMessageHeader;
import Core.CommandLine.User.User;

public class VerifiedMessage {

    public User GetUser()
    {
        return usr;
    }
    public void SetUser(User user)
    {
        this.usr = user;
    }

    public String GetMessage()
    {
        return message;
    }
    public void SetMessage(String message)
    {
        this.message = message;
    }

    public boolean IsUserLoggedIn()
    {
        return usr != null;
    }

    public PlatformMessageHeader getSenderPlatformHeader() {
        return senderPlatformHeader;
    }

    public void setSenderPlatformHeader(PlatformMessageHeader notLoggedInHeader) {
        this.senderPlatformHeader = notLoggedInHeader;
    }

    public void Reply(String replyMessage)
    {
        if(IsUserLoggedIn())
        {
            MessageSender.SystemMessageToUser(GetUser(),replyMessage);
        }
        else
        {
            getSenderPlatformHeader().SendMessage(replyMessage);
        }
    }

    private User usr;
    private String message;
    private PlatformMessageHeader senderPlatformHeader;


}
