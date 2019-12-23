package com.example.abc.sigmak.MyClass;

import com.example.abc.sigmak.Utility.Tools;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;
import org.json.JSONTokener;

import java.util.Date;

public class Question extends Post {


    public Question(int ID, String title, PostType type,
                    Date postDate, Date lastEditedDate,
                    PostCategory category, int authorID,
                    int likes, int reads, int comments,
                    String[] keyWords, int answers, QuestionStatus status, Integer[] statisfiedAnswerID) {
        super(ID, title, type, postDate, lastEditedDate, category, authorID, likes, reads, comments, keyWords);
        Answers = answers;
        Status = status;
        StatisfiedAnswerIDs = statisfiedAnswerID;
    }

    public Question(int ID, String title, PostType type,
                    Date postDate, Date lastEditedDate,
                    PostCategory category, int authorID,
                    int likes, int reads, int comments,
                    String[] keyWords) {
        super(ID, title, type, postDate, lastEditedDate, category, authorID, likes, reads, comments, keyWords);
        Answers = 0;
        Status = QuestionStatus.NoFinished;
        StatisfiedAnswerIDs = null;
    }

    public enum QuestionStatus{
        NoFinished,Closed/*无满意回答*/,Accepted//有满意回答
    }


    public int Answers;
    public QuestionStatus Status;
    public Integer[] StatisfiedAnswerIDs;

    public Question(String JsonText) {
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
            Answers = jo.getInt("Answers");
            Status = QuestionStatus.valueOf(jo.getString("Status"));
            StatisfiedAnswerIDs = Tools.CombinedStringToIntegerArray((jo.getString("StatisfiedAnswerIDs")));
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
            jsonText.key("Answers");
            jsonText.value(Answers);
            jsonText.key("Status");
            jsonText.value(Status.name());
            jsonText.key("StatisfiedAnswerIDs");
            jsonText.value(Tools.IntArrayToString(StatisfiedAnswerIDs));
            jsonText.endObject();

        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return jsonText.toString();
    }
}
