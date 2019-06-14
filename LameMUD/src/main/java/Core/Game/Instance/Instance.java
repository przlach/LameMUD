package Core.Game.Instance;

import javax.persistence.Entity;

@Entity
public class Instance {

    private String name;
    private String password;

    public Instance(String name)
    {
        this.name = name;
    }
}
