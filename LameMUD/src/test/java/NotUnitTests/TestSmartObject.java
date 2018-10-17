package NotUnitTests;

import Core.Params.SmartObject.SmartObject;

public class TestSmartObject extends SmartObject {

    public TestSmartObject(int id)
    {
        this.fakeID = id;
    }

    @Override
    public int GetID() {
        return fakeID;
    }

    @Override
    public String getSmartObjectClassString() {
        return null;
    }

    @Override
    protected void RegisterSmartParameters() {

    }
    private final int fakeID;
}
