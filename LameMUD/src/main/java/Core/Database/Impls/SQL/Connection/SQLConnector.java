package Core.Database.Impls.SQL.Connection;

import Core.Log.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

class SQLConnector {

    static Connection GetConnection(SQLServerParameters serverParams) throws SQLException
    {
        Logger.Log("GetConnection(): DM arguments: "+serverParams.GetUrl()+", "
                                                             +serverParams.GetUserName()+", "
                                                             +serverParams.GetUserPassword());

        return DriverManager.getConnection(serverParams.GetUrl(),
                                           serverParams.GetUserName(),
                                           serverParams.GetUserPassword());
        // Shouldn't this function be protected from returning not working connection?
    }

    static boolean IsWorkingProperly(SQLServerParameters serverParams)
    {
        try {
            Connection conn = GetConnection(serverParams);
            Logger.Log(serverParams.GetUrl()+" IsWorking():created");

            conn.close();
            Logger.Log(serverParams.GetUrl()+" IsWorking():closed");

            return true;

        } catch (SQLException e) {
            Logger.Log(serverParams.GetUrl()+" IsWorking():error");
            return false;
        }
    }

}
