package Core.CommandLine.GameLogic;

abstract public class ItemBuilder {

    public Item Build()
    {
        return selectedInternalBuilder.Build();
    }
    public Item Build(int id)
    {
        return selectedInternalBuilder.Build(id);
    }
    public void SetInternalBuilder(ItemBuilderInternal internalBuilder)
    {
        selectedInternalBuilder = internalBuilder;
    }

    protected ItemBuilder()
    {

    }

    private ItemBuilderInternal selectedInternalBuilder;
}
