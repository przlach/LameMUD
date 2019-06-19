package Core.Game.Instance;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Instance {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer ID;

    private String name;
    private String password;

    public Instance(String name, String password)
    {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }
}
