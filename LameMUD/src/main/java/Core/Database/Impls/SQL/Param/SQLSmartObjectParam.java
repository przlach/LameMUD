package Core.Database.Impls.SQL.Param;

import Core.Params.SmartObject.SmartObject;

public class SQLSmartObjectParam implements SQLParamOwner {

    SQLSmartObjectParam(SmartObject owner)
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

    private SmartObject owner;
}
