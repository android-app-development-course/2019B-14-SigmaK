package com.example.abc.sigmak.MyClass;

import com.example.abc.sigmak.Utility.Tools;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;
import org.json.JSONTokener;

import java.util.Date;

public class Answer extends Post {
    public int QuestionID;

    public Answer(int ID, String title, PostType type, Date postDate, Date lastEditedDate, PostCategory category, int authorID, int likes, int reads, int comments, String[] keyWords, int questionID) {
        super(ID, title, type, postDate, lastEditedDate, category, authorID, likes, reads, comments, keyWords);
        QuestionID = questionID;
    }

    public Answer(int ID, String title, PostType type,
                  Date postDate, Date lastEditedDate,
                  PostCategory category, int authorID,
                  int likes, int reads, int comments,
                  String[] keyWords) {
        super(ID, title, type, postDate, lastEditedDate, category, authorID, likes, reads, comments, keyWords);
        QuestionID = -1;
    }


//    public AccountInfo(String JsonText) {
//        UserID = -1;
//        Name = null;
//        Email = null;
//        ProfilePhoto = null;
//        try {
//            JSONTokener jsonParser = new JSONTokener(JsonText);
//            JSONObject jo = (JSONObject) jsonParser.nextValue();
//            UserID = jo.getInt("UserID");
//            Name = jo.getString("Name");
//            Email = jo.getString("Email");
//            ProfilePhoto = Tools.StringToBitmap("ProfilePhoto");
//        } catch (JSONException ex) {
//            // 异常处理代码
//        }catch (Exception ex){
//            ex.printStackTrace();
//        }
//    }
//
//    public String toJsonString(){
//        JSONStringer jsonText = new JSONStringer();
//        try {
//            jsonText.object();
//            jsonText.key("UserID");
//            jsonText.value(UserID);
//            jsonText.key("Name");
//            jsonText.value(Name);
//            jsonText.key("Email");
//            jsonText.value(Email);
//            jsonText.key("ProfilePhoto");
//            jsonText.value(Tools.BitmapToString(ProfilePhoto));
//            jsonText.endObject();
//        }
//        catch (Exception ex){
//            ex.printStackTrace();
//        }
//        return jsonText.toString();
//    }
}
