package Core.Database.Impls.SQL;

import Core.Database.API.Params.Param;
import Core.Database.Impls.SQL.Connection.SQLSelectedServer;
import Core.Database.Impls.SQL.MySQLCommon;
import Core.Database.Impls.SQL.Param.SQLParam;
import Core.Log.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class MySQLGenericDatabase {

    public boolean AddSQLObject(List<SQLParam> newObjectParams, String tableName) {

        try {

            Connection con = SQLSelectedServer.getConnection();
            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet uprs = stmt.executeQuery("SELECT * FROM " + tableName + ";");

            uprs.moveToInsertRow();

            for (SQLParam param : newObjectParams) {
                param.InsertIntoResultSet(uprs);
            }

            uprs.insertRow();

            uprs.close();
            stmt.close();
            con.close();

            return true;


        } catch (SQLException e) {
            Logger.Log(e.getMessage(),
                    "error", "SQL");
            return false;
        }

    }

    public int GetSQLObject(List<SQLParam> filteringParams, List<SQLParam> resultHoldingParams, String tableName) {

        try {

            Connection con = SQLSelectedServer.getConnection();
            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            String sqlQuery = "SELECT";
            for(SQLParam param : resultHoldingParams)
            {
                sqlQuery += " " + param.GetParamName() + ",";
            }
            sqlQuery.substring(0,sqlQuery.length() - 1);
            sqlQuery += " FROM " + tableName + " WHERE";
            for(SQLParam param : filteringParams)
            {
                sqlQuery += " " + param.GetQueryWhereString() + " AND";
            }
            sqlQuery.substring(0,sqlQuery.length() - 3);
            sqlQuery += ";";
            ResultSet uprs = stmt.executeQuery(sqlQuery);

            if(!uprs.first())
            {
                return 0;
            }

            for (SQLParam param : resultHoldingParams) {
                param.CheckAndGetFromResultSet(uprs);
            }

            //TODO should there be done more with return value below, is it even needed?

            uprs.last();
            int resultCount = uprs.getRow();

            uprs.close();
            stmt.close();
            con.close();

            return resultCount;


        } catch (SQLException e) {
            Logger.Log(e.getMessage(),
                    "error", "SQL");
            return -1;
        }
    }

    public int CountSQLObject(List<SQLParam> filteringParams, String tableName)
    {
        try {

            Connection con = SQLSelectedServer.getConnection();
            Statement stmt = con.createStatement();
            String sqlQuery = "SELECT COUNT(*)";
            sqlQuery += " FROM " + tableName + " WHERE";
            for(SQLParam param : filteringParams)
            {
                sqlQuery += " " + param.GetQueryWhereString() + " AND";
            }
            sqlQuery.substring(0,sqlQuery.length() - 3);
            sqlQuery += ";";
            ResultSet uprs = stmt.executeQuery(sqlQuery);

            uprs.close();
            stmt.close();
            con.close();

            return uprs.getInt(1);;

        } catch (SQLException e) {
            Logger.Log(e.getMessage(),
                    "error","SQL");
            return -1;
        }
    }

    public boolean IsSQLObject(List<SQLParam> filteringParams, String tableName)
    {
        return (CountSQLObject(filteringParams, tableName) >= 1);
    }
}
