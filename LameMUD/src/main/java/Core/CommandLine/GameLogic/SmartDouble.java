package Core.CommandLine.GameLogic;

import Core.Database.API.DatabaseAPI;
import Core.Database.API.DatabaseHandler;
import Core.Database.API.Params.DoubleParam;
import Core.Database.API.Params.ParamOwner;

import static Core.CommandLine.GameLogic.SmartParamStatus.actual;

public class SmartDouble {

    private DoubleParam content;
    private SmartParamStatus status;

    public SmartDouble(String paramName)
    {
        content = new DoubleParam(paramName,0);
        status = SmartParamStatus.valueNotSet;
    }

    public SmartDouble(String paramName, double paramVal)
    {
        content = new DoubleParam(paramName,paramVal);
        status = SmartParamStatus.notSendedToDatabase;
    }

    public void SetValue(double newVal)
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

    public double GetValue()
    {
        return content.getParamValue();
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
        double getResult = (Double)database.GetParam(owner,content);
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
