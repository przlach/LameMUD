package Core.CommandLine.User;

public class DefaultUserBuilderInternal implements UserBuilderInternal {
    @Override
    public User Build() {
        return null;
    }

    @Override
    public User Build(int id) {
        return null;
    }

    @Override
    public User ObsoleteBuild(int id, String username) {
        return new User(id, username);
    }
}
