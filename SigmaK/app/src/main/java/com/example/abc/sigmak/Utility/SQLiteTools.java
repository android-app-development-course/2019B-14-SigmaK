package com.example.abc.sigmak.Utility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

public class SQLiteTools extends SQLiteOpenHelper {

    private static final String DATABASE_NAMEwithoutDB = "SigmaK_v1_11";
    private static final String DATABASE_NAME = "SigmaK_v1_11.db";
    private static final int DATABASE_VERSION = 1;
    private static SQLiteTools instance;
    public static final String TAG="SQLiteTools";

    private SQLiteTools(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public synchronized void close() {
        super.close();
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
        db.execSQL("CREATE TABLE Account(" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "Email VARCHAR(30) NOT NULL UNIQUE," +
                "UserName VARCHAR(20) NOT NULL UNIQUE," +
                "Password VARCHAR(40) NOT NULL," +
                "ProfilePhoto TEXT NOT NULL) ");
        db.execSQL("CREATE TABLE UserInfo(" +
                "UserInfoID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "AccountID INTEGER NOT NULL UNIQUE," +
                "Follows INTEGER DEFAULT 0 NOT NULL," +
                "Followers INTEGER DEFAULT 0 NOT NULL," +
                "Coins INTEGER DEFAULT 0 NOT NULL," +
                "Likes INTEGER DEFAULT 0 NOT NULL," +
                "FOREIGN KEY (AccountID) references Account(ID) ON DELETE CASCADE) ");
        db.execSQL("CREATE TABLE PostInfo(" +
                "PostID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "Title TEXT NOT NULL," +
                "Type VARCHAR(10) NOT NULL," +
                "PostDate TIMESTAMP DEFAULT (datetime('now', 'localtime')) NOT NULL," +
                "LastEditedDate TIMESTAMP DEFAULT (datetime('now', 'localtime')) NOT NULL," +
                "Category VARCHAR(40) NOT NULL," +
                "KeyWords TEXT NOT NULL," +
                "AuthorID INTEGER NOT NULL," +
                "Likes INTEGER DEFAULT 0 NOT NULL," +
                "Reads INTEGER DEFAULT 0 NOT NULL," +
                "Comments INTEGER DEFAULT 0 NOT NULL," +
                "FOREIGN KEY (AuthorID) references Account(ID)) ");
        db.execSQL("CREATE TABLE Favourites(" +
                "FavouritesRecordID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "UserID INTEGER NOT NULL," +
                "PostID INTEGER NOT NULL," +
                "TYPE VARCHAR(10) NOT NULL," +
                "FOREIGN KEY (UserID) references Account(ID) ON DELETE CASCADE," +
                "FOREIGN KEY (PostID) references PostInfo(PostID) ON DELETE CASCADE) ");
        db.execSQL("CREATE TABLE Follow(" +
                "PK_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "UserID INTEGER NOT NULL," +
                "FollowerID INTEGER NOT NULL," +
                "FollowDate TIMESTAMP DEFAULT (datetime('now', 'localtime')) NOT NULL," +
                "LastEditedDate TIMESTAMP DEFAULT (datetime('now', 'localtime')) NOT NULL," +
                "FOREIGN KEY (UserID) references Account(ID) ON DELETE CASCADE," +
                "FOREIGN KEY (FollowerID) references Account(ID) ON DELETE CASCADE) ");
        db.execSQL("CREATE TABLE PostContent(" +
                "ContentID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "PostID INTEGER NOT NULL," +
                "TextContent TEXT NOT NULL," +
                "FOREIGN KEY (PostID) references PostInfo(PostID) ON DELETE CASCADE) ");
        db.execSQL("CREATE TABLE Comment(" +
                "CommentID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "PostID INTEGER NOT NULL," +
                "PostDate TIMESTAMP DEFAULT (datetime('now', 'localtime')) NOT NULL," +
                "Likes INTEGER DEFAULT 0 NOT NULL," +
                "Content TEXT NOT NULL," +
                "FOREIGN KEY (PostID) references PostInfo(PostID) ON DELETE CASCADE) ");
        db.execSQL("CREATE TABLE AnswerInfo(" +
                "PK_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "QuestionID INTEGER NOT NULL," +
                "AnswerID INTEGER NOT NULL," +
                "FOREIGN KEY (QuestionID) references PostInfo(PostID) ON DELETE CASCADE) ");
        db.execSQL("CREATE TABLE QuestionInfo(" +
                "PK_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "PostID INTEGER NOT NULL," +
                "Answers INTEGER DEFAULT 0 NOT NULL," +
                "Status VARCHAR(20) DEFAULT 'NoFinished' NOT NULL," +
                "StatisfiedAnswerIDs TEXT DEFAULT '-1' NOT NULL," +
                "FOREIGN KEY (PostID) references PostInfo(PostID) ON DELETE CASCADE) ");
        db.execSQL("CREATE TABLE Likes(" +
                "PK_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "PostID INTEGER NOT NULL," +
                "UserID INTEGER NOT NULL," +
                "FOREIGN KEY (PostID) references PostInfo(PostID) ON DELETE CASCADE," +
                "FOREIGN KEY (UserID) references Account(ID) ON DELETE CASCADE)");
        db.execSQL("CREATE TABLE Disapproves(" +
                "PK_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "PostID INTEGER NOT NULL," +
                "UserID INTEGER NOT NULL," +
                "FOREIGN KEY (PostID) references PostInfo(PostID) ON DELETE CASCADE," +
                "FOREIGN KEY (UserID) references Account(ID) ON DELETE CASCADE) ");
        db.execSQL("CREATE TABLE UserInterest(" +
                "PK_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "UserID INTEGER NOT NULL UNIQUE," +
                "Category VARCHAR(20) NOT NULL," +
                "KeyWords TEXT DEFAULT 'empty' NOT NULL," +
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
        try(SQLiteDatabase db = instance.getWritableDatabase()) {
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
        //if(instance==null)
           // getInstance();
        SQLiteDatabase db = null;
        long retvalue = 0;
        try{
            db = instance.getWritableDatabase();
            //retvalue = db.insertWithOnConflict(TableName, null, Values, CONFLICT_REPLACE);
            retvalue = db.insert(TableName, null, Values);
        }catch(Exception ex){
            Log.i(TAG, ex.getMessage());
        }
        finally {
            db.close();
        }
        return retvalue;
    }

    public String DateBaseName(){
        return DATABASE_NAMEwithoutDB;
    }
}
