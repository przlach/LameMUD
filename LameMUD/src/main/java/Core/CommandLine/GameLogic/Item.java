package Core.CommandLine.GameLogic;

import Core.Database.API.DatabaseAPI;
import Core.Database.API.DatabaseHandler;
import Core.Database.API.Params.ItemParam;
import Core.Params.SmartDouble;
import Core.Params.SmartParamUpdater;
import Core.Params.SmartParamUpdaterInput;
import Core.Params.SmartString;

import java.util.ArrayList;

public abstract class Item {

    public Item()
    {
        DatabaseAPI database = DatabaseHandler.Get();
        this.id = database.AddItem(getItemClassString());
        smartParams = new SmartParamUpdaterInput(new ArrayList<SmartDouble>(),
                                                 new ArrayList<SmartString>(),
                                                 new ItemParam(this));
        RegisterSmartParameters();
    }

    public Item(int id)
    {
        this.id = id;
        if(id < 0)  // allows to create not properly functioning Item, currently used if you need only to use getItemClassString() of any subclass.
        {
            return;
        }
        smartParams = new SmartParamUpdaterInput(new ArrayList<SmartDouble>(),
                                                 new ArrayList<SmartString>(),
                                                 new ItemParam(this));
        RegisterSmartParameters();
        GetParametersValuesFromDatabase();
    }

    abstract public String getItemClassString();

    abstract protected void RegisterSmartParameters();

    // duno if it will be even used, let it stay for now
    public static Item GetItemFromDatabase(int id)
    {
        DatabaseAPI database = DatabaseHandler.Get();

        String itemClassString = database.GetItemClassString(id);
        if(itemClassString == null)
        {
            return null;
        }
        Item gettedItem = ItemClassStringToItemConverter.convert(itemClassString,id);
        if(gettedItem == null)
        {
            return null;
        }
        return gettedItem;
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
        DatabaseHandler.Get().RemoveItem(this);

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
