package Core.Database.Impls.SQL.Param;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLStringParam implements SQLParam {

    public SQLStringParam(String paramName, String val)
    {
        this.val = val;
        this.paramName = paramName;
    }

    @Override
    public Object ExtractParamFromSelectedRow(ResultSet uprs) throws SQLException {
        return uprs.getString("paramVal");
    }

    @Override
    public String GetParamName() {
        return paramName;
    }

    @Override
    public void PrepareRowToBeInserted(ResultSet uprs) throws SQLException {
        uprs.updateString("paramName", paramName);
        uprs.updateString("paramVal",val);
    }

    @Override
    public String GetSQLTableNameSubstring() {
        return "String";
    }

    private String paramName;
    private String val;

}
