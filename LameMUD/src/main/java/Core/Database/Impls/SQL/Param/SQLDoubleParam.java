package Core.Database.Impls.SQL.Param;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLDoubleParam implements SQLParam {

    public SQLDoubleParam(String paramName, double val)
    {
        this.val = val;
        this.paramName = paramName;
    }

    @Override
    public Object ExtractParamFromSelectedRow(ResultSet uprs) throws SQLException {
        return uprs.getDouble("paramVal");
    }

    @Override
    public String GetParamName() {
        return paramName;
    }

    @Override
    public void PrepareRowToBeInserted(ResultSet uprs) throws SQLException {
        uprs.updateString("paramName", paramName);
        uprs.updateDouble("paramVal",val);
    }

    @Override
    public String GetSQLTableNameSubstring() {
        return "Double";
    }

    private String paramName;
    private double val;
}
