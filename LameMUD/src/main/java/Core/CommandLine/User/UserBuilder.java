package Core.CommandLine.User;

public class UserBuilder {

    public static User Build()
    {
        return selectedBuilder.Build();
    }
    public static User Build(int id)
    {
        return selectedBuilder.Build(id);
    }
    public static User ObsoleteBuild(int id, String username)
    {
        return selectedBuilder.ObsoleteBuild(id,username);
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
