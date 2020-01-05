package com.example.abc.sigmak;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.example.abc.sigmak.Exceptions.RecordException;
import com.example.abc.sigmak.MyClass.Post;
import com.example.abc.sigmak.MyClass.TextContent;
import com.example.abc.sigmak.Utility.Manager;

import java.io.IOException;

//import com.example.abc.test18.R;

public class daily extends AppCompatActivity {
    boolean test=false;
    TextView userid;
    TextView articletitle;
    TextView article;
    int id;
    Manager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily);
        id=getIntent().getIntExtra("ArticleID",0);
        manager= Manager.getInstance(this.getApplicationContext());
        userid=(TextView)findViewById(R.id.daily_UserName);
        articletitle=(TextView)findViewById(R.id.Article_title);
        article=(TextView)findViewById(R.id.Article);
        Post post=null;
        try {
            post=manager.GetPostInfo(id);
        } catch (RecordException e) {
            e.printStackTrace();
            test=true;
        }
        if(test!=true)
        {
            userid.setText(post.AuthorID);
            articletitle.setText(post.Title);
            TextContent content=null;
            try {
                content=manager.GetPostCotent(id);
            } catch (RecordException e) {
                e.printStackTrace();
                test=true;
            } catch (IOException e) {
                e.printStackTrace();
                test=true;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                test=true;
            }
            if(test!=true)
            {
                article.setText(content.Text.toString());
            }
        }
    }
}
