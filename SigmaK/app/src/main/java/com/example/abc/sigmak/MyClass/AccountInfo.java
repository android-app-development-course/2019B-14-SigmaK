package com.example.abc.sigmak.MyClass;

import android.graphics.Bitmap;

import com.example.abc.sigmak.Utility.Tools;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;
import org.json.JSONTokener;

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

    public AccountInfo(String JsonText) {
        try {
            JSONTokener jsonParser = new JSONTokener(JsonText);
            JSONObject jo = (JSONObject) jsonParser.nextValue();
            UserID = jo.getInt("UserID");
            Name = jo.getString("Name");
            Email = jo.getString("Email");
            ProfilePhoto = Tools.StringToBitmap(jo.getString("ProfilePhoto"));
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
            jsonText.key("UserID");
            jsonText.value(UserID);
            jsonText.key("Name");
            jsonText.value(Name);
            jsonText.key("Email");
            jsonText.value(Email);
            jsonText.key("ProfilePhoto");
            jsonText.value(Tools.BitmapToString(ProfilePhoto));
            jsonText.endObject();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return jsonText.toString();
    }
}