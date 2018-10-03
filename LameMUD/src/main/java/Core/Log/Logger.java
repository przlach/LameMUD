package Core.Log;

import java.util.ArrayList;
import java.util.Iterator;

public class Logger {

    public static void Log(String message,String... tags)
    {

        String wholeString = " [";
        for (String s :tags)
        {
            wholeString+=s+", ";
        }
        if(tags.length>0)
        {
            wholeString = wholeString.substring(0,wholeString.length()-2);
        }
        wholeString += "]"+ GetLineNumberInfo() + " " + message + "\n";
        loggedMsgs.add(wholeString);

        //ChatAnnotation.BroadcastMessage(wholeString);
    }

    private static String GetLineNumberInfo()
    {
        String output="{";
        output += Thread.currentThread().getStackTrace()[3].getClassName() + ":" +Thread.currentThread().getStackTrace()[3].getMethodName() + ":" + Thread.currentThread().getStackTrace()[3].getLineNumber() + "}";
        return output;
    }

    private static ArrayList<String> loggedMsgs = new ArrayList<String>();

    public static String LogToString()
    {
        String wholeOutput = "";
        Iterator<String> it = loggedMsgs.iterator();
        while (it.hasNext())
        {
            wholeOutput += it.next();
            if(it.hasNext())
            {
                wholeOutput += "\n";
            }
        }
        return wholeOutput;
    }

}
