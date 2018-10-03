package Core.CommandLine.GameLogic;

import Core.Database.API.DatabaseAPI;
import Core.Database.API.DatabaseHandler;
import Core.Database.API.Params.ItemParam;

import java.util.ArrayList;

public abstract class Item {

    public Item()
    {
        DatabaseAPI database = DatabaseHandler.Get();
        this.id = database.AddItem(getItemClassString());
        thisParam = new ItemParam(this);
        RegisterSmartParameters();
    }

    public Item(int id)
    {
        this.id = id;
        if(id < 0)  // allows to create not properly functioning Item, currently used if you need only to use getItemClassString() of any subclass.
        {
            return;
        }
        thisParam = new ItemParam(this);
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
        for (SmartDouble smartDouble:trackedSmartDoubles)
        {
            smartDouble.GetValueFromDatabase(thisParam);
        }
        for (SmartString smartString:trackedSmartStrings)
        {
            smartString.GetValueFromDatabase(thisParam);
        }
    }

    public void SetParametersValueInDatabase()
    {
        for (SmartDouble smartDouble:trackedSmartDoubles)
        {
            smartDouble.SetValueInDatabase(thisParam);
        }
        for (SmartString smartString:trackedSmartStrings)
        {
            smartString.SetValueInDatabase(thisParam);
        }
    }

    public void RemoveParametersFromDatabase()
    {
        for (SmartDouble smartDouble:trackedSmartDoubles)
        {
            smartDouble.RemoveValueFromDatabase(thisParam);
        }
        for (SmartString smartString:trackedSmartStrings)
        {
            smartString.RemoveValueFromDatabase(thisParam);
        }
    }

    protected boolean trackSmartParameter(SmartDouble param)
    {
        if(containsSameSmartParam(param))
        {
            return false;
        }
        trackedSmartDoubles.add(param);
        return true;
    }

    private boolean containsSameSmartParam(SmartDouble param)
    {
        for (SmartDouble smartDouble: trackedSmartDoubles) {

            if(smartDouble.GetParamName().equals(param.GetParamName()))
            {
                return true;
            }
        }
        return false;
    }

    protected boolean trackSmartParameter(SmartString param)
    {
        if(containsSameSmartParam(param))
        {
            return false;
        }
        trackedSmartStrings.add(param);
        return true;
    }

    private boolean containsSameSmartParam(SmartString param)
    {
        for (SmartString smartString: trackedSmartStrings) {

            if(smartString.GetParamName().equals(param.GetParamName()))
            {
                return true;
            }
        }
        return false;
    }

    public boolean Remove()
    {
        PreRemove();

        RemoveParametersFromDatabase();
        trackedSmartDoubles = null;
        trackedSmartStrings = null;
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

    private ItemParam thisParam;
    protected final int id;
    private ArrayList<SmartDouble> trackedSmartDoubles = new ArrayList<SmartDouble>();
    private ArrayList<SmartString> trackedSmartStrings = new ArrayList<SmartString>();

}
