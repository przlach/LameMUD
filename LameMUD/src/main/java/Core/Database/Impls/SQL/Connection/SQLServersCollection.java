package Core.Database.Impls.SQL.Connection;

import java.util.ArrayList;

public class SQLServersCollection {

    private static ArrayList<SQLServerParameters> serverList;

    public static boolean addServer(SQLServerParameters newServerParameters)
    {
        if(serverList == null)
        {
            serverList = new ArrayList<SQLServerParameters>();
        }

        if(serverList.contains(newServerParameters))
        {
            return false;
        }

        serverList.add(newServerParameters);
        return true;
    }

    public static SQLServerParameters[] getServers()
    {
        if(serverList == null)
        {
            return  null;
        }
        else
        {
            return serverList.toArray(new SQLServerParameters[serverList.size()]);
        }
    }

}
