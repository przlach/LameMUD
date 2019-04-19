package IntegrationTests.GameInstances;

import Core.CommandLine.Commands.Chatrooms.ListChatrooms;
import Core.CommandLine.Commands.Instances.ListInstances;
import Core.CommandLine.VerifiedMessage;
import Core.Config.LocalTestServerParameters;
import Core.Config.MainConfig;
import Core.Database.API.DatabaseHandler;
import Core.Database.Impls.SQL.Connection.SQLSelectedServer;
import Core.Database.Impls.SQL.Connection.SQLServersCollection;
import Core.Game.Instance.Instance;
import IntegrationTests.GameInstances.Utils.StubUserConsole;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class InstanceCreation {

    @Before
    public void setUp()
    {
        //MainConfig.SetConfig();
        SQLServersCollection.addServer(new LocalTestServerParameters());

        //user1 = new StubUserConsole();
        //user1.RegisterAndLogin();
    }

    public void WipeAllInstances()
    {
        String sql = "DELETE FROM gameInstances";

        try {

            Connection conn = SQLSelectedServer.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        List<Instance> instances = DatabaseHandler.Get().instances().getAllInstances();
        assertEquals("There shouldn't be any instances", 0, instances.size());
    }

    public void AssertPrintInstancesOutput(String output, String[] expectedInstanceNames)
    {
        String[] outputLines = output.split("\\n");
        assertEquals("Wrong number of instances", expectedInstanceNames.length, outputLines.length - 1);

        if(expectedInstanceNames.length == 0)
        {
            assertEquals("Wrong reply of instance list when there are no intances",
                    "There are no instances.",
                    output);
            return;
        }

        for(int i=1; i < outputLines.length; ++i)
        {
            assertEquals("Mismatching instance names", expectedInstanceNames[i-1], outputLines[i]);
        }
    }

    @Test
    public void PrintInstancesWhenThereIsNone()
    {
        WipeAllInstances();

        ArgumentCaptor<String> commandReplyCaptor = ArgumentCaptor.forClass(String.class);
        VerifiedMessage userCommand = mock(VerifiedMessage.class);
        when(userCommand.GetMessage()).thenReturn("/instance list");
        when(userCommand.IsUserLoggedIn()).thenReturn(true);

        ListInstances executedCommand = new ListInstances();

        executedCommand.ExecuteCommandWithLoginCheck(userCommand);
        verify(userCommand).Reply(commandReplyCaptor.capture());
        // verify command used Database.getAllInstances(); or something

        String printInstancesOutput = commandReplyCaptor.getValue();

        String[] expectedInstances = { };
        AssertPrintInstancesOutput(printInstancesOutput, expectedInstances);
    }

    @Test
    public void CreateFirstInstanceWithoutPassword()
    {
        WipeAllInstances();
        //StubUserConsole user1Spy = Mockito.spy(user1);

        String instanceName = "game1";

        ArgumentCaptor<String> commandReplyCaptor = ArgumentCaptor.forClass(String.class);
        VerifiedMessage userCommand = mock(VerifiedMessage.class);
        when(userCommand.GetMessage()).thenReturn("/instance create " + instanceName);
        when(userCommand.IsUserLoggedIn()).thenReturn(true);

        CreateInstance createInstanceCommand = new CreateInstance();
        createInstanceCommand.ExecuteCommandWithLoginCheck(userCommand);


        String createInstanceOutput = commandReplyCaptor.getValue();
        // expect createInstanceOutput

        //user1.SendMessage("/instance list");
        //Mockito.verify(user1Spy).ReceiveReply(captor.capture());

        ListInstances executedCommand2 = new ListInstances();
        executedCommand2.ExecuteCommandWithLoginCheck(userCommand);

        String printInstancesOutput = commandReplyCaptor.getValue();

        String[] expectedInstances = { instanceName };
        AssertPrintInstancesOutput(printInstancesOutput, expectedInstances);
    }

    @Test
    public void CreateSecondInstanceWithPassword()
    {

    }

    @Test
    public void CreateInstanceWhenNameIsTaken()
    {

    }

    @Test
    public void CreateInstanceWithInproperPassword()
    {

    }

    @Test
    public void CreateInstanceWithNameTakenAndInproperPassword()
    {

    }

    //private StubUserConsole user1;

}
