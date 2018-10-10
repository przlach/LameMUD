package Extensions.Platforms;

import Core.CommandLine.Platforms.PlatformMessageHeader;
import Extensions.Platforms.LocalExecutionPlatform.GUI.LocalClient;

public class LocalExecutionPlatformHeader implements PlatformMessageHeader {

    public LocalExecutionPlatformHeader(LocalClient owner)
    {
        this.owner = owner;
    }

    public void SendMessage(String msgReply) {
        owner.ReceiveMessage(msgReply);
    }

    public void SendAuthToken(String token) {

    }

    public boolean AllowTrustedLogin() {
        return true;
    }

    public boolean AllowsMultipleSessionsFromSamePlatform() {
        return true;
    }

    public String GetPlatformIdentifier() {
        return platformIdentifier;
    }

    public String GetUserIdentifier() {
        return userIdentifier;
    }

    public String GetSessionIdentifier() {
        return sessionIdentifier;
    }

    public void setUserIdentifier(String userIdentifier) {
        this.userIdentifier = userIdentifier;
    }

    public void setSessionIdentifier(String sessionIdentifier) {
        this.sessionIdentifier = sessionIdentifier;
    }

    private final String platformIdentifier = "localExecutionPlatformIdentifier";
    private String userIdentifier = "localExecutionUnsetUserID";
    private String sessionIdentifier = "localExecutionSessionID";

    private LocalClient owner;
}
