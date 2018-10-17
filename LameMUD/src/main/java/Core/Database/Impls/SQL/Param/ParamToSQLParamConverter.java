package Core.Database.Impls.SQL.Param;

import Core.Database.API.Params.*;

public class ParamToSQLParamConverter {

    public static SQLParam convert(Param param)
    {
        if(param instanceof DoubleParam)
        {
            SQLDoubleParam convertedParam = new SQLDoubleParam(param.getParamName(),((DoubleParam) param).getParamValue());
            return convertedParam;
        }
        else if(param instanceof StringParam)
        {
            SQLStringParam convertedParam = new SQLStringParam(param.getParamName(),((StringParam) param).getParamValue());
            return convertedParam;
        }
        else
        {
            try {
                throw new Exception("Got not handled subclass of Param interface");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static SQLParamOwner convert(ParamOwner owner)
    {
        if(owner instanceof UserParam)
        {
            SQLUserParam convertedOwner = new SQLUserParam(((UserParam) owner).GetUser());
            return convertedOwner;
        }
        else if(owner instanceof SmartObjectParam)
        {
            SQLSmartObjectParam convertedOwner = new SQLSmartObjectParam(((SmartObjectParam) owner).GetSmartObject());
            return convertedOwner;
        }
        else
        {
            try {
                throw new Exception("Got not handled subclass of ParamOwner interface");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
