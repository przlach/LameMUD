package Core.CommandLine.GameLogic;

public class ItemBuilder {

    public static Item Build()
    {
        return selectedBuilder.Build();
    }
    public static Item Build(int id)
    {
        return selectedBuilder.Build(id);
    }
    public static void SetInternalBuilder(ItemBuilderInternal internalBuilder)
    {
        selectedBuilder = internalBuilder;
    }

    private static ItemBuilderInternal selectedBuilder;

}
