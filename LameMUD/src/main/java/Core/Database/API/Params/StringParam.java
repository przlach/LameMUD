package Core.Database.API.Params;

public class StringParam implements Param {

    public StringParam(String paramName, String paramValue)
    {
        this.paramName = paramName;
        this.paramValue = paramValue;
    }

    @Override
    public String getParamName() {
        return paramName;
    }

    public String getParamValue()
    {
        return paramValue;
    }

    public void setParamValue(String newVal)
    {
        paramValue = newVal;
    }

    private String paramName;
    private String paramValue;
}
