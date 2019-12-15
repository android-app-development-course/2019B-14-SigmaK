package com.example.abc.sigmak.MyClass;

import android.graphics.Bitmap;

/*
账号信息
 */
public class AccountInfo {
    public int UserID;
    public String Name;
    public String Email;
    public Bitmap ProfilePhoto;

    public AccountInfo(int userID, String name, String email, Bitmap profilePhoto) {
        UserID = userID;
        Name = name;
        Email = email;
        ProfilePhoto = profilePhoto;
    }

    public AccountInfo() {
        UserID = -1;
        Name = null;
        Email = null;
        ProfilePhoto = null;
    }

    public void SetAccountInfo(int userID, String name, String email, Bitmap profilePhoto) {
        UserID = userID;
        Name = name;
        Email = email;
        ProfilePhoto = profilePhoto;
    }
}