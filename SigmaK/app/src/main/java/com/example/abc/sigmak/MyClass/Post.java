package com.example.abc.sigmak.MyClass;

import android.util.Log;

import com.example.abc.sigmak.Utility.Tools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;
import org.json.JSONTokener;

import java.io.Serializable;
import java.util.Date;

public class Post implements Serializable {
    protected Post(){
        super();
    }

    public Post(int ID, String title, PostType type,
                Date postDate, Date lastEditedDate,
                PostCategory category, int authorID,
                int likes, int reads, int comments, String[] keyWords) {
        this.ID = ID;
        Title = title;
        Type = type;
        PostDate = postDate;
        LastEditedDate = lastEditedDate;
        Category = category;
        AuthorID = authorID;
        Likes = likes;
        Reads = reads;
        Comments = comments;
        KeyWords = keyWords;
    }

    public enum PostType{Mix,Question,Blog,Answer,Comment}
    public enum PostCategory{计算机科学,数学科学,文学批评,英语}

//PostID,Title,Type,PostDate,LastEditedDate,Category,KeyWords,AuthorID,Likes,Reads,Comments
    public int ID;
    public String Title;
    public PostType Type;
    public Date PostDate;
    public Date LastEditedDate;
    public PostCategory Category;
    public int AuthorID;
    public int Likes;
    public int Reads;
    public int Comments;
    public String[] KeyWords;

    public Post(String JsonText) {
        try {
            JSONTokener jsonParser = new JSONTokener(JsonText);
            JSONObject jo = (JSONObject) jsonParser.nextValue();
            ID = jo.getInt("ID");
            Title = jo.getString("Title");
            Type = PostType.valueOf(jo.getString("Type"));
            PostDate = Tools.StringToDate(jo.getString("PostDate"));
            LastEditedDate = Tools.StringToDate(jo.getString("LastEditedDate"));
            Category = PostCategory.valueOf(jo.getString("Category"));
            AuthorID = jo.getInt("AuthorID");
            Likes = jo.getInt("Likes");
            Reads = jo.getInt("Reads");
            Comments = jo.getInt("Comment");
            KeyWords = Tools.CombinedStringToStringArray(jo.getString("KeyWrods"));
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
            jsonText.key("Title");
            jsonText.value(Title);
            jsonText.key("Type");
            jsonText.value(Type.name());
            jsonText.key("PostDate");
            jsonText.value(Tools.DateToString(PostDate));
            jsonText.key("LastEditedDate");
            jsonText.value(Tools.DateToString(LastEditedDate));
            jsonText.key("Category");
            jsonText.value(Category.name());
            jsonText.key("AuthorID");
            jsonText.value(AuthorID);
            jsonText.key("Likes");
            jsonText.value(Likes);
            jsonText.key("Reads");
            jsonText.value(Reads);
            jsonText.key("Comments");
            jsonText.value(Comments);
            jsonText.key("KeyWords");
            jsonText.value(Tools.StringArrayToString(KeyWords));
            jsonText.endObject();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return jsonText.toString();
    }
}
//	继承来显示public int Disapproves;//不显示在UI上