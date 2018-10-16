package Core.Config;

import Core.Database.Impls.SQL.Connection.SQLServerParameters;

public class LocalTestServerParameters extends SQLServerParameters {

    @Override
    public void SetParameters() {
        host = "localhost";
        databaseName = "test";
        userName = "test";
        userPassword = "test";
    }
}
