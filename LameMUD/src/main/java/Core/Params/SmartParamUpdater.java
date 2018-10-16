package Core.Params;

public class SmartParamUpdater {

    public static void GetParametersValuesFromDatabase(SmartParamUpdaterInput input)
    {
        for (SmartDouble smartDouble:input.getSmartDoubles())
        {
            smartDouble.GetValueFromDatabase(input.getOwner());
        }
        for (SmartString smartString:input.getSmartStrings())
        {
            smartString.GetValueFromDatabase(input.getOwner());
        }
    }

    public static void SetParametersValueInDatabase(SmartParamUpdaterInput input)
    {
        for (SmartDouble smartDouble:input.getSmartDoubles())
        {
            smartDouble.SetValueInDatabase(input.getOwner());
        }
        for (SmartString smartString:input.getSmartStrings())
        {
            smartString.SetValueInDatabase(input.getOwner());
        }
    }

    public static void RemoveParametersFromDatabase(SmartParamUpdaterInput input)
    {
        for (SmartDouble smartDouble:input.getSmartDoubles())
        {
            smartDouble.RemoveValueFromDatabase(input.getOwner());
        }
        for (SmartString smartString:input.getSmartStrings())
        {
            smartString.RemoveValueFromDatabase(input.getOwner());
        }
    }
}
