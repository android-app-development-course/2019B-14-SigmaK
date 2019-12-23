package com.example.abc.sigmak.MyClass;

import com.example.abc.sigmak.Utility.Tools;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;
import org.json.JSONTokener;

import java.io.Serializable;
import java.util.Date;

public class Comment implements Serializable {
    public int ID;
    public int PostID;
    public int UserID;
    public Date PostDate;
    public int Likes;
    public String Content;//最长200中文字符

    public Comment(int ID, int postID, int userID, Date postDate, int likes, String content) {
        this.ID = ID;
        PostID = postID;
        UserID = userID;
        PostDate = postDate;
        Likes = likes;
        Content = content;
    }


    public Comment(String JsonText) {
        try {
            JSONTokener jsonParser = new JSONTokener(JsonText);
            JSONObject jo = (JSONObject) jsonParser.nextValue();
            ID = jo.getInt("UserID");
            PostID = jo.getInt("PostID");
            UserID = jo.getInt("UserID");
            PostDate = Tools.StringToDate(jo.getString("PostDate"));
            Likes = jo.getInt("Likes");
            Content = jo.getString("Content");
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
            jsonText.key("ID");
            jsonText.value(ID);
            jsonText.key("PostID");
            jsonText.value(PostID);
            jsonText.key("UserID");
            jsonText.value(UserID);
            jsonText.key("PostDate");
            jsonText.value(Tools.DateToString(PostDate));
            jsonText.key("Likes");
            jsonText.value(Likes);
            jsonText.key("Content");
            jsonText.value(Content);
            jsonText.endObject();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return jsonText.toString();
    }
}
