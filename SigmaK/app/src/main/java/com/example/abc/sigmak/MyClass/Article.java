package com.example.abc.sigmak.MyClass;

import java.util.Date;

public class Article extends Post {
    public Article(int ID, String title, Date postDate, int authorID, int likes, int reads, int comments) {
        super(ID, title, postDate, authorID, likes, reads, comments);
    }

    public enum BlogCategory{计算机科学,数学科学,文学批评,英语}
    public Date LastEditedDate;
    public BlogCategory Category;
    public String[] KeyWords;
}
