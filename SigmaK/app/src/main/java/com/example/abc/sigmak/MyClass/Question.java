package com.example.abc.sigmak.MyClass;

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
}
