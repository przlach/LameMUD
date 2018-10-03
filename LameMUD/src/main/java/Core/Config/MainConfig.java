package Core.Config;

import Core.CommandLine.Commands.AccountManagment.AccountManagmentCommands;
import Core.CommandLine.Commands.Chatrooms.ChatroomsCommands;
import Core.CommandLine.Commands.Commands;
import Core.CommandLine.Commands.Other.Help;
import Core.CommandLine.Commands.Other.SetWelcomeMessage;
import Core.Database.API.DatabaseHandler;
import Core.Database.Impls.SQL.MySQLDatabase;

public class MainConfig {

    public static void SetConfig()
    {
        if(!isConfigSet)
        {
            SetConfigInternal();
            isConfigSet = true;
        }
    }

    private static void SetConfigInternal()
    {
        CheckIfJDBCDriverIsPresent();
        SetDatabase();
        SetAvaibleCommands();
    }

    private static void CheckIfJDBCDriverIsPresent()
    {
        try {
            Class.forName("com.mysql.jdbc.Driver");

        } catch(java.lang.ClassNotFoundException e) {
            System.out.println("ClassNotFoundException: ");
            System.out.println(e.getMessage());
        }
    }

    private static void SetDatabase()
    {
        DatabaseHandler.SetDatabaseImpl(new MySQLDatabase());
    }

    private static void SetAvaibleCommands()
    {
        Commands.AddAvaibleCommands(AccountManagmentCommands.Get());
        Commands.AddAvaibleCommands(ChatroomsCommands.Get());
        Commands.AddAvaibleCommand(new Help());
        Commands.AddAvaibleCommand(new SetWelcomeMessage());
    }

    private static boolean isConfigSet = false;
    private static String onSuccessfullConnectionMessage = "Hi! type \"/help\" to get command list, or just anything to let us guide you on how to start chatting!";

    public static void SetOnSuccessfullConnectionMessage(String newMessage)
    {
        onSuccessfullConnectionMessage = newMessage;
    }

    public static String GetOnSuccessfullConnectionMessage()
    {
        return onSuccessfullConnectionMessage;
    }

}
