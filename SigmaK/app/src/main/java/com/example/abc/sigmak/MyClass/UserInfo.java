package com.example.abc.sigmak.MyClass;

import android.graphics.Bitmap;

import com.example.abc.sigmak.Utility.Tools;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;
import org.json.JSONTokener;

import java.io.Serializable;

/*
用户状态信息
 */
public class UserInfo  implements Serializable {
    public int Follows;
    public int Followers;
    public int Coins;
    public int Likes;
    public int Weight;
    public String[] Keywords;

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

    public UserInfo(String JsonText) {
        try {
            JSONTokener jsonParser = new JSONTokener(JsonText);
            JSONObject jo = (JSONObject) jsonParser.nextValue();
            Follows = jo.getInt("Follows");
            Followers = jo.getInt("Followers");
            Coins = jo.getInt("Coins");
            Likes = jo.getInt("Likes");
        } catch (JSONException ex) {
            // 异常处理代码
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public String toJsonString(){
        JSONStringer jsonText = new JSONStringer();
        try {
            jsonText.object();
            jsonText.key("Follows");
            jsonText.value(Follows);
            jsonText.key("Followers");
            jsonText.value(Followers);
            jsonText.key("Coins");
            jsonText.value(Coins);
            jsonText.key("Likes");
            jsonText.value(Likes);
            jsonText.endObject();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return jsonText.toString();
    }
}
