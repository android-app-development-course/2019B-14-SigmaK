package com.example.abc.sigmak.MyClass;

import java.io.Serializable;
import java.util.Date;

public class Comment implements Serializable {
    public int ID;
    public int ArticleID;
    public String UserName;
    public Date PostDate;
    public int Likes;
    public char[] Content;//最长200中文字符
}
