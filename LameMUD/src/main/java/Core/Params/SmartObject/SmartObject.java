package Core.Params.SmartObject;

import Core.Database.API.DatabaseAPI;
import Core.Database.API.DatabaseHandler;
import Core.Database.API.Params.SmartObjectParam;
import Core.Params.SmartDouble;
import Core.Params.SmartParamUpdater;
import Core.Params.SmartParamUpdaterInput;
import Core.Params.SmartString;

import java.util.ArrayList;

public abstract class SmartObject {

    public SmartObject()
    {
        DatabaseAPI database = DatabaseHandler.Get();
        this.id = database.AddSmartObject(getSmartObjectClassString());
        smartParams = new SmartParamUpdaterInput(new ArrayList<SmartDouble>(),
                                                 new ArrayList<SmartString>(),
                                                 new SmartObjectParam(this));
        RegisterSmartParameters();
    }

    public SmartObject(int id)
    {
        this.id = id;
        if(id < 0)  // allows to create not properly functioning SmartObject, currently used if you need only to use getSmartObjectClassString() of any subclass.
        {
            return;
        }
        smartParams = new SmartParamUpdaterInput(new ArrayList<SmartDouble>(),
                                                 new ArrayList<SmartString>(),
                                                 new SmartObjectParam(this));
        RegisterSmartParameters();
        GetParametersValuesFromDatabase();
    }

    abstract public String getSmartObjectClassString();

    abstract protected void RegisterSmartParameters();

    // duno if it will be even used, let it stay for now
    public static SmartObject GetSmartObjectFromDatabase(int id)
    {
        DatabaseAPI database = DatabaseHandler.Get();

        String smartObjectClassString = database.GetSmartObjectClassString(id);
        if(smartObjectClassString == null)
        {
            return null;
        }
        SmartObject gettedSmartObject = SmartObjectClassStringToSmartObjectConverter.convert(smartObjectClassString,id);
        if(gettedSmartObject == null)
        {
            return null;
        }
        return gettedSmartObject;
    }

    public void GetParametersValuesFromDatabase()
    {
        SmartParamUpdater.GetParametersValuesFromDatabase(smartParams);
    }

    public void SetParametersValueInDatabase()
    {
        SmartParamUpdater.SetParametersValueInDatabase(smartParams);
    }

    public void RemoveParametersFromDatabase()
    {
        SmartParamUpdater.RemoveParametersFromDatabase(smartParams);
    }

    protected boolean trackSmartParameter(SmartDouble param)
    {
        return smartParams.trackSmartParameter(param);
    }

    private boolean containsSameSmartParam(SmartDouble param)
    {
        return smartParams.containsSameSmartParam(param);
    }

    protected boolean trackSmartParameter(SmartString param)
    {
        return smartParams.trackSmartParameter(param);
    }

    private boolean containsSameSmartParam(SmartString param)
    {
        return smartParams.containsSameSmartParam(param);
    }

    public boolean Remove()
    {
        PreRemove();

        RemoveParametersFromDatabase();
        smartParams = null;
        DatabaseHandler.Get().RemoveSmartObject(this);

        AfterRemove();
        return true;
    }

    protected void PreRemove()
    {

    }

    protected void AfterRemove()
    {

    }

    public int GetID()
    {
        return id;
    }

    protected final int id;
    private SmartParamUpdaterInput smartParams;

}
