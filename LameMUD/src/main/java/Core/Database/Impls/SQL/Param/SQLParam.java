package Core.Database.Impls.SQL.Param;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface SQLParam {
    
    Object ExtractParamFromSelectedRow(ResultSet uprs) throws SQLException;
    String GetParamName();
    void PrepareRowToBeInserted(ResultSet uprs) throws SQLException;
    String GetSQLTableNameSubstring();
}
