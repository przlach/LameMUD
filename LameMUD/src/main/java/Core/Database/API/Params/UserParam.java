package Core.Database.API.Params;

import Core.CommandLine.User.User;

public class UserParam implements ParamOwner {

    public UserParam(User owner)
    {
        this.owner = owner;
    }

    public User GetUser()
    {
        return owner;
    }

    private User owner;
}
