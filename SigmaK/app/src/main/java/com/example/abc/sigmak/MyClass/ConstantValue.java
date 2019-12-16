package com.example.abc.sigmak.MyClass;

import java.util.Date;

public final class ConstantValue {
    public static String UserIDKey = "UserID";
    public static String LoginStatusKey = "LoginStatus";
    public static String CoinsKey = "Coins";
    public static String ProfilePhotoKey = "ProfilePhoto";
    //"CREATE TABLE Account(ID INTEGER PRIMARY KEY AUTOINCREMENT, Email VARCHAR(30) NOT NULL UNIQUE" +
     //       ", UserName VARCHAR(20) NOT NULL UNIQUE, Password VARCHAR(40) NOT NULL, ProfilePhoto TEXT NOT NULL)");
    public enum TableAccount{
        ID,Email,UserName,Password,ProfilePhoto
    }

    public enum TableUserInfo{
        UserInfoID,AccountID,Follows,Followers,Coins,Likes
    }

    public enum TableFavourites{
        FavouritesRecordID,UserID,PostID,Type
    }

    public enum TablePostInfo{
        PostID,Title,Type,PostDate,LastEditedDate,Category,KeyWords,AuthorID,Likes,Reads,Comments
    }

    //Follow:UserID,FollowerID,FollowDate
    public enum TableFollow{
        UserID,FollowerID,FollowDate
    }

    public enum TablePostContent{
        ContentID,PostID,TextContent
    }

    public enum TableComment{
        CommentID,PostID,UserID,PostDate,Likes,Content
    }

    public enum TableQuestionInfo{
        ID,PostID,Answers,Status,StatisfiedAnswerIDs
    }

    public enum TableAnswerInfo{
        ID,QuestionID,AnswerID
    }
}
