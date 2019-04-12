package IntegrationTests.GameInstances.Utils;

import Core.CommandLine.MainInterpreter;
import Core.CommandLine.Platforms.PlatformMessage;
import Core.Database.API.DatabaseHandler;
import Core.Sessions.ActiveUsers;

import static org.junit.Assert.assertTrue;

public class StubUserConsole {

    private static int globalID = 0;

    private StubPlatformHeader header;
    private final String username;
    private final String password;

    public StubUserConsole()
    {
        header = new StubPlatformHeader(this);
        header.setUserIdentifier("stubUserConsole" + globalID++);
        username = "stubUsername" + globalID;
        password = "veryGoodPassword";
    }

    public void SendMessage(String message)
    {
        PlatformMessage platformMessage = new PlatformMessage();
        platformMessage.SetHeader(header);
        platformMessage.SetMessage(message);
        MainInterpreter.HandleMessage(platformMessage);
    }

    public void ReceiveReply(String reply)
    {

    }

    public void RegisterAndLogin()
    {
        DatabaseHandler.Get().RemoveUser(username);
        SendMessage("/register " + username + " " + password);
        SendMessage("/login " + username + " " + password);
        assertTrue(ActiveUsers.IsHeaderActive(header));
    }

}
