package Core.CommandLine.Commands;

import Core.CommandLine.VerifiedMessage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Command {

    public Command()
    {
        GetPatterns();
        GetBaseString();
        if(noFunctionWasSetCounter==2) {
            ThrowExceptionBecauseNoneOfTwoFunctionsWasOverrided();
        }

    }
    private void ThrowExceptionBecauseNoneOfTwoFunctionsWasOverrided()     // this function is part of so shitty idea, that I am proud of it
    {
        try {
            throw new Exception("In command subclasses, GetPatterns() or GetBaseString() need to be overloaded (at least one)");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    protected String[] GetPatterns()
    {
        noFunctionWasSetCounter++;
        return null;
    }
    protected String GetBaseString()
    {
        noFunctionWasSetCounter++;
        return null;
    }
    public Matcher Match(String candidate)
    {
        for(String stringPattern: GetPatterns())
        {
            Pattern pattern = Pattern.compile(stringPattern);
            Matcher matcher = pattern.matcher(candidate);
            if(matcher.matches())
            {
                return matcher;
            }
        }
        return null;
    }
    public void ExecuteCommandWithLoginCheck(VerifiedMessage caller)
    {
        boolean isUserLoggedIn = caller.IsUserLoggedIn();
        if(isUserLoggedIn)
        {
            if(!ExecutableWhenLoggedIn())
            {
                caller.Reply("This command can't be executed when you are logged in. (you can type \"/logout\" to do that).");
                return;
            }
        }
        else
        {
            if(!ExecutableWhenLoggedOut())
            {
                caller.Reply("This command can't be executed when you are logged out (to change that use \"/register [login] [password]\" and \"/login [login] [password]\").");
                return;
            }
        }
        ExecuteCommand(caller);
    }

    abstract public String GetShortDescription();

    public String GetFullDescription()
    {
        return "Description not yet done (WIP)";
    }

    public boolean IsCommandVisibleToEverybody()
    {
        return true;
    }

    abstract public void ExecuteCommand(VerifiedMessage caller);

    protected boolean ExecutableWhenLoggedIn()
    {
        return true;
    }
    protected boolean ExecutableWhenLoggedOut()
    {
        return false;
    }

    @Override
    public boolean equals(Object o) {
        return this.getClass().equals(o.getClass());
    }

    private boolean noFunctionWasSet = false;
    private int noFunctionWasSetCounter = 0;

}
