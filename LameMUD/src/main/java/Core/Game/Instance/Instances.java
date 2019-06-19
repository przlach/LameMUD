package Core.Game.Instance;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class Instances implements InstancesAPI{

    public boolean createInstance(String name, String password)
    {
        List<Instance> list = instancesRepository.findByNameeee(name);
        if(list.size() != 0)
        {
            return false;
        }
        Instance newInstance = new Instance(name, password);
        instancesRepository.save(newInstance);
        return true;
    }

    public boolean createInstance(String name)
    {
        return createInstance(name,"");
    }

    @Override
    public List<Instance> getAllInstances() {
        List<Instance> allInstances = new ArrayList<Instance>();
        for(Instance inst : instancesRepository.findAll())
        {
            allInstances.add(inst);
        }
        return allInstances;
    }

    @Override
    public Instance getInstance(String name) {
        List<Instance> foundInstance = instancesRepository.findByNameeee(name);
        if(foundInstance.size() == 0)
        {
            return null;
        }
        else
        {
            return  foundInstance.get(0);
        }
    }

    @Override
    public List<String> getInstancesNames() {
        List<String> instanceNames = new ArrayList<String>();
        for(Instance inst : instancesRepository.findAll())
        {
            instanceNames.add(inst.getName());
        }
        return instanceNames;
    }

    @Autowired
    public InstancesRepository instancesRepository;

}
