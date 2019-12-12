package com.example.abc.sigmak;

/**
 * Created by abc on 2019/11/21.
 */

public class Preview {
    String title;
    String text;
    String writer;
    String like;
    boolean is_Liked;

    public Preview(String Title,String Text,String Writer,String Like) {
        title = Title;
        text = Text;
        writer = Writer;
        like = Like;
        is_Liked=false;
    }
    public Preview(String Title,String Text,String Writer,String Like,boolean Flag) {
        title = Title;
        text = Text;
        writer = Writer;
        like = Like;
        is_Liked=Flag;
    }

    public boolean is_Liked() {
        return is_Liked;
    }

    public void setIs_Liked(boolean is_Liked) {
        this.is_Liked = is_Liked;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public String getWriter() {
        return writer;
    }

    public String getLike() {
        return like;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }
}
