package Core.Database.Impls.SQL;

import Core.Database.Impls.SQL.Connection.SQLSelectedServer;
import Core.Database.Impls.SQL.Param.SQLParam;
import Core.Database.Impls.SQL.Param.SQLParamOwner;
import Core.Log.Logger;

import java.sql.*;

public class MySQLCommon {

    static String HashPassword(String pwd)
    {
        return pwd;
    }

    private static String GetParamTableName(SQLParamOwner owner, SQLParam param)
    {
        String tableName = owner.GetSQLTableNameSubstring()
                + param.GetSQLTableNameSubstring()
                + "Params";

        return tableName;
    }

    static boolean SetTableParam(SQLParamOwner owner, SQLParam param) {

        if(GetParamTableName(owner,param) != null)
        {
            RemoveTableParam(owner, param);
        }

        try {

            Connection con = SQLSelectedServer.getConnection();
            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String tableName = GetParamTableName(owner,param);
            ResultSet uprs = stmt.executeQuery("SELECT * FROM "+tableName+";");

            uprs.moveToInsertRow();
            uprs.updateInt(owner.GetSQLColumnNameOfOwnerID(),owner.GetOwnerID());
            param.PrepareRowToBeInserted(uprs);
            uprs.insertRow();

            uprs.close();
            stmt.close();
            con.close();

            return true;

        }
        catch (SQLException e) {
            Logger.Log(e.getMessage(),
                    "error","SQL");
            return false;
        }

    }

    static Object GetTableParam(SQLParamOwner owner, SQLParam param) {

        try {

            Logger.Log("GetTableParam: pre GetConnection()");
            Connection con = SQLSelectedServer.getConnection();
            Logger.Log("GetTableParam: after GetConnection()");
            Statement stmt = con.createStatement();
            String tableName = GetParamTableName(owner,param);
            String query = "SELECT * FROM "+tableName+" WHERE "+owner.GetSQLColumnNameOfOwnerID()+"=" + owner.GetOwnerID() + " AND paramName='"+param.GetParamName()+"';";
            Logger.Log(query,"SQL","query","sql2 command");
            ResultSet uprs = stmt.executeQuery(query);

            if(!uprs.first())
            {
                return null;
            }

            Object result = param.ExtractParamFromSelectedRow(uprs);

            uprs.close();
            stmt.close();
            con.close();

            return result;

        }
        catch (SQLException e) {
            Logger.Log(e.getMessage(),
                    "error","SQL");
            return false;
        }
    }

    static boolean RemoveTableParam(SQLParamOwner owner, SQLParam param) {

        if (GetTableParam(owner,param) == null)
        {
            return false;
        }

        try {

            Connection con = SQLSelectedServer.getConnection();
            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String tableName = GetParamTableName(owner,param);
            ResultSet uprs = stmt.executeQuery("SELECT * FROM "+tableName+" WHERE "+owner.GetSQLColumnNameOfOwnerID()+"=" + owner.GetOwnerID() + " AND paramName='"+param.GetParamName()+"';");

            uprs.first();
            uprs.deleteRow();

            uprs.close();
            stmt.close();
            con.close();

            return true;

        }
        catch (SQLException e) {
            Logger.Log(e.getMessage(),
                    "error","SQL");
            return false;
        }
    }

    private static boolean TableExists(String tableName)
    {
        try {

            Connection con = SQLSelectedServer.getConnection();
            Statement stmt = con.createStatement();
            ResultSet uprs = stmt.executeQuery("SELECT * FROM "+tableName+";");

            uprs.close();
            stmt.close();
            con.close();

            return true;

        }
        catch (SQLException e) {
            Logger.Log("TableExists("+tableName+") exteption: "+e.getMessage(),
                    "error","SQL");
            return false;
        }
    }

    private static boolean CreateTable(Table table)
    {
        try {

            Logger.Log("GetTableParam: pre GetConnection()");
            Connection con = SQLSelectedServer.getConnection();
            Logger.Log("GetTableParam: after GetConnection()");
            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            stmt.executeUpdate("create table "+table.name+" ("+table.createTableParameters+");");

            stmt.close();
            con.close();

            return true;

        }
        catch (SQLException e) {
            Logger.Log(e.getMessage(),
                    "error","SQL");
            return false;
        }
    }

    public static boolean PrepareDatabase()
    {
        String[] baseTableInits = {"usrs","id int NOT NULL AUTO_INCREMENT, nickname varchar(32), hashedPwd varchar(255), PRIMARY KEY (id)",
                                   "items","id int NOT NULL AUTO_INCREMENT, className varchar(32), PRIMARY KEY (id)",
                                   "usrsDoubleParams", "id int NOT NULL AUTO_INCREMENT, usrId int, paramName varchar(32), paramVal float, PRIMARY KEY (id)",
                                   "itemsDoubleParams", "id int NOT NULL AUTO_INCREMENT, itemId int, paramName varchar(32), paramVal float, PRIMARY KEY (id)",
                                   "usrsStringParams", "id int NOT NULL AUTO_INCREMENT, usrId int, paramName varchar(32), paramVal varchar(32), PRIMARY KEY (id)",
                                   "itemsStringParams", "id int NOT NULL AUTO_INCREMENT, itemId int, paramName varchar(32), paramVal varchar(32), PRIMARY KEY (id)",
                                   "platforms", "id int NOT NULL AUTO_INCREMENT, name varchar(32), PRIMARY KEY (id)",
                                   "trustedPlatforms","platformID int, headerID varchar(64), userID int, CONSTRAINT PKTrustedPlatforms PRIMARY KEY (platformID,headerID,userID)",
                                   "usrsInChatrooms","id int NOT NULL AUTO_INCREMENT, chatroomID int, userID int, PRIMARY KEY (id)",
                                   "gameInstances","id int NOT NULL AUTO_INCREMENT, name varchar(32), hashedPwd varchar(255), PRIMARY KEY (id)"};

        Table[] baseTables = new Table[baseTableInits.length/2];
        for(int i=0;i<baseTables.length;i++)
        {
            baseTables[i] = new Table(baseTableInits[i*2],baseTableInits[i*2+1]);
        }

        boolean prepareSuccess = true;

        Logger.Log("");
        for (Table table:baseTables) {
            if(!TableExists(table.name))
            {
                Logger.Log(table.name + " doesn't exists","databasePrep");
                if(!CreateTable(table))
                {
                    Logger.Log(table.name + " creation failed","databasePrep");
                    prepareSuccess = false;
                }
                else
                {
                    Logger.Log(table.name + " created successfully","databasePrep");
                }
            }
            else
            {
                Logger.Log(table.name + " exists already","databasePrep");
            }
        }
        Logger.Log("");

        return prepareSuccess;
    }

    private static class Table
    {
        public Table(String name, String createTableParameters) {
            this.name = name;
            this.createTableParameters = createTableParameters;
        }

        public String name;
        public String createTableParameters;
    }

}
