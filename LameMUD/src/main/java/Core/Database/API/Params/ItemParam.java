package Core.Database.API.Params;

import Core.CommandLine.GameLogic.Item;

public class ItemParam implements ParamOwner{

    public ItemParam(Item owner)
    {
        this.owner = owner;
    }

    public Item GetItem()
    {
        return owner;
    }

    private Item owner;
}
