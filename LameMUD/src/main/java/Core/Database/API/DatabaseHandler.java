package Core.Database.API;

public class DatabaseHandler {

    public static DatabaseAPI Get()
    {
        if(handler == null)
        {
            throw new NullPointerException();
        }
        if(!alreadyUsed)
        {
            alreadyUsed = true;
        }
        return handler;
    }

    static
    {
        handler = null;
    }

    public static boolean SetDatabaseImpl(DatabaseAPI implementation)
    {
        if(alreadyUsed)
        {
            return false;
        }
        handler = implementation;
        return true;
    }

    private static DatabaseAPI handler;
    private static boolean alreadyUsed;


}
