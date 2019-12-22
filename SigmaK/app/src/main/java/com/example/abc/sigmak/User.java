package com.example.abc.sigmak;

import android.graphics.Bitmap;

/**
 * Created by abc on 2019/12/21.
 */

public class User {


    int userid;
    Bitmap photo;
    String name;
    boolean postion=false;//ture  已关注 false 未关注
    User(int id, Bitmap bitmap, String Name)
    {
        userid=id;
        photo=bitmap;
        name=Name;
    }
    User(int id, Bitmap bitmap, String Name, boolean status)
    {
        userid=id;
        photo=bitmap;
        name=Name;
        postion=status;
    }

}
