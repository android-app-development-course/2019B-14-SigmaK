package com.example.abc.sigmak.Utility;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import java.util.LinkedList;
import java.util.List;

public class SQLiteTools extends SQLiteOpenHelper {

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
        db.execSQL("CREATE TABLE Account(ID INTEGER PRIMARY KEY AUTOINCREMENT, Email VARCHAR(30) NOT NULL UNIQUE" +
                ", UserName VARCHAR(20) NOT NULL UNIQUE, Password VARCHAR(40) NOT NULL, ProfilePhoto TEXT NOT NULL)");
        //CHECK:这里不用close吗
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * 执行SQL语句的查询并返回得到的结果，全部字段以字符串的形式放在LinkedList里面返回
     * !!!!要加null的检查！！！！有可能返回为null
     * @param Command Sql命令
     * @return 全部字段以字符串的形式放在LinkedList里面返回
     */
    public List<Object> rawQuery(String Command){
        List<Object> list = new LinkedList<Object>();

        try (SQLiteDatabase db = instance.getReadableDatabase();
             Cursor cursor = db.rawQuery(Command,null)) {
            if(cursor.getCount()==0)
                list = null;
            cursor.moveToFirst();
            int colNum = cursor.getColumnCount();
            do{
                for(int i=1;i<=colNum;++i){
                    list.add(cursor.getString(i));
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
}
