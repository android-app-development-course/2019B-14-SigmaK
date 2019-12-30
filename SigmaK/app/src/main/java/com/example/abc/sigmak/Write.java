package com.example.abc.sigmak;

import android.nfc.FormatException;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.abc.sigmak.Exceptions.RecordException;
import com.example.abc.sigmak.MyClass.Post;
import com.example.abc.sigmak.MyClass.TextContent;
import com.example.abc.sigmak.Utility.Manager;

import java.io.IOException;

import ezy.ui.view.RoundButton;

public class Write extends AppCompatActivity {
    RadioGroup post_type;
    RadioGroup post_category;
    Post.PostType type;
    Post.PostCategory category;
    RoundButton submit;
    TextView mytitle;
    TextView mycontent;Manager manager;
    boolean ok=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);
        manager=Manager.getInstance(this.getApplicationContext());
        try {
            ok=manager.LoginStatus(this.getApplicationContext());
        } catch (IOException e) {
            e.printStackTrace();
            ok=false;
        }
        post_type=(RadioGroup)findViewById(R.id.post_type);
        post_category=(RadioGroup)findViewById(R.id.post_category);
        submit=(RoundButton)findViewById(R.id.Write_submit);
        mycontent=(TextView)findViewById(R.id.write_post);
        mytitle=(TextView)findViewById(R.id.write_title);
        post_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId){
                    case R.id.my_question:
                        type= Post.PostType.Question;
                        break;
                    case R.id.my_article:
                        type= Post.PostType.Blog;
                        break;
                }
            }
        });
        post_category.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.computer:
                        category= Post.PostCategory.计算机科学;
                        break;
                    case R.id.maths:
                        category= Post.PostCategory.数学科学;
                        break;
                    case R.id.literary:
                        category= Post.PostCategory.文学批评;
                        break;
                    case R.id.English:
                        category= Post.PostCategory.英语;
                        break;
                }
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ok)
                {
                    TextContent content=new TextContent(mycontent.getText().toString());
                    String[] keyword={mycontent.getText().toString()};
                    try {
                        manager.PostArticle(mytitle.getText().toString(),content,category,keyword,type);
                    } catch (RecordException e) {
                        e.printStackTrace();
                    } catch (FormatException e) {
                        e.printStackTrace();
                    }
                }
                finish();
            }
        });
    }
}
