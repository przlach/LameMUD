package Core.Params;

import Core.Database.API.Params.ParamOwner;

import java.util.Collection;

public class SmartParamUpdaterInput {

    private Collection<SmartDouble> smartDoubles;
    private Collection<SmartString> smartStrings;
    private ParamOwner owner;

    public SmartParamUpdaterInput(Collection<SmartDouble> smartDoubles, Collection<SmartString> smartStrings, ParamOwner owner) {
        this.smartDoubles = smartDoubles;
        this.smartStrings = smartStrings;
        this.owner = owner;
    }

    public Collection<SmartDouble> getSmartDoubles() {
        return smartDoubles;
    }

    public void setSmartDoubles(Collection<SmartDouble> smartDoubles) {
        this.smartDoubles = smartDoubles;
    }

    public Collection<SmartString> getSmartStrings() {
        return smartStrings;
    }

    public void setSmartStrings(Collection<SmartString> smartStrings) {
        this.smartStrings = smartStrings;
    }

    public ParamOwner getOwner() {
        return owner;
    }

    public void setOwner(ParamOwner owner) {
        this.owner = owner;
    }

    public boolean trackSmartParameter(SmartDouble param)
    {
        if(containsSameSmartParam(param))
        {
            return false;
        }
        getSmartDoubles().add(param);
        return true;
    }

    public boolean containsSameSmartParam(SmartDouble param)
    {
        for (SmartDouble smartDouble: getSmartDoubles()) {

            if(smartDouble.GetParamName().equals(param.GetParamName()))
            {
                return true;
            }
        }
        return false;
    }

    public boolean trackSmartParameter(SmartString param)
    {
        if(containsSameSmartParam(param))
        {
            return false;
        }
        getSmartStrings().add(param);
        return true;
    }

    public boolean containsSameSmartParam(SmartString param)
    {
        for (SmartString smartString: getSmartStrings()) {

            if(smartString.GetParamName().equals(param.GetParamName()))
            {
                return true;
            }
        }
        return false;
    }
}
