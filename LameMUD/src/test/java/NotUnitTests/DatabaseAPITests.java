package NotUnitTests;

import Core.CommandLine.GameLogic.Item;
import Core.CommandLine.Platforms.PlatformMessageHeader;
import Core.CommandLine.User.User;
import Core.CommandLine.User.UserBuilder;
import Core.Config.MainConfig;
import Core.Database.API.DatabaseAPI;
import Core.Database.API.DatabaseHandler;
import Core.Database.API.Params.*;
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

        User returnerUser = database.AddUser(userName, password);
        assertNotNull("AddUser didn't return anything.", returnerUser);
    }

    @Test
    public void testTryToCreateUserWithAlreadyExistingUsername() {

        String userName = "testNickname";
        String password = "testPassword";
        DatabaseAPI database = DatabaseHandler.Get();

        if (!database.IsUser(userName)) {
            User createdUser = database.AddUser(userName, password);
            assertNotNull("Adding user failed.", createdUser);
        }

        User sameUser = database.AddUser(userName, password);
        assertNull("AddUser() allowed to create user with the same nickname.", sameUser);

    }

    @Test
    public void testGetUser() {

        String userName = "testNickname";
        String password = "testPassword";
        DatabaseAPI database = DatabaseHandler.Get();

        if (!database.IsUser(userName)) {
            User createdUser = database.AddUser(userName, password);
            assertNotNull("Adding user failed.", createdUser);
        }

        User gettedUser = database.GetUser(userName);
        assertNotNull("GetUser() returned nothing.", gettedUser);

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

        User createdUser = database.AddUser(userName, password);
        assertNotNull("Adding user failed.", createdUser);

        User gettedUser = database.GetUser(userName);
        assertNotNull("GetUser() returned nothing.", gettedUser);

        assertEquals("id gotten by GetUser() is different then the one returned by AddUser()",
                gettedUser.getId(), createdUser.getId());
    }

    @Test
    public void testGetNonExistingUser() {

        String userName = "testNickname";
        DatabaseAPI database = DatabaseHandler.Get();

        if (database.IsUser(userName)) {
            boolean wasUserRemoved = database.RemoveUser(userName);
            assertTrue("Couldn't remove user from database", wasUserRemoved);
        }

        User gettedUser = database.GetUser(userName);
        assertNull("GetUser() returned user, when it doesn't exist.", gettedUser);
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
            User createdUser = database.AddUser(userName, password);
            assertNotNull("Adding user failed.", createdUser);
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
    public void testAddItem() {

        String itemClass = "testClass";
        DatabaseAPI database = DatabaseHandler.Get();

        int createdItemID = database.AddItem(itemClass);
        assertTrue("AddItem() returned invalid itemID", createdItemID >= 0);
    }

    @Test
    public void testGetItemClassString() {

        String itemClass = "testClass";
        DatabaseAPI database = DatabaseHandler.Get();

        int createdItemID = database.AddItem(itemClass);
        assertTrue("Couldn't create item.", createdItemID >= 0);

        String gettedItemClassString = database.GetItemClassString(createdItemID);
        assertNotNull("GetGetItemClassString() returned null.", gettedItemClassString);
        assertEquals("Getted item class string doesn't match the requested one.", itemClass, gettedItemClassString);
    }

    @Test
    public void testGetNonExistingItemClassString() {

        String itemClass = "testClass";
        DatabaseAPI database = DatabaseHandler.Get();

        int createdItemID = database.AddItem(itemClass);
        assertTrue("Couldn't create item.", createdItemID >= 0);

        String gettedItemClassString = database.GetItemClassString(createdItemID + 1);
        assertNull("GetItem() returned not null.", gettedItemClassString);
    }

    @Test
    public void testIsItem() {

        String itemClass = "testClass";
        DatabaseAPI database = DatabaseHandler.Get();

        int createdItemID = database.AddItem(itemClass);
        assertTrue("Couldn't create item.", createdItemID >= 0);

        boolean doesItemExist = database.IsItem(createdItemID);
        assertTrue("IsItem() returned false.", doesItemExist);
    }

    @Test
    public void testIsNonExistingItem() {

        String itemClass = "testClass";
        DatabaseAPI database = DatabaseHandler.Get();

        int createdItemID = database.AddItem(itemClass);
        assertTrue("Couldn't create item.", createdItemID >= 0);

        boolean doesItemExist = database.IsItem(createdItemID + 1);
        assertTrue("IsItem() returned true.", doesItemExist);
    }

    @Test
    public void testRemoveItem() {

        String itemClass = "testClass";
        DatabaseAPI database = DatabaseHandler.Get();

        int createdItemID = database.AddItem(itemClass);
        assertTrue("Couldn't create item.", createdItemID >= 0);

        boolean isItemRemoved = database.RemoveItem(createdItemID);
        assertTrue("RemoveItem() returned false.", isItemRemoved);
    }

    @Test
    public void testRemoveNonExistingItem() {

        String itemClass = "testClass";
        DatabaseAPI database = DatabaseHandler.Get();

        int createdItemID = database.AddItem(itemClass);
        assertTrue("Couldn't create item.", createdItemID >= 0);

        boolean isItemRemoved = database.IsItem(createdItemID + 1);
        assertTrue("RemoveItem() returned true.", isItemRemoved);
    }

    @Test
    public void testPasswordAuthenticate() {

        DatabaseAPI database = DatabaseHandler.Get();

        String userName = "testNickname";
        String password = "testPassword";
        User testUser = database.GetUser(userName);

        if (testUser == null) {
            testUser = database.AddUser(userName, password);
            assertNotNull("Couldn't create user", testUser);
        }

        User authenticadedUser = database.AuthenticateUser(userName, password);
        assertNotNull("User returned with AuthenticateUser(...) is null");
        assertTrue("AuthenticateUser() didn't returned the exact same user as expected", authenticadedUser.equals(testUser));
    }

    @Test
    public void testWrongPasswordAuthenticate() {

        DatabaseAPI database = DatabaseHandler.Get();

        String userName = "testNickname";
        String password = "testPassword";
        String wrongPassword = "wrongPwd";
        User testUser = database.GetUser(userName);

        if (testUser == null) {
            testUser = database.AddUser(userName, password);
            assertNotNull("Couldn't create user", testUser);
        }

        User authenticadedUser = database.AuthenticateUser(userName, wrongPassword);
        assertNull("AuthenticateUser() returned user, even when wrong password was given", authenticadedUser);
    }

    @Test
    public void testPasswordAuthenticateWithoutExistingUser() {

        DatabaseAPI database = DatabaseHandler.Get();

        String userName = "testNickname";
        String password = "testPassword";
        User testUser = database.GetUser(userName);

        if (testUser != null) {
            boolean removeUserResult = database.RemoveUser(userName);
            assertTrue("Couldn't remove user", removeUserResult);
        }

        User authenticadedUser = database.AuthenticateUser(userName, password);
        assertNull("AuthenticateUser() returned not null, even when user isn't existing", authenticadedUser);

    }

    @Test
    public void testHeaderAuthenticate() {

        DatabaseAPI database = DatabaseHandler.Get();

        String userName = "testNickname";
        String password = "testPassword";
        User testUser = database.GetUser(userName);

        if (testUser == null) {
            testUser = database.AddUser(userName, password);
            assertNotNull("Couldn't create user", testUser);
        }

        StubPlatformMessageHeader testedHeader = new StubPlatformMessageHeader();
        testedHeader.SetUserIdentifier("testID");
        database.RemoveTrustedPlatformFromUser(testedHeader, userName);
        boolean addTrustedPlatformResult = database.AddTrustedPlatformToUser(testedHeader, userName);
        assertTrue("Couldn't add trusted platform", addTrustedPlatformResult);

        User authenticadedUser = database.AuthenticateUser(userName, testedHeader);
        assertNotNull("User returned with AuthenticateUser(...) is null", authenticadedUser);
        assertTrue("AuthenticateUser() didn't returned the exact same user as expected", authenticadedUser.equals(testUser));
    }

    @Test
    public void testWrongHeaderAuthenticate() {

        DatabaseAPI database = DatabaseHandler.Get();

        String userName = "testNickname";
        String password = "testPassword";
        User testUser = database.GetUser(userName);

        if (testUser == null) {
            testUser = database.AddUser(userName, password);
            assertNotNull("Couldn't create user", testUser);
        }

        StubPlatformMessageHeader testedHeader = new StubPlatformMessageHeader();
        testedHeader.SetUserIdentifier("testID");
        database.RemoveTrustedPlatformFromUser(testedHeader, userName);

        User authenticadedUser = database.AuthenticateUser(userName, testedHeader);
        assertNull("User returned with AuthenticateUser(...) is not null, even if selected header isn't the trusted one", authenticadedUser);
    }

    @Test
    public void testHeaderAuthenticateWhenPlatformDoesntSupportIt() {

        DatabaseAPI database = DatabaseHandler.Get();

        String userName = "testNickname";
        String password = "testPassword";
        User testUser = database.GetUser(userName);

        if (testUser == null) {
            testUser = database.AddUser(userName, password);
            assertNotNull("Couldn't create user", testUser);
        }

        StubPlatformMessageHeader testedHeader = new StubPlatformMessageHeader();
        testedHeader.SetUserIdentifier("testID");
        database.RemoveTrustedPlatformFromUser(testedHeader, userName);
        boolean addTrustedPlatformResult = database.AddTrustedPlatformToUser(testedHeader, userName);
        assertTrue("Couldn't add trusted platform", addTrustedPlatformResult);

        testedHeader.setTrustedLogin(false);
        User authenticadedUser = database.AuthenticateUser(userName, testedHeader);
        assertNull("User was returned with AuthenticateUser(...), when header trusted login was set to false.", authenticadedUser);
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
    public void testItemStringParamFirstTimeSet()
    {
        String testParamViaGetParam = (String)SetParameterFirstTimeAndReturnIt(testedItemOwner, testedStringParam);
        assertEquals("Param value got via GetParam() isn't equal to the one used in SetParam()", testStringParamValue,testParamViaGetParam);
    }

    @Test
    public void testItemStringParamSetAgain()
    {
        String secondTestStringParamValue = "testStringParamValue";
        StringParam secondTestParam = new StringParam("secondTestStringParamName",secondTestStringParamValue);

        String testParamViaGetParam = (String)testSetParameterAgainAndReturnIt(testedItemOwner, testedStringParam,secondTestParam);
        assertEquals("Param value got via GetParam() isn't equal to the one used in SetParam()",testStringParamValue,testParamViaGetParam);
    }

    @Test
    public void testItemStringParamRemove()
    {
        testRemoveParameter(testedItemOwner,testedStringParam);
    }

    @Test
    public void testRemoveNonExistingItemStringParam()
    {
        testRemoveNonExistingParameter(testedItemOwner,testedStringParam);
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
    public void testItemDoubleParamFirstTimeSet()
    {
        double testParamViaGetParam = (Double)SetParameterFirstTimeAndReturnIt(testedItemOwner, testedDoubleParam);
        assertEquals("Param value got via GetParam() isn't equal to the one used in SetParam()", testDoubleParamValue,testParamViaGetParam,0.0001);
    }

    @Test
    public void testItemDoubleParamSetAgain()
    {
        double secondTestDoubleParamValue = 7.142;
        DoubleParam secondTestParam = new DoubleParam("secondTestDoubleParamName",secondTestDoubleParamValue);

        double testParamViaGetParam = (Double)testSetParameterAgainAndReturnIt(testedItemOwner, testedDoubleParam,secondTestParam);
        assertEquals("Param value got via GetParam() isn't equal to the one used in SetParam()",testDoubleParamValue,testParamViaGetParam,0.0001);
    }

    @Test
    public void testItemDoubleParamRemove()
    {
        testRemoveParameter(testedItemOwner,testedDoubleParam);
    }

    @Test
    public void testRemoveNonExistingItemDoubleParam()
    {
        testRemoveNonExistingParameter(testedItemOwner,testedDoubleParam);
    }

    private void InitDatabase() {
        MainConfig.SetConfig();

        testItem = new TestItem(6);
        testedItemOwner = new ItemParam(testItem);
    }

    private User CreateTestUser() {
        User testUser;
        DatabaseAPI database = DatabaseHandler.Get();

        testUser = database.GetUser(testUserUsername);
        if (testUser == null) {
            testUser = database.AddUser(testUserUsername, testUserPassword);
            assertNotNull("Adding user failed.", testUser);
        }
        return testUser;
    }

    private void RemoveTestUser() {
        User testUser;
        DatabaseAPI database = DatabaseHandler.Get();

        testUser = database.GetUser(testUserUsername);
        if (testUser != null) {
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

    private final User testUser = UserBuilder.ObsoleteBuild(4,"testUser");
    private final String testStringParamValue = "testStringParamValue";
    private final UserParam testedUserOwner = new UserParam(testUser);
    private final StringParam testedStringParam = new StringParam("testStringParamName",testStringParamValue);

    private Item testItem;
    private final double testDoubleParamValue = 14.2;
    private ItemParam testedItemOwner;
    private final DoubleParam testedDoubleParam = new DoubleParam("testDoubleParamName",testDoubleParamValue);
}
