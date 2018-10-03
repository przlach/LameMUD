package Core.Database.Impls.SQL.Connection;

import Core.Database.Impls.SQL.MySQLCommon;
import Core.Log.Logger;

import java.sql.Connection;
import java.sql.SQLException;

public class SQLSelectedServer {

    private static SQLServerParameters selectedParameters;

    public static Connection getConnection()
    {
        if(selectedParameters == null)
        {
            selectServer();

            if(selectedParameters == null)
            {
                return null;
            }
        }

        Connection conn = null;

        try
        {
            conn = SQLConnector.GetConnection(selectedParameters);
            if(conn == null)
            {
                throw new NullPointerException();
            }

        }
        catch (SQLException e)
        {
            e.printStackTrace();
            Logger.Log(e.toString());
        }
        catch (NullPointerException e)
        {
            e.printStackTrace();
            Logger.Log(e.toString());
        }
        return conn;

    }

    private static void selectServer()
    {
        SQLServerParameters[] serverCandidates = SQLServersCollection.getServers();
        for(SQLServerParameters candidate: serverCandidates)
        {
            if(SQLConnector.IsWorkingProperly(candidate))
            {
                selectedParameters = candidate;
                MySQLCommon.PrepareDatabase();
                return;
            }
        }
        return;
    }
}
