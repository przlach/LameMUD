package Core.Game.Instance;

import org.springframework.beans.factory.annotation.Autowired;

public class Instances implements InstancesAPI{

    public boolean createInstance(String name, String password)
    {
        instancesRepository.
        return false; // TODO
    }

    public boolean createInstance(String name)
    {
        return createInstance(name,"");
    }


    @Autowired
    private InstancesRepository instancesRepository;

}
