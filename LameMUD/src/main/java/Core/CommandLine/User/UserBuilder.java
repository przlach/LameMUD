package Core.CommandLine.User;

public class UserBuilder {

    public static User Build(int id, String username)
    {
        return selectedBuilder.Build(id,username);
    }
    public static void SetInternalBuilder(UserBuilderInternal internalBuilder)
    {
        selectedBuilder = internalBuilder;
    }

    private static UserBuilderInternal selectedBuilder;

    static
    {
        SetInternalBuilder(new DefaultUserBuilderInternal());
    }

}
