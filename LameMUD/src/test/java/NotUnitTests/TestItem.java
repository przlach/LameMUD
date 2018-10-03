package NotUnitTests;

import Core.CommandLine.GameLogic.Item;

public class TestItem extends Item {

    public TestItem(int id)
    {
        this.fakeID = id;
    }

    @Override
    public int GetID() {
        return fakeID;
    }

    @Override
    public String getItemClassString() {
        return null;
    }

    @Override
    protected void RegisterSmartParameters() {

    }
    private final int fakeID;
}
