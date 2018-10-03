package Core.Database.Impls.DemoDatabase;

public class UserTableRow {

    private static int rowCounter;
    private final int id;
    private String username;
    private String password;

    static
    {
        rowCounter = 0;
    }

    public UserTableRow(String username, String password) {
        this.username = username;
        this.password = password;
        id = ++rowCounter;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
