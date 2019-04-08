package Core.Database.API;

import Core.Game.Instance.Instance;

public interface DatabaseGameInstances {

    Instance getInstance(String instanceName);
    Integer getInt();

}
