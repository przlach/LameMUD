package Core.Database.API.Params;

public class DoubleParam implements Param {

    public DoubleParam(String paramName, double paramValue)
    {
        this.paramName = paramName;
        this.paramValue = paramValue;
    }

    @Override
    public String getParamName() {
        return paramName;
    }

    public double getParamValue()
    {
        return paramValue;
    }

    public void setParamValue(double newVal)
    {
        paramValue = newVal;
    }

    private String paramName;
    private double paramValue;
}
