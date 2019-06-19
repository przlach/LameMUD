package Core.Game.Instance;

import java.util.List;

public interface InstancesAPI {

    public boolean createInstance(String name, String password);
    public boolean createInstance(String name);
    public List<Instance> getAllInstances();
    public Instance getInstance(String name);
    public List<String> getInstancesNames();

}
