package com.example.abc.sigmak.MyClass;

import java.util.Date;

public class Question extends Post {
    public Question(int ID, String title, Date postDate, int authorID, int likes, int reads, int comments) {
        super(ID, title, postDate, authorID, likes, reads, comments);
    }

    public enum QuestionStatus{
        NoFinished,Closed/*无满意回答*/,Accepted//有满意回答
    }

    public Date LastEditedDate;
    public Article.BlogCategory Category;
    public String[] KeyWords;
    public int Answers;
    public QuestionStatus Status;
}
