package Core.Game.Instance;

import Core.Database.API.DatabaseAPI;
import Core.Database.API.DatabaseGameInstances;
import Core.Database.API.DatabaseHandler;
/* TODO REMOVE
public class InstanceBuilder {

    public InstanceBuilder()
    {
        lastError = null;
    }

    public Instance create(String name)
    {
        return create(name,"");
    }

    public Instance create(String name, String password)
    {
        if(!VerifyPassword(password))
        {
            lastError = InstanceBuilderError.Create_IllegalPassword;
            return null;
        }

        Instance alreadyExistingInstance = DatabaseHandler.Get().instances().getInstance(name);
        if(alreadyExistingInstance != null)
        {
            lastError = InstanceBuilderError.Create_NameTaken;
            return null;
        }

        lastError = null;
        return new Instance(name);
    }

    public InstanceBuilderError getLastError()
    {
        return lastError;
    }

    private boolean VerifyPassword(String password)
    {
        if(password.equals(""))
        {
            return true;
        }
        if(password.length() < 6)
        {
            return false;
        }
        return true;
    }

    private InstanceBuilderError lastError;

}

*/