package NotUnitTests;

import Core.Params.SmartObject.SmartObject;
import Core.CommandLine.Platforms.PlatformMessageHeader;
import Core.CommandLine.User.User;
import Core.Config.MainConfig;
import Core.Database.API.DatabaseAPI;
import Core.Database.API.DatabaseHandler;
import Core.Database.API.Params.*;
import Core.Database.Impls.SQL.Connection.SQLServersCollection;
import Core.Config.LocalTestServerParameters;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class DatabaseAPITests {

    //TODO public void TestConnection();

    //TODO test where you try to access database by DatabaseAPI functions but sql server is down?


    @Before
    public void setUp() throws Exception {
        InitDatabase();
    }

    @Test
    public void testCreateUser() {

        String userName = "testNickname";
        String password = "testPassword";
        DatabaseAPI database = DatabaseHandler.Get();

        if (database.IsUser(userName)) {
            boolean wasUserRemoved = database.RemoveUser(userName);
            assertTrue("Couldn't remove user from database", wasUserRemoved);
        }

        int createdUserId = database.AddUser(userName, password);
        assertTrue("AddUser failed.", createdUserId >= 0);
    }

    @Test
    public void testTryToCreateUserWithAlreadyExistingUsername() {

        String userName = "testNickname";
        String password = "testPassword";
        DatabaseAPI database = DatabaseHandler.Get();

        if (!database.IsUser(userName)) {
            int createdUserId = database.AddUser(userName, password);
            assertTrue("Adding user failed.", createdUserId >= 0);
        }

        int sameUserId = database.AddUser(userName, password);
        assertTrue("AddUser() allowed to create user with the same nickname.", sameUserId == -1);

    }

    @Test
    public void testGetUser() {

        String userName = "testNickname";
        String password = "testPassword";
        DatabaseAPI database = DatabaseHandler.Get();

        if (!database.IsUser(userName)) {
            int createdUserId = database.AddUser(userName, password);
            assertTrue("Adding user failed.", createdUserId >= 0);
        }

        int gettedUserId = database.GetUserID(userName);
        assertTrue("GetUser() returned nothing.", gettedUserId >= 0);

    }

    @Test
    public void testIfGetUserReturnsProperId() {

        String userName = "testNickname";
        String password = "testPassword";
        DatabaseAPI database = DatabaseHandler.Get();

        if (database.IsUser(userName)) {
            boolean wasUserRemoved = database.RemoveUser(userName);
            assertTrue("Couldn't remove user from database", wasUserRemoved);
        }

        int createdUserId = database.AddUser(userName, password);
        assertTrue("Adding user failed.", createdUserId >= 0);

        int gettedUserId = database.GetUserID(userName);
        assertTrue("GetUser() returned not allowed userId.", gettedUserId >= 0);

        assertEquals("id gotten by GetUser() is different then the one returned by AddUser()",
                gettedUserId, createdUserId);
    }

    @Test
    public void testGetNonExistingUser() {

        String userName = "testNickname";
        DatabaseAPI database = DatabaseHandler.Get();

        if (database.IsUser(userName)) {
            boolean wasUserRemoved = database.RemoveUser(userName);
            assertTrue("Couldn't remove user from database", wasUserRemoved);
        }

        int gettedUserId = database.GetUserID(userName);
        assertTrue("GetUser() returned proper userId, when it doesn't exist.", gettedUserId == -1);
    }

    @Test
    public void testIsUser() {

        String userName = "testNickname";
        String password = "testPassword";
        DatabaseAPI database = DatabaseHandler.Get();

        database.AddUser(userName, password);

        boolean userExists = database.IsUser(userName);
        assertTrue("IsUser() returned false", userExists);

    }

    @Test
    public void testIsNonExistingUser() {

        String userName = "testNickname";
        DatabaseAPI database = DatabaseHandler.Get();

        database.RemoveUser(userName);

        boolean userExists = database.IsUser(userName);
        assertFalse("IsUser() returned true", userExists);
    }

    @Test
    public void testRemoveUser() {

        String userName = "testNickname";
        String password = "testPassword";
        DatabaseAPI database = DatabaseHandler.Get();

        if (!database.IsUser(userName)) {
            int createdUserId = database.AddUser(userName, password);
            assertTrue("Adding user failed.", createdUserId >= 0);
        }

        boolean wasUserRemoved = database.RemoveUser(userName);
        assertTrue("RemoveUser() returned false", wasUserRemoved);
    }

    @Test
    public void testRemoveNonExistingUser() {

        String userName = "testNickname";
        String password = "testPassword";
        DatabaseAPI database = DatabaseHandler.Get();

        if (database.IsUser(userName)) {
            boolean wasUserRemoved = database.RemoveUser(userName);
            assertTrue("Couldn't remove user from database", wasUserRemoved);
        }

        boolean wasUserRemoved = database.RemoveUser(userName);
        assertFalse("RemoveUser() returned true", wasUserRemoved);
    }

    @Test
    public void testAddSmartObject() {

        String smartObjectClass = "testClass";
        DatabaseAPI database = DatabaseHandler.Get();

        int createdSmartObjectID = database.AddSmartObject(smartObjectClass);
        assertTrue("AddSmartObject() returned invalid smartObjectID", createdSmartObjectID >= 0);
    }

    @Test
    public void testGetSmartObjectClassString() {

        String smartObjectClass = "testClass";
        DatabaseAPI database = DatabaseHandler.Get();

        int createdSmartObjectID = database.AddSmartObject(smartObjectClass);
        assertTrue("Couldn't create smartObject.", createdSmartObjectID >= 0);

        String gettedSmartObjectClassString = database.GetSmartObjectClassString(createdSmartObjectID);
        assertNotNull("GetGetSmartObjectClassString() returned null.", gettedSmartObjectClassString);
        assertEquals("Getted smartObject class string doesn't match the requested one.", smartObjectClass, gettedSmartObjectClassString);
    }

    @Test
    public void testGetNonExistingSmartObjectClassString() {

        String smartObjectClass = "testClass";
        DatabaseAPI database = DatabaseHandler.Get();

        int createdSmartObjectID = database.AddSmartObject(smartObjectClass);
        assertTrue("Couldn't create smartObject.", createdSmartObjectID >= 0);

        String gettedSmartObjectClassString = database.GetSmartObjectClassString(createdSmartObjectID + 1);
        assertNull("GetSmartObject() returned not null.", gettedSmartObjectClassString);
    }

    @Test
    public void testIsSmartObject() {

        String smartObjectClass = "testClass";
        DatabaseAPI database = DatabaseHandler.Get();

        int createdSmartObjectID = database.AddSmartObject(smartObjectClass);
        assertTrue("Couldn't create smartObject.", createdSmartObjectID >= 0);

        boolean doesSmartObjectExist = database.IsSmartObject(createdSmartObjectID);
        assertTrue("IsSmartObject() returned false.", doesSmartObjectExist);
    }

    @Test
    public void testIsNonExistingSmartObject() {

        String smartObjectClass = "testClass";
        DatabaseAPI database = DatabaseHandler.Get();

        int createdSmartObjectID = database.AddSmartObject(smartObjectClass);
        assertTrue("Couldn't create smartObject.", createdSmartObjectID >= 0);

        boolean doesSmartObjectExist = database.IsSmartObject(createdSmartObjectID + 1);
        assertTrue("IsSmartObject() returned true.", doesSmartObjectExist);
    }

    @Test
    public void testRemoveSmartObject() {

        String smartObjectClass = "testClass";
        DatabaseAPI database = DatabaseHandler.Get();

        int createdSmartObjectID = database.AddSmartObject(smartObjectClass);
        assertTrue("Couldn't create smartObject.", createdSmartObjectID >= 0);

        boolean isSmartObjectRemoved = database.RemoveSmartObject(createdSmartObjectID);
        assertTrue("RemoveSmartObject() returned false.", isSmartObjectRemoved);
    }

    @Test
    public void testRemoveNonExistingSmartObject() {

        String smartObjectClass = "testClass";
        DatabaseAPI database = DatabaseHandler.Get();

        int createdSmartObjectID = database.AddSmartObject(smartObjectClass);
        assertTrue("Couldn't create smartObject.", createdSmartObjectID >= 0);

        boolean isSmartObjectRemoved = database.IsSmartObject(createdSmartObjectID + 1);
        assertTrue("RemoveSmartObject() returned true.", isSmartObjectRemoved);
    }

    @Test
    public void testPasswordAuthenticate() {

        DatabaseAPI database = DatabaseHandler.Get();

        String userName = "testNickname";
        String password = "testPassword";
        int testUserId = database.GetUserID(userName);

        if (testUserId < 0) {
            int createdUserId = database.AddUser(userName, password);
            assertTrue("Adding user failed.", createdUserId >= 0);
        }

        boolean authenticationResult = database.AuthenticateUser(userName, password);
        assertTrue("AuthenticateUser() returned false", authenticationResult);
    }

    @Test
    public void testWrongPasswordAuthenticate() {

        DatabaseAPI database = DatabaseHandler.Get();

        String userName = "testNickname";
        String password = "testPassword";
        String wrongPassword = "wrongPwd";
        int testUserId = database.GetUserID(userName);

        if (testUserId < 0) {
            int createdUserId = database.AddUser(userName, password);
            assertTrue("Adding user failed.", createdUserId >= 0);
        }

        boolean authenticationResult = database.AuthenticateUser(userName, wrongPassword);
        assertFalse("AuthenticateUser() returned true, even when wrong password was given", authenticationResult);
    }

    @Test
    public void testPasswordAuthenticateWithoutExistingUser() {

        DatabaseAPI database = DatabaseHandler.Get();

        String userName = "testNickname";
        String password = "testPassword";
        int testUserId = database.GetUserID(userName);

        if (testUserId >= 0) {
            boolean removeUserResult = database.RemoveUser(userName);
            assertTrue("Couldn't remove user", removeUserResult);
        }

        boolean authenticationResult = database.AuthenticateUser(userName, password);
        assertFalse("AuthenticateUser() returned true, even when user isn't existing", authenticationResult);

    }

    @Test
    public void testHeaderAuthenticate() {

        DatabaseAPI database = DatabaseHandler.Get();

        String userName = "testNickname";
        String password = "testPassword";
        int testUserId = database.GetUserID(userName);

        if (testUserId < 0) {
            int createdUserId = database.AddUser(userName, password);
            assertTrue("Adding user failed.", createdUserId >= 0);
        }

        StubPlatformMessageHeader testedHeader = new StubPlatformMessageHeader();
        testedHeader.SetUserIdentifier("testID");
        database.RemoveTrustedPlatformFromUser(testedHeader, userName);
        boolean addTrustedPlatformResult = database.AddTrustedPlatformToUser(testedHeader, userName);
        assertTrue("Couldn't add trusted platform", addTrustedPlatformResult);

        boolean authenticationResult = database.AuthenticateUser(userName, testedHeader);
        assertTrue("AuthenticateUser() didn't returned succeded", authenticationResult);
    }

    @Test
    public void testWrongHeaderAuthenticate() {

        DatabaseAPI database = DatabaseHandler.Get();

        String userName = "testNickname";
        String password = "testPassword";
        int testUserId = database.GetUserID(userName);

        if (testUserId < 0) {
            int createdUserId = database.AddUser(userName, password);
            assertTrue("Adding user failed.", createdUserId >= 0);
        }

        StubPlatformMessageHeader testedHeader = new StubPlatformMessageHeader();
        testedHeader.SetUserIdentifier("testID");
        database.RemoveTrustedPlatformFromUser(testedHeader, userName);

        boolean authenticationResult = database.AuthenticateUser(userName, testedHeader);
        assertFalse("AuthenticateUser(...) didn't failed, even if selected header isn't the trusted one", authenticationResult);
    }

    @Test
    public void testHeaderAuthenticateWhenPlatformDoesntSupportIt() {

        DatabaseAPI database = DatabaseHandler.Get();

        String userName = "testNickname";
        String password = "testPassword";
        int testUserId = database.GetUserID(userName);

        if (testUserId < 0) {
            int createdUserId = database.AddUser(userName, password);
            assertTrue("Adding user failed.", createdUserId >= 0);
        }

        StubPlatformMessageHeader testedHeader = new StubPlatformMessageHeader();
        testedHeader.SetUserIdentifier("testID");
        database.RemoveTrustedPlatformFromUser(testedHeader, userName);
        boolean addTrustedPlatformResult = database.AddTrustedPlatformToUser(testedHeader, userName);
        assertTrue("Couldn't add trusted platform", addTrustedPlatformResult);

        testedHeader.setTrustedLogin(false);
        boolean authenticationResult = database.AuthenticateUser(userName, testedHeader);
        assertFalse("AuthenticateUser(...) returned false, when header trusted login was set to false.", authenticationResult);
    }

    @Test
    public void testAddTrustedPlatform() {

        DatabaseAPI database = DatabaseHandler.Get();

        CreateTestUser();
        RemoveTrustedTestHeaderFromTestUser();

        StubPlatformMessageHeader testHeader = new StubPlatformMessageHeader();
        boolean addTrustedPlatformResult = database.AddTrustedPlatformToUser(testHeader, testUserUsername);
        assertTrue("Adding trusted platform failed", addTrustedPlatformResult);
    }

    @Test
    public void testAddTrustedPlatformWhenHeaderDoesntSupportIt() {

        DatabaseAPI database = DatabaseHandler.Get();

        CreateTestUser();
        RemoveTrustedTestHeaderFromTestUser();

        StubPlatformMessageHeader testHeader = new StubPlatformMessageHeader();
        testHeader.setTrustedLogin(false);
        boolean addTrustedPlatformResult = database.AddTrustedPlatformToUser(testHeader, testUserUsername);
        assertFalse("Adding trusted platform succeded, even when header doesn't support trusted login",
                addTrustedPlatformResult);
    }

    @Test
    public void testAddTrustedPlatformToNonexistingUser() {

        DatabaseAPI database = DatabaseHandler.Get();

        CreateTestUser();
        RemoveTrustedTestHeaderFromTestUser();
        RemoveTestUser();

        StubPlatformMessageHeader testHeader = new StubPlatformMessageHeader();
        boolean addTrustedPlatformResult = database.AddTrustedPlatformToUser(testHeader, testUserUsername);
        assertFalse("Adding trusted platform succeded, even when targeted user doesn't exist",
                addTrustedPlatformResult);
    }

    @Test
    public void testAddSameTrustedPlatformHeaderToUserMultipleTimes() {

        DatabaseAPI database = DatabaseHandler.Get();

        CreateTestUser();
        RemoveTrustedTestHeaderFromTestUser();

        StubPlatformMessageHeader testHeader = new StubPlatformMessageHeader();
        boolean addTrustedPlatformResult = database.AddTrustedPlatformToUser(testHeader, testUserUsername);
        assertTrue("Adding trusted platform failed", addTrustedPlatformResult);
        boolean addAgainTrustedPlatformResult = database.AddTrustedPlatformToUser(testHeader, testUserUsername);
        assertFalse("Adding trusted platform again succeded", addAgainTrustedPlatformResult);
    }

    @Test
    public void testRemoveTrustedPlatform() {

        DatabaseAPI database = DatabaseHandler.Get();

        CreateTestUser();
        AddTrustedTestHeaderToTestUser();

        StubPlatformMessageHeader testHeader = new StubPlatformMessageHeader();
        boolean removeTrustedPlatformResult = database.RemoveTrustedPlatformFromUser(testHeader, testUserUsername);
        assertTrue("Removing trusted platform failed", removeTrustedPlatformResult);
    }

    @Test
    public void testRemoveTrustedPlatformWhenHeaderDoesntSupportIt() {

        DatabaseAPI database = DatabaseHandler.Get();

        CreateTestUser();
        AddTrustedTestHeaderToTestUser();

        StubPlatformMessageHeader testHeader = new StubPlatformMessageHeader();
        testHeader.setTrustedLogin(false);
        boolean removeTrustedPlatformResult = database.RemoveTrustedPlatformFromUser(testHeader, testUserUsername);
        assertTrue("Removing trusted platform failed.",
                removeTrustedPlatformResult);
    }

    @Test
    public void testRemoveTrustedPlatformFromNonexistingUser() {

        DatabaseAPI database = DatabaseHandler.Get();

        CreateTestUser();
        AddTrustedTestHeaderToTestUser();
        RemoveTestUser();

        StubPlatformMessageHeader testHeader = new StubPlatformMessageHeader();
        testHeader.setTrustedLogin(false);
        boolean removeTrustedPlatformResult = database.RemoveTrustedPlatformFromUser(testHeader, testUserUsername);
        assertFalse("Removing trusted platform succeded, even when user doesn't exist",
                removeTrustedPlatformResult);
    }

    @Test
    public void testRemoveSameTrustedHeaderFromUserMultipleTimes() {

        DatabaseAPI database = DatabaseHandler.Get();

        CreateTestUser();
        AddTrustedTestHeaderToTestUser();

        StubPlatformMessageHeader testHeader = new StubPlatformMessageHeader();
        boolean removeTrustedPlatformResult = database.RemoveTrustedPlatformFromUser(testHeader, testUserUsername);
        assertTrue("Removing trusted platform failed", removeTrustedPlatformResult);
        boolean removeAgainTrustedPlatformResult = database.RemoveTrustedPlatformFromUser(testHeader, testUserUsername);
        assertFalse("Removing trusted platform succeded, even when it was already removed earlier", removeAgainTrustedPlatformResult);
    }

    @Test
    public void testUserStringParamFirstTimeSet()
    {
        String testParamViaGetParam = (String)SetParameterFirstTimeAndReturnIt(testedUserOwner, testedStringParam);
        assertEquals("Param value got via GetParam() isn't equal to the one used in SetParam()", testStringParamValue,testParamViaGetParam);
    }

    @Test
    public void testUserStringParamSetAgain()
    {
        String secondTestStringParamValue = "testStringParamValue";
        StringParam secondTestParam = new StringParam("secondTestStringParamName",secondTestStringParamValue);

        String testParamViaGetParam = (String)testSetParameterAgainAndReturnIt(testedUserOwner, testedStringParam,secondTestParam);
        assertEquals("Param value got via GetParam() isn't equal to the one used in SetParam()",testStringParamValue,testParamViaGetParam);
    }

    @Test
    public void testUserStringParamRemove()
    {
        testRemoveParameter(testedUserOwner,testedStringParam);
    }

    @Test
    public void testRemoveNonExistingUserStringParam()
    {
        testRemoveNonExistingParameter(testedUserOwner,testedStringParam);
    }

    @Test
    public void testSmartObjectStringParamFirstTimeSet()
    {
        String testParamViaGetParam = (String)SetParameterFirstTimeAndReturnIt(testedSmartObjectOwner, testedStringParam);
        assertEquals("Param value got via GetParam() isn't equal to the one used in SetParam()", testStringParamValue,testParamViaGetParam);
    }

    @Test
    public void testSmartObjectStringParamSetAgain()
    {
        String secondTestStringParamValue = "testStringParamValue";
        StringParam secondTestParam = new StringParam("secondTestStringParamName",secondTestStringParamValue);

        String testParamViaGetParam = (String)testSetParameterAgainAndReturnIt(testedSmartObjectOwner, testedStringParam,secondTestParam);
        assertEquals("Param value got via GetParam() isn't equal to the one used in SetParam()",testStringParamValue,testParamViaGetParam);
    }

    @Test
    public void testSmartObjectStringParamRemove()
    {
        testRemoveParameter(testedSmartObjectOwner,testedStringParam);
    }

    @Test
    public void testRemoveNonExistingSmartObjectStringParam()
    {
        testRemoveNonExistingParameter(testedSmartObjectOwner,testedStringParam);
    }

    @Test
    public void testUserDoubleParamFirstTimeSet()
    {
        double testParamViaGetParam = (Double)SetParameterFirstTimeAndReturnIt(testedUserOwner, testedDoubleParam);
        assertEquals("Param value got via GetParam() isn't equal to the one used in SetParam()", testDoubleParamValue,testParamViaGetParam,0.0001);
    }

    @Test
    public void testUserDoubleParamSetAgain()
    {
        double secondTestDoubleParamValue = 2.77;
        DoubleParam secondTestParam = new DoubleParam("secondTestDoubleParamName",secondTestDoubleParamValue);

        double testParamViaGetParam = (Double)testSetParameterAgainAndReturnIt(testedUserOwner, testedDoubleParam,secondTestParam);
        assertEquals("Param value got via GetParam() isn't equal to the one used in SetParam()",testDoubleParamValue,testParamViaGetParam,0.0001);
    }

    @Test
    public void testUserDoubleParamRemove()
    {
        testRemoveParameter(testedUserOwner,testedDoubleParam);
    }

    @Test
    public void testRemoveNonExistingUserDoubleParam()
    {
        testRemoveNonExistingParameter(testedUserOwner,testedDoubleParam);
    }

    @Test
    public void testSmartObjectDoubleParamFirstTimeSet()
    {
        double testParamViaGetParam = (Double)SetParameterFirstTimeAndReturnIt(testedSmartObjectOwner, testedDoubleParam);
        assertEquals("Param value got via GetParam() isn't equal to the one used in SetParam()", testDoubleParamValue,testParamViaGetParam,0.0001);
    }

    @Test
    public void testSmartObjectDoubleParamSetAgain()
    {
        double secondTestDoubleParamValue = 7.142;
        DoubleParam secondTestParam = new DoubleParam("secondTestDoubleParamName",secondTestDoubleParamValue);

        double testParamViaGetParam = (Double)testSetParameterAgainAndReturnIt(testedSmartObjectOwner, testedDoubleParam,secondTestParam);
        assertEquals("Param value got via GetParam() isn't equal to the one used in SetParam()",testDoubleParamValue,testParamViaGetParam,0.0001);
    }

    @Test
    public void testSmartObjectDoubleParamRemove()
    {
        testRemoveParameter(testedSmartObjectOwner,testedDoubleParam);
    }

    @Test
    public void testRemoveNonExistingSmartObjectDoubleParam()
    {
        testRemoveNonExistingParameter(testedSmartObjectOwner,testedDoubleParam);
    }

    private void InitDatabase() {
        MainConfig.SetConfig();
        SQLServersCollection.addServer(new LocalTestServerParameters());

        testSmartObject = new TestSmartObject(6);
        testedSmartObjectOwner = new SmartObjectParam(testSmartObject);
        User.Create(testUserUsername,testUserPassword);
        testUser = User.Get(testUserUsername);
        testedUserOwner = new UserParam(testUser);
    }

    private int CreateTestUser() {
        int testUserId;
        DatabaseAPI database = DatabaseHandler.Get();

        testUserId = database.GetUserID(testUserUsername);
        if (testUserId < 0) {
            testUserId = database.AddUser(testUserUsername, testUserPassword);
            assertTrue("Adding user failed.", testUserId >= 0);
        }
        return testUserId;
    }

    private void RemoveTestUser() {
        int testUserId;
        DatabaseAPI database = DatabaseHandler.Get();

        testUserId = database.GetUserID(testUserUsername);
        if (testUserId >= 0) {
            boolean removeUserResult = database.RemoveUser(testUserUsername);
            assertTrue("Removing user failed.", removeUserResult);
        }
    }

    private void RemoveTrustedTestHeaderFromTestUser() {
        DatabaseAPI database = DatabaseHandler.Get();

        PlatformMessageHeader testHeader = new StubPlatformMessageHeader();
        database.RemoveTrustedPlatformFromUser(testHeader, testUserUsername);
    }

    private void AddTrustedTestHeaderToTestUser() {
        DatabaseAPI database = DatabaseHandler.Get();

        PlatformMessageHeader testHeader = new StubPlatformMessageHeader();
        database.AddTrustedPlatformToUser(testHeader, testUserUsername);
    }

    public Object SetParameterFirstTimeAndReturnIt(ParamOwner testedOwner, Param testedParam) {

        RemoveTestParamIfExists(testedOwner,testedParam);
        SetTestParamIfDoesntExists(testedOwner,testedParam);

        return DatabaseHandler.Get().GetParam(testedOwner,testedParam);

    }

    public Object testSetParameterAgainAndReturnIt(ParamOwner testedOwner, Param testedParam, Param testedParam2) {

        RemoveTestParamIfExists(testedOwner,testedParam);
        SetTestParamIfDoesntExists(testedOwner,testedParam);
        SetTestParam(testedOwner,testedParam2);

        return DatabaseHandler.Get().GetParam(testedOwner,testedParam);
    }

    public void testRemoveParameter(ParamOwner testedOwner, Param testedParam)
    {
        SetTestParamIfDoesntExists(testedOwner,testedParam);
        RemoveTestParamIfExists(testedOwner,testedParam);
        Object getParamResult = DatabaseHandler.Get().GetParam(testedOwner,testedParam);
        assertNull("Get returned something, after the object is removed",getParamResult);
    }

    public void testRemoveNonExistingParameter(ParamOwner testedOwner, Param testedParam)
    {
        RemoveTestParamIfExists(testedOwner,testedParam);
        RemoveTestParam(testedOwner,testedParam);
        Object getParamResult = DatabaseHandler.Get().GetParam(testedOwner,testedParam);
        assertNull("Get returned something, after the object is removed",getParamResult);
    }

    public void RemoveTestParam(ParamOwner testedOwner, Param testedParam)
    {
        boolean expectedRemoveResult;
        DatabaseAPI database = DatabaseHandler.Get();

        if(database.GetParam(testedOwner,testedParam) != null)
        {
            expectedRemoveResult = true;
        }
        else
        {
            expectedRemoveResult = false;
        }
        boolean removeResult = database.RemoveParam(testedOwner,testedParam);
        assertEquals("Remove test param returned wrong boolean than expected",expectedRemoveResult,removeResult);
    }

    public void RemoveTestParamIfExists(ParamOwner testedOwner, Param testedParam)
    {
        DatabaseAPI database = DatabaseHandler.Get();

        if(database.GetParam(testedOwner,testedParam) != null)
        {
            boolean removeResult = database.RemoveParam(testedOwner,testedParam);
            assertTrue("Remove test param returned wrong boolean than expected",removeResult);
        }
    }

    public void SetTestParamIfDoesntExists(ParamOwner testedOwner, Param testedParam)
    {
        DatabaseAPI database = DatabaseHandler.Get();

        if(database.GetParam(testedOwner,testedParam) == null)
        {
            boolean setResult = database.SetParam(testedOwner,testedParam);
            assertTrue("Remove test param returned wrong boolean than expected",setResult);

            Object getResult = database.GetParam(testedOwner,testedParam);
            assertNotNull("After setting param, using get on it returns null",getResult);
        }
    }

    public void SetTestParam(ParamOwner testedOwner, Param testedParam)
    {
        DatabaseAPI database = DatabaseHandler.Get();

        boolean setResult = database.SetParam(testedOwner,testedParam);
        assertTrue("Remove test param returned wrong boolean than expected",setResult);

        Object getResult = database.GetParam(testedOwner,testedParam);
        assertNotNull("After setting param, using get on it returns null",getResult);
    }

    private final String testUserUsername = "testNickname";
    private final String testUserPassword = "testPassword";

    private User testUser;
    private final String testStringParamValue = "testStringParamValue";
    private UserParam testedUserOwner;
    private final StringParam testedStringParam = new StringParam("testStringParamName",testStringParamValue);

    private SmartObject testSmartObject;
    private final double testDoubleParamValue = 14.2;
    private SmartObjectParam testedSmartObjectOwner;
    private final DoubleParam testedDoubleParam = new DoubleParam("testDoubleParamName",testDoubleParamValue);
}
