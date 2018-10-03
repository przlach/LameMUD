package Core.Database.Impls.DemoDatabase;

public class UserParamDoubleRow {

    private static int rowCounter;
    private final int id;
    private int userID;
    private String paramName;
    private double paramVal;

    static
    {
        rowCounter = 0;
    }

    public UserParamDoubleRow(int userID, String paramName, double paramVal) {
        this.userID = userID;
        this.paramName = paramName;
        this.paramVal = paramVal;
        id = ++rowCounter;
    }

    public int getId() {
        return id;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public double getParamVal() {
        return paramVal;
    }

    public void setParamVal(double paramVal) {
        this.paramVal = paramVal;
    }
}
