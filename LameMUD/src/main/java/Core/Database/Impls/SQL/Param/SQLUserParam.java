package Core.Database.Impls.SQL.Param;

import Core.CommandLine.User.User;

public class SQLUserParam implements SQLParamOwner {

    SQLUserParam(User owner)
    {
        this.owner = owner;
    }

    @Override
    public int GetOwnerID() {
        return owner.getId();
    }

    @Override
    public String GetSQLColumnNameOfOwnerID() {
        return "usrId";
    }

    @Override
    public String GetSQLTableNameSubstring() {
        return "usrs";
    }

    private User owner;
}
