package IntegrationTests.GameInstances;

import Core.CommandLine.Commands.Chatrooms.ListChatrooms;
import Core.CommandLine.Commands.Instances.CreateInstance;
import Core.CommandLine.Commands.Instances.ListInstances;
import Core.CommandLine.VerifiedMessage;
import Core.Config.LocalTestServerParameters;
import Core.Config.MainConfig;
import Core.Database.API.DatabaseAPI;
import Core.Database.API.DatabaseHandler;
import Core.Database.Impls.SQL.Connection.SQLSelectedServer;
import Core.Database.Impls.SQL.Connection.SQLServersCollection;
import Core.Database.Impls.SQL.MySQLDatabase;
import Core.Game.Instance.Instance;
import Core.Game.Instance.Instances;
import Core.Game.Instance.InstancesAPI;
import Core.Game.Instance.InstancesAPIHandler;
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
        InstancesAPI realInstances = new Instances();
        spyInstances = Mockito.spy(realInstances);
        InstancesAPIHandler.set(spyInstances);

        //DatabaseAPI realDatabase = new MySQLDatabase();
        //spyDatabase = Mockito.spy(realDatabase);
        //DatabaseHandler.set(spyDatabase);

        //user1 = new StubUserConsole();
        //user1.RegisterAndLogin();
    }

    private void WipeAllInstances()
    {
        String sql = "DELETE FROM gameInstances";

        try {

            Connection conn = SQLSelectedServer.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        List<Instance> instances = InstancesAPIHandler.get().getAllInstances();
        assertEquals("There shouldn't be any instances", 0, instances.size());
    }

    private void AssertPrintInstancesOutput(String output, String[] expectedInstanceNames)
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

    private void AssertInstanceList(String[] expectedInstanceNames)
    {
        ArgumentCaptor<String> commandReplyCaptor = ArgumentCaptor.forClass(String.class);
        VerifiedMessage userCommand = mock(VerifiedMessage.class);
        when(userCommand.GetMessage()).thenReturn("/instance list");
        when(userCommand.IsUserLoggedIn()).thenReturn(true);

        ListInstances listInstancesCommand = new ListInstances();

        listInstancesCommand.ExecuteCommandWithLoginCheck(userCommand);
        verify(userCommand).Reply(commandReplyCaptor.capture());
        verify(spyInstances).getInstancesNames();

        String printInstancesOutput = commandReplyCaptor.getValue();

        AssertPrintInstancesOutput(printInstancesOutput, expectedInstanceNames);
    }

    @Test
    public void PrintInstancesWhenThereIsNone()
    {
        WipeAllInstances();

        String[] expectedInstances = { };

        AssertInstanceList(expectedInstances);
    }

    private String ExecuteCreateInstanceCommand(String createCommandArguments)
    {
        ArgumentCaptor<String> commandReplyCaptor = ArgumentCaptor.forClass(String.class);
        VerifiedMessage userCommand = mock(VerifiedMessage.class);
        when(userCommand.GetMessage()).thenReturn("/instance create " + createCommandArguments);
        when(userCommand.IsUserLoggedIn()).thenReturn(true);

        CreateInstance createInstanceCommand = new CreateInstance();
        createInstanceCommand.ExecuteCommandWithLoginCheck(userCommand);
        verify(userCommand).Reply(commandReplyCaptor.capture());
        String createInstanceOutput = commandReplyCaptor.getValue();

        return createInstanceOutput;
    }

    @Test
    public void CreateFirstInstanceWithoutPassword()
    {
        WipeAllInstances();

        String instanceName = "game1";

        String createInstanceOutput = ExecuteCreateInstanceCommand(instanceName);
        assertEquals("Instance " + instanceName + " created successfully.", createInstanceOutput);
        verify(spyInstances).createInstance(instanceName);
        verify(spyInstances).getInstance(instanceName);
        //verify(spyDatabase).instances().createInstance(instanceName);

        String[] expectedInstances = { instanceName };

        AssertInstanceList(expectedInstances);
    }

    @Test
    public void CreateSecondInstanceWithPassword()
    {
        WipeAllInstances();

        String prevInstanceName = "game1";
        String instanceName = "game2";
        String instancePassword = "password1";

        String createInstanceOutput = ExecuteCreateInstanceCommand(prevInstanceName);
        assertEquals("Instance " + prevInstanceName + " created successfully.", createInstanceOutput);
        verify(spyInstances).createInstance(prevInstanceName);
        verify(spyInstances).getInstance(prevInstanceName);
        //verify(spyDatabase).instances().getInstance(prevInstanceName);
        //verify(spyDatabase).instances().createInstance(prevInstanceName);

        String createInstance2Output = ExecuteCreateInstanceCommand(instanceName + " " + instancePassword);
        assertEquals("Instance " + instanceName + " created successfully.", createInstance2Output);
        verify(spyInstances).createInstance(instanceName, instancePassword);
        verify(spyInstances).getInstance(instanceName);
        //verify(spyDatabase).instances().getInstance(instanceName);
        //verify(spyDatabase).instances().createInstance(instanceName, instancePassword);

        String[] expectedInstances = { prevInstanceName, instanceName };

        AssertInstanceList(expectedInstances);
    }

    @Test
    public void CreateInstanceWhenNameIsTaken()
    {
        WipeAllInstances();

        String instanceName = "game1";

        String createInstanceOutput = ExecuteCreateInstanceCommand(instanceName);
        assertEquals("Instance " + instanceName + " created successfully.", createInstanceOutput);
        verify(spyInstances).createInstance(instanceName);
        verify(spyInstances).getInstance(instanceName);
        //verify(spyDatabase).instances().createInstance(instanceName);

        String createSameInstanceAgainOutput = ExecuteCreateInstanceCommand(instanceName);
        assertEquals("Couldn't create instance " + instanceName + ". The name is taken.", createSameInstanceAgainOutput);
        verify(spyInstances).getInstance(instanceName);
        //verify(spyDatabase).instances().getInstance(instanceName);

        String[] expectedInstances = { instanceName };

        AssertInstanceList(expectedInstances);
    }

    @Test
    public void CreateInstanceWithInproperPassword()
    {
        WipeAllInstances();

        String instanceName = "game3";
        String instancePassword = "pw";

        String createInstanceOutput = ExecuteCreateInstanceCommand(instanceName + " " + instancePassword);
        assertEquals("Couldn't create instance " + instanceName + ". The password doesn't match requirements.", createInstanceOutput);
        verify(spyInstances).createInstance(instanceName, instancePassword);
        verify(spyInstances).getInstance(instanceName);
        //verify(spyInstances).verifyPassword
        // TODO how to verify incorrect password? collect return of Instances::verifyPasswordCorrectness ?

        String[] expectedInstances = { };

        AssertInstanceList(expectedInstances);
    }

    //private DatabaseAPI spyDatabase;
    InstancesAPI spyInstances;

}
