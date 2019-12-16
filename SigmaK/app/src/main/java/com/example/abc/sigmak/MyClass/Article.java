package com.example.abc.sigmak.MyClass;

import java.util.Date;

public class Article extends Post {


    public Article(int ID, String title, PostType type,
                   Date postDate, Date lastEditedDate,
                   PostCategory category, int authorID,
                   int likes, int reads, int comments,
                   String[] keyWords) {
        super(ID, title, type, postDate, lastEditedDate, category, authorID, likes, reads, comments, keyWords);
    }
}
