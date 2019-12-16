package com.example.abc.sigmak.MyClass;

import java.io.Serializable;
import java.util.Date;

public class Comment implements Serializable {
    public int ID;
    public int PostID;
    public int UserID;
    public Date PostDate;
    public int Likes;
    public char[] Content;//最长200中文字符

    public Comment(int ID, int postID, int userID, Date postDate, int likes, char[] content) {
        this.ID = ID;
        PostID = postID;
        UserID = userID;
        PostDate = postDate;
        Likes = likes;
        Content = content;
    }
}
