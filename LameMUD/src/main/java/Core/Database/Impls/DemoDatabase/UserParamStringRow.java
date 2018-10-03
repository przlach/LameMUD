package Core.Database.Impls.DemoDatabase;

public class UserParamStringRow {

    private static int rowCounter;
    private final int id;
    int userID;
    String paramName;
    String paramVal;

    static
    {
        rowCounter = 0;
    }

    public UserParamStringRow(int userID, String paramName, String paramVal) {
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

    public String getParamVal() {
        return paramVal;
    }

    public void setParamVal(String paramVal) {
        this.paramVal = paramVal;
    }
}
