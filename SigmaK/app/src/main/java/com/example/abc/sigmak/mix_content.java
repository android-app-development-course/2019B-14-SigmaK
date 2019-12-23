package com.example.abc.sigmak;

import com.example.abc.sigmak.MyClass.Post;
import com.example.abc.sigmak.MyClass.TextContent;

import java.util.List;

/**
 * Created by abc on 2019/12/23.
 */

public class mix_content {
    boolean Like;
    boolean Dislike;
    TextContent m_content;
    Post m_post;
    boolean is_spread=false;
    mix_content(boolean like,boolean dislike,TextContent content,Post post)
    {
        Like=like;
        Dislike=dislike;
        m_content=content;
        m_post=post;
    }

}
