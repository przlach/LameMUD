package Core.Params.SmartObject;

abstract public class SmartObjectBuilder {

    public SmartObject Build()
    {
        return selectedInternalBuilder.Build();
    }
    public SmartObject Build(int id)
    {
        return selectedInternalBuilder.Build(id);
    }
    public void SetInternalBuilder(SmartObjectBuilderInternal internalBuilder)
    {
        selectedInternalBuilder = internalBuilder;
    }

    protected SmartObjectBuilder()
    {

    }

    private SmartObjectBuilderInternal selectedInternalBuilder;
}
