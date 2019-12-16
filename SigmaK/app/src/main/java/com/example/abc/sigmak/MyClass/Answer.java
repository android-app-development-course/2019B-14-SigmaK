package com.example.abc.sigmak.MyClass;

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
}
