package Core.Database.Impls.SQL.Connection;

abstract public class SQLServerParameters {

    public String GetUrl()
    {
        return "jdbc:mysql://" + host + ":3306/" + databaseName + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    }

    public String GetUserName()
    {
        return userName;
    }

    public String GetUserPassword()
    {
        return userPassword;
    }

    public SQLServerParameters()
    {
        SetParameters();
    }

    public abstract void SetParameters();

    protected String host;
    protected String databaseName;

    protected String userName;
    protected String userPassword;

}
