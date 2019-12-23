package com.example.abc.sigmak.Utility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import java.util.LinkedList;
import java.util.List;

public class SQLiteTools extends SQLiteOpenHelper {

    private static final String DATABASE_NAMEwithoutDB = "SigmaK";
    private static final String DATABASE_NAME = "SigmaK.db";
    private static final int DATABASE_VERSION = 1;
    private static SQLiteTools instance;

    private SQLiteTools(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public static SQLiteTools getInstance(Context context) {
        if (null == instance) {
            synchronized (SQLiteTools.class) {
                if (null == instance) {
                    instance = new SQLiteTools(context, DATABASE_NAME, null, DATABASE_VERSION);
                }
            }
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        db.execSQL("CREATE TABLE Account(ID INTEGER PRIMARY KEY AUTOINCREMENT, Email VARCHAR(30) NOT NULL UNIQUE" +
//                ", UserName VARCHAR(20) NOT NULL UNIQUE, Password VARCHAR(40) NOT NULL, ProfilePhoto TEXT NOT NULL)");
        db.execSQL("CREATE TABLE Account(\n" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "Email VARCHAR(30) NOT NULL UNIQUE,\n" +
                "UserName VARCHAR(20) NOT NULL UNIQUE,\n" +
                "Password VARCHAR(40) NOT NULL,\n" +
                "ProfilePhoto TEXT NOT NULL)\n" +
                "\n" +
                "CREATE TABLE UserInfo(\n" +
                "UserInfoID INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "AccountID INTEGER NOT NULL,\n" +
                "Follows INTEGER DEFAULT 0 NOT NULL,\n" +
                "Followers INTEGER DEFAULT 0 NOT NULL,\n" +
                "Coins INTEGER DEFAULT 0 NOT NULL,\n" +
                "Likes INTEGER DEFAULT 0 NOT NULL,\n" +
                "FOREIGN KEY (AccountID) references Account(ID) ON DELETE CASCADE)\n" +
                "\n" +
                "CREATE TABLE PostInfo(\n" +
                "PostID INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "Title TEXT NOT NULL,\n" +
                "Type VARCHAR(10) NOT NULL,\n" +
                "PostDate TIMESTAMP DEFAULT (datetime('now', 'localtime')) NOT NULL,\n" +
                "LastEditedDate TIMESTAMP DEFAULT (datetime('now', 'localtime')) NOT NULL,\n" +
                "Category VARCHAR(40) NOT NULL,\n" +
                "KeyWords TEXT NOT NULL,\n" +
                "AuthorID INTEGER NOT NULL,\n" +
                "Likes INTEGER DEFAULT 0 NOT NULL,\n" +
                "Reads INTEGER DEFAULT 0 NOT NULL,\n" +
                "Comments INTEGER DEFAULT 0 NOT NULL,\n" +
                "FOREIGN KEY (AuthorID) references Account(ID),)\n" +
                "\n" +
                "CREATE TABLE Favourites(\n" +
                "FavouritesRecordID INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "UserID INTEGER NOT NULL,\n" +
                "PostID INTEGER NOT NULL,\n" +
                "TYPE VARCHAR(10) NOT NULL,\n" +
                "FOREIGN KEY (UserID) references Account(ID) ON DELETE CASCADE,\n" +
                "FOREIGN KEY (PostID) references PostInfo(PostID) ON DELETE CASCADE)\n" +
                "\n" +
                "CREATE TABLE Follow(\n" +
                "PK_id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "UserID INTEGER NOT NULL,\n" +
                "FollowerID INTEGER NOT NULL,\n" +
                "FollowDate TIMESTAMP DEFAULT (datetime('now', 'localtime')) NOT NULL,\n" +
                "LastEditedDate TIMESTAMP DEFAULT (datetime('now', 'localtime')) NOT NULL,\n" +
                "FOREIGN KEY (UserID) references Account(ID) ON DELETE CASCADE,\n" +
                "FOREIGN KEY (FollowerID) references Account(ID) ON DELETE CASCADE)\n" +
                "\n" +
                "CREATE TABLE PostContent(\n" +
                "ContentID INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "PostID INTEGER NOT NULL,\n" +
                "TextContent TEXT NOT NULL,\n" +
                "FOREIGN KEY (PostID) references PostInfo(PostID) ON DELETE CASCADE)\n" +
                "\n" +
                "CREATE TABLE Comment(\n" +
                "CommentID INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "PostID INTEGER NOT NULL,\n" +
                "PostDate TIMESTAMP DEFAULT (datetime('now', 'localtime')) NOT NULL,\n" +
                "Likes INTEGER DEFAULT 0 NOT NULL,\n" +
                "Content TEXT NOT NULL,\n" +
                "FOREIGN KEY (PostID) references PostInfo(PostID) ON DELETE CASCADE)\n" +
                "\n" +
                "CREATE TABLE AnswerInfo(\n" +
                "PK_id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "QuestionID INTEGER NOT NULL,\n" +
                "AnswerID INTEGER NOT NULL,\n" +
                "FOREIGN KEY (QuestionID) references PostInfo(PostID) ON DELETE CASCADE)\n" +
                "\n" +
                "\n" +
                "CREATE TABLE QuestionInfo(\n" +
                "PK_id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "PostID INTEGER NOT NULL,\n" +
                "Answers INTEGER DEFAULT 0 NOT NULL,\n" +
                "Status VARCHAR(20) DEFAULT 'NoFinished' NOT NULL,\n" +
                "StatisfiedAnswerIDs TEXT DEFAULT '-1' NOT NULL,\n" +
                "FOREIGN KEY (PostID) references PostInfo(PostID) ON DELETE CASCADE)\n" +
                "\n" +
                "CREATE TABLE Likes(\n" +
                "PK_id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "PostID INTEGER NOT NULL,\n" +
                "UserID INTEGER NOT NULL,\n" +
                "FOREIGN KEY (PostID) references PostInfo(PostID) ON DELETE CASCADE,\n" +
                "FOREIGN KEY (UserID) references Account(ID) ON DELETE CASCADE)\n" +
                "\n" +
                "CREATE TABLE Disapproves(\n" +
                "PK_id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "PostID INTEGER NOT NULL,\n" +
                "UserID INTEGER NOT NULL,\n" +
                "FOREIGN KEY (PostID) references PostInfo(PostID) ON DELETE CASCADE,\n" +
                "FOREIGN KEY (UserID) references Account(ID) ON DELETE CASCADE)\n" +
                "\n" +
                "CREATE TABLE UserInterest(\n" +
                "PK_id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "UserID INTEGER NOT NULL,\n" +
                "Category VARCHAR(20) NOT NULL,\n" +
                "KeyWords TEXT DEFAULT 'empty' NOT NULL,\n" +
                "FOREIGN KEY (UserID) references Account(ID) ON DELETE CASCADE)");
        //CHECK:这里不用close吗
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

//    /**
//     * 执行SQL语句的查询并返回得到的结果，全部字段以字符串的形式放在LinkedList里面返回
//     * !!!!要加null的检查！！！！有可能返回为null
//     * @param Command Sql命令
//     * @return 全部字段以字符串的形式放在LinkedList里面返回
//     */
//    public List<Object> rawQuery(String Command){
//        List<Object> list = new LinkedList<Object>();
//
//        try (SQLiteDatabase db = instance.getReadableDatabase();
//             Cursor cursor = db.rawQuery(Command,null)) {
//            if(cursor.getCount()==0)
//                list = null;
//            cursor.moveToFirst();
//            int colNum = cursor.getColumnCount();
//            do{
//                for(int i=0;i<colNum;++i){
//                    list.add(cursor.getString(i));
//                }
//            }while (cursor.moveToNext());
//        }catch (Exception ex){
//            ex.printStackTrace();
//        }
//        return list;
//    }

    /**
     * 执行SQL语句的查询并返回得到的结果，全部字段以字符串的形式放在LinkedList里面返回
     * !!!!要加null的检查！！！！有可能返回为null
     * @param Command Sql命令
     * @return 全部字段以字符串的形式放在LinkedList里面返回
     */
    public List<String> QueryString(String Command){
        List<String> list = new LinkedList<String>();

        try (SQLiteDatabase db = instance.getReadableDatabase();
             Cursor cursor = db.rawQuery(Command,null)) {
            if(cursor.getCount()==0)
                list = null;
            cursor.moveToFirst();
            int colNum = cursor.getColumnCount();
            do{
                for(int i=0;i<colNum;++i){
                    list.add(cursor.getString(i).trim());
                }
            }while (cursor.moveToNext());
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return list;
    }

    /**
     * 执行SQL语句的查询并返回得到的结果，全部字段以字符串的形式放在LinkedList里面返回
     * !!!!要加null的检查！！！！有可能返回为null
     * @param Command Sql命令
     * @return 全部字段以字符串的形式放在LinkedList里面返回
     */
    public List<Integer> QueryInt(String Command){
        List<Integer> list = new LinkedList<Integer>();

        try (SQLiteDatabase db = instance.getReadableDatabase();
             Cursor cursor = db.rawQuery(Command,null)) {
            if(cursor.getCount()==0)
                list = null;
            cursor.moveToFirst();
            int colNum = cursor.getColumnCount();
            do{
                for(int i=0;i<colNum;++i){
                    list.add(cursor.getInt(i));
                }
            }while (cursor.moveToNext());
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return list;
    }

    public void ExecuteSql(String Command){
        try(SQLiteDatabase db = instance.getReadableDatabase()) {
            toEnableCascadeDelete(db);
            db.execSQL(Command);
        }
    }

    private void toEnableCascadeDelete(SQLiteDatabase db){
        db.execSQL("PRAGMA foreign_keys = ON");
    }

    /**
     *
     *        ContentValues values = new ContentValues();
     *        values.put("_id", id_here);
     *        values.put("text", your_text_here);
     * @param TableName
     * @param Values
     * @return
     */
    public long insert(String TableName, ContentValues Values){
        SQLiteDatabase db = null;
        long retvalue = 0;
        try{
            db = instance.getReadableDatabase();
            //retvalue = db.insertWithOnConflict(TableName, null, Values, CONFLICT_REPLACE);
            retvalue = db.insert(TableName, null, Values);
        }finally {
            db.close();
        }
        return retvalue;
    }

    public String DateBaseName(){
        return DATABASE_NAMEwithoutDB;
    }
}
