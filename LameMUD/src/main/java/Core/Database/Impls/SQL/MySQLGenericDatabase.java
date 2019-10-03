package Core.Database.Impls.SQL;

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
            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE , ResultSet.CONCUR_UPDATABLE);
            ResultSet uprs = stmt.executeQuery("SELECT * FROM "+ tableName +";");

            uprs.moveToInsertRow();

            for(SQLParam param : newObjectParams)
            {
                param.InsertIntoResultSet(uprs);
            }

            uprs.insertRow();

            uprs.close();
            stmt.close();
            con.close();

            return true;


        } catch (SQLException e) {
            Logger.Log(e.getMessage(),
                    "error","SQL");
            return false;
        }

    }

}
