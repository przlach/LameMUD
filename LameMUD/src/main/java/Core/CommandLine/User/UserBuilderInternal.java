package Core.CommandLine.User;

public interface UserBuilderInternal {

    public User Build();
    public User Build(int id);
    public User ObsoleteBuild(int id, String username);

}
