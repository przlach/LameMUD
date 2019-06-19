package Core.Game.Instance;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface InstancesRepository extends CrudRepository<Instance, Integer> {

    List<Instance> findByNameeee(String name);

}
