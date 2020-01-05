package com.example.abc.sigmak;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.abc.sigmak.Exceptions.RecordException;
import com.example.abc.sigmak.MyClass.AccountInfo;
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
    ImageView profile;
    int id;
    Manager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily);
        id=getIntent().getIntExtra("ArticleID",0);
        manager= Manager.getInstance(this.getApplicationContext());
        userid=(TextView)findViewById(R.id.daily_id);
        articletitle=(TextView)findViewById(R.id.Article_title);
        article=(TextView)findViewById(R.id.Article);
        profile=(ImageView)findViewById(R.id.profile_daily);
        Post post=null;
        try {
            post=manager.GetPostInfo(id);
        } catch (RecordException e) {
            e.printStackTrace();
            test=true;
        }
        AccountInfo user=null;
        if(test!=true)
        {
            try {
                user=manager.GetAccountInfo();
            } catch (RecordException e) {
                e.printStackTrace();
            }
            if(user!=null)
            {
                profile.setImageBitmap(user.ProfilePhoto);
            }
            userid.setText(post.AuthorID+"");
            if(post!=null)
            {
                articletitle.setText(post.Title);
            }
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
            if(test!=true&&content!=null)
            {
                article.setText(content.Text.toString());
            }
        }
    }
}
