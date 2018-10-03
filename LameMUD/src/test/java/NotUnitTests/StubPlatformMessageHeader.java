package NotUnitTests;

import Core.CommandLine.Platforms.PlatformMessageHeader;

public class StubPlatformMessageHeader implements PlatformMessageHeader {

    StubPlatformMessageHeader()
    {
        trustedLogin = true;
        userIdentifier = "testID";
    }

    @Override
    public void SendMessage(String msgReply) {

    }

    @Override
    public void SendAuthToken(String token) {

    }

    @Override
    public boolean AllowTrustedLogin() {
        return trustedLogin;
    }

    @Override
    public boolean AllowsMultipleSessionsFromSamePlatform() {
        return false;
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
        return null;
    }

    public void SetUserIdentifier(String id)
    {
        userIdentifier = id;
    }

    public void setTrustedLogin(boolean trustedLogin) {
        this.trustedLogin = trustedLogin;
    }

    private final String platformIdentifier = "Stub";
    private String userIdentifier;
    private boolean trustedLogin;
}
