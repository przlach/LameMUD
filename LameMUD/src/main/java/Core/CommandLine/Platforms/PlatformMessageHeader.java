package Core.CommandLine.Platforms;

public interface PlatformMessageHeader {

    public void SendMessage(String msgReply);    // not sure if that function should be here
    public void SendAuthToken(String token);      // not sure if that function should be here
    public boolean AllowTrustedLogin();
    public boolean AllowsMultipleSessionsFromSamePlatform();

    public String GetPlatformIdentifier();
    public String GetUserIdentifier();
    public String GetSessionIdentifier();

}
