package com.example.abc.sigmak.MyClass;

import java.io.Serializable;
import java.util.Date;

public class Post implements Serializable {


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


}
//	继承来显示public int Disapproves;//不显示在UI上