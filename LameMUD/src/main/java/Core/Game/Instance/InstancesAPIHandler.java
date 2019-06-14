package Core.Game.Instance;

public class InstancesAPIHandler {


    public static InstancesAPI get() {
        return _instancesAPI;
    }

    public static void set(InstancesAPI instancesAPI) {
        _instancesAPI = instancesAPI;
    }

    static private InstancesAPI _instancesAPI;

}
