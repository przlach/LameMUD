package Core.CommandLine.User;

public class DefaultUserBuilderInternal implements UserBuilderInternal {
    
    @Override
    public User Build(int id, String username) {
        return new User(id, username);
    }
}
