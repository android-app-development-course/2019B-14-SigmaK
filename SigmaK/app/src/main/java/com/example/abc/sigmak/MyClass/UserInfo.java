package com.example.abc.sigmak.MyClass;

import android.graphics.Bitmap;

import java.io.Serializable;

/*
用户状态信息
 */
public class UserInfo  implements Serializable {
    public int Follows;
    public int Followers;
    public int Coins;
    public int Likes;

    public UserInfo() {
        Follows = 0;
        Followers = 0;
        Coins = 0;
        Likes = 0;
    }

    public UserInfo(int follows, int followers, int coins, int likes) {
        Follows = follows;
        Followers = followers;
        Coins = coins;
        Likes = likes;
    }

    public void SetUserInfo(int follows, int followers, int coins, int likes) {
        Follows = follows;
        Followers = followers;
        Coins = coins;
        Likes = likes;
    }
}
