package Core.CommandLine.GameLogic;

import Core.Database.API.DatabaseAPI;
import Core.Database.API.DatabaseHandler;
import Core.Database.API.Params.ParamOwner;
import Core.Database.API.Params.StringParam;

import static Core.CommandLine.GameLogic.SmartParamStatus.actual;

public class SmartString {

    private StringParam content;
    private SmartParamStatus status;

    public SmartString(String paramName)
    {
        content = new StringParam(paramName,"");
        status = SmartParamStatus.valueNotSet;
    }

    public SmartString(String paramName, String paramVal)
    {
        content = new StringParam(paramName,paramVal);
        status = SmartParamStatus.notSendedToDatabase;
    }

    public void SetValue(String newVal)
    {
        switch (status)
        {
            case valueNotSet:
                status = SmartParamStatus.notSendedToDatabase;
                break;
            case actual:
                if(newVal != content.getParamValue())
                {
                    status = SmartParamStatus.outdated;
                }

        }
        content.setParamValue(newVal);
    }

    public void SetValue(boolean newVal)
    {
        String newStringVal;
        if(newVal)
        {
            newStringVal = "true";
        }
        else
        {
            newStringVal = "false";
        }
        SetValue(newStringVal);
    }

    public String GetValue()
    {
        return content.getParamValue();
    }

    public boolean GetBoolean() {
        return content.getParamValue().equals("true");
    }

    public boolean SetValueInDatabase(ParamOwner owner)
    {
        switch (status)
        {
            case valueNotSet:
                return false;
            case actual:
                return false;
        }
        DatabaseAPI database = DatabaseHandler.Get();
        boolean setResult = database.SetParam(owner,content);
        status = actual;
        return setResult;
    }
    public boolean GetValueFromDatabase(ParamOwner owner)
    {
        switch (status)
        {
            case actual:
                return false;
        }
        DatabaseAPI database = DatabaseHandler.Get();
        String getResult = (String)database.GetParam(owner,content);
        content.setParamValue(getResult);
        status = actual;
        return true;
    }
    public boolean RemoveValueFromDatabase(ParamOwner owner)
    {
        switch (status)
        {
            case valueNotSet:
            case notSendedToDatabase:
            case outdated:
            case actual:
        }
        DatabaseAPI database = DatabaseHandler.Get();
        boolean removeResult = database.RemoveParam(owner,content);
        return removeResult;
    }
    public String GetParamName()
    {
        return content.getParamName();
    }

}
