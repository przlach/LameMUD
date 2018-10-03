package Core.Database.Impls.SQL.Param;

import Core.CommandLine.GameLogic.Item;

public class SQLItemParam implements SQLParamOwner {

    SQLItemParam(Item owner)
    {
        this.owner = owner;
    }

    @Override
    public int GetOwnerID() {
        return owner.GetID();
    }

    @Override
    public String GetSQLColumnNameOfOwnerID() {
        return "itemId";
    }

    @Override
    public String GetSQLTableNameSubstring() {
        return "items";
    }

    private Item owner;
}
