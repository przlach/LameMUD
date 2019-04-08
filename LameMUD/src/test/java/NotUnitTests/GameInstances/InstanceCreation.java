package NotUnitTests.GameInstances;

import Core.CommandLine.MainInterpreter;
import Core.CommandLine.Platforms.PlatformMessage;
import Core.Config.LocalTestServerParameters;
import Core.Config.MainConfig;
import Core.Database.Impls.SQL.Connection.SQLSelectedServer;
import Core.Database.Impls.SQL.Connection.SQLServersCollection;
import Extensions.Platforms.LocalExecutionPlatform.GUI.LocalClient;
import Extensions.Platforms.LocalExecutionPlatformHeader;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InstanceCreation {

    @Before
    public void setUp()
    {
        MainConfig.SetConfig();
        SQLServersCollection.addServer(new LocalTestServerParameters());
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
    }

    public void AssertPrintInstancesOutput(String output, String[] expectedInstanceNames)
    {

    }

    public void ExecuteCommand(String command)
    {
        LocalClient owner = new LocalClient();
        LocalExecutionPlatformHeader header = new LocalExecutionPlatformHeader();

        PlatformMessage platformMessage = new PlatformMessage();
        platformMessage.SetMessage(command);
        platformMessage.SetHeader(LocalExecutionPlatformHeader);

        MainInterpreter.HandleMessage();
    }

    @Test
    public void PrintInstancesWhenThereIsNone()
    {
        WipeAllInstances();

        String printInstancesOutput = ExecuteCommand("/instance list");

        String[] expectedInstances = { };
        AssertPrintInstancesOutput(printInstancesOutput, expectedInstances);
    }

    @Test
    public void CreateFirstInstanceWithoutPassword()
    {
        WipeAllInstances();

        String instanceName = "game1";

        String createInstanceOutput = ExecuteCommand("/instance create " + instanceName);
        // expect createInstanceOutput

        String printInstancesOutput = PrintInstancesCommand();

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



}
