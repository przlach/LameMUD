package Core.Game.Instance;

import Core.Database.API.DatabaseGameInstances;
import Core.Database.API.DatabaseHandler;
import Core.Database.Impls.SQL.MySQLDatabase;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class InstanceBuilderTest {


    @Before
    public void setUp() {

        database = mock(DatabaseGameInstances.class);
        DatabaseHandler.SetDatabaseImpl(new MySQLDatabaseInstanceUnitTestStub(database));

    }

    @Test
    public void testCreateInstance() {

        InstanceBuilder builder = new InstanceBuilder();
        String newInstanceName = "game1";

        when(DatabaseHandler.Get().instances().getInstance(newInstanceName)).thenReturn(null);

        Instance instance = builder.create(newInstanceName);
        assertNotNull("Builder method returned null instead of instance.",instance);

    }

    @Test
    public void testCreateInstanceWithNameTaken() {

        InstanceBuilder builder = new InstanceBuilder();
        String newInstanceName = "game1";

        Instance alreadyExistingInstance = new Instance(newInstanceName);

        when(DatabaseHandler.Get().instances().getInstance(anyString())).thenReturn(alreadyExistingInstance);

        Instance instance = builder.create(newInstanceName);
        assertNull("Builder method returned something but it should be null.",instance);

        assertEquals("Wrong error returned", InstanceBuilderError.Create_NameTaken, builder.getLastError());

    }

    @Test
    public void testCreateInstanceWithPassword() {

        InstanceBuilder builder = new InstanceBuilder();
        String newInstanceName = "game1";
        String correctPassword = "CorrectPassword";

        when(DatabaseHandler.Get().instances().getInstance(newInstanceName)).thenReturn(null);

        Instance instance = builder.create(newInstanceName,correctPassword);
        assertNotNull("Builder method returned null instead of instance.",instance);

    }

    @Test
    public void testCreateInstanceWithIllegalPassword() {

        InstanceBuilder builder = new InstanceBuilder();
        String newInstanceName = "game1";
        String wrongPassword = "pas";

        when(DatabaseHandler.Get().instances().getInstance(newInstanceName)).thenReturn(null);

        Instance instance = builder.create(newInstanceName,wrongPassword);
        assertNull("Builder method returned instance even if password was illegal.",instance);

        assertEquals("Wrong error returned", InstanceBuilderError.Create_IllegalPassword, builder.getLastError());

    }

    DatabaseGameInstances database;

}