package Core.Database.API.Params;

import Core.Params.SmartObject.SmartObject;

public class SmartObjectParam implements ParamOwner{

    public SmartObjectParam(SmartObject owner)
    {
        this.owner = owner;
    }

    public SmartObject GetSmartObject()
    {
        return owner;
    }

    private SmartObject owner;
}
