package Core.Database.Impls.SQL.Param;

public interface SQLParamOwner {

    int GetOwnerID();
    String GetSQLColumnNameOfOwnerID();
    String GetSQLTableNameSubstring();

}
