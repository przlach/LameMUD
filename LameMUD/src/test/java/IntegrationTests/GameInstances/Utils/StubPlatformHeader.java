package IntegrationTests.GameInstances.Utils;

import Core.CommandLine.Platforms.PlatformMessageHeader;

public class StubPlatformHeader implements PlatformMessageHeader {

    public StubPlatformHeader(StubUserConsole platform)
    {
        owner = platform;
    }

    @Override
    public void SendMessage(String msgReply) {
        owner.ReceiveReply(msgReply);
    }

    @Override
    public void SendAuthToken(String token) {

    }

    @Override
    public boolean AllowTrustedLogin() {
        return true;
    }

    @Override
    public boolean AllowsMultipleSessionsFromSamePlatform() {
        return true;
    }

    @Override
    public String GetPlatformIdentifier() {
        return platformIdentifier;
    }

    @Override
    public String GetUserIdentifier() {
        return userIdentifier;
    }

    @Override
    public String GetSessionIdentifier() {
        return sessionIdentifier;
    }

    public void setUserIdentifier(String userIdentifier) {
        this.userIdentifier = userIdentifier;
    }

    public void setSessionIdentifier(String sessionIdentifier) {
        this.sessionIdentifier = sessionIdentifier;
    }

    private final String platformIdentifier = "stubPlatformIdentifier";
    private String userIdentifier = "stubUnsetUserID";
    private String sessionIdentifier = "stubSessionID";

    public StubUserConsole owner;
}
