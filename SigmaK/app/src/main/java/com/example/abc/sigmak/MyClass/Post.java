package com.example.abc.sigmak.MyClass;

import java.io.Serializable;
import java.util.Date;

public class Post implements Serializable {
    public enum PostType{Mix,Question,Blog}

    public int ID;
    public String Title;
    public Date PostDate;
    public int AuthorID;
    public int Likes;
    public int Reads;
    public int Comments;

    public Post(int ID, String title, Date postDate, int authorID, int likes, int reads, int comments) {
        this.ID = ID;
        Title = title;
        PostDate = postDate;
        AuthorID = authorID;
        Likes = likes;
        Reads = reads;
        Comments = comments;
    }
}
//	继承来显示public int Disapproves;//不显示在UI上