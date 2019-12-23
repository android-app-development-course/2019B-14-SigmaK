package com.example.abc.sigmak;

import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abc.sigmak.Exceptions.RecordException;
import com.example.abc.sigmak.MyClass.Post;
import com.example.abc.sigmak.MyClass.TextContent;
import com.example.abc.sigmak.Utility.Manager;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.abc.sigmak.R.id.post;

public class Question extends AppCompatActivity implements Answer_Adapter.InnerItemOnclickListener,AdapterView.OnItemClickListener {
    String text="With drooping heads and tremulous tails, they mashed their way through the thick mud, floundering and stumbling he between whiles, as if they were falling to pieces at the large joints. As often as the driver rested them and brought them to a stand, with a wary `Wo-ho! so-ho then!' the near leader violently shook his head and everything upon it--like an unusually emphatic horse, ";
    ImageButton btn1;
    ImageButton hidebtn;
    EditText editText1;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        Manager manager=Manager.getInstance(this.getApplicationContext());
        int id=getIntent().getIntExtra("QuestionID",0);
        listView=(ListView)findViewById(R.id.mix_listview);
        Post post=null;
        List<Post> tmp=null;
        List<mix_content> answer=new ArrayList<mix_content>();
        List<Integer> type=new ArrayList<Integer>();
        final int TYPE_ONE=0,TYPE_TWO=1,TYPE_THREE=2,TYPE_COUNT=3;
        Post tmp1;
        TextContent content=null;
        try {
            post=manager.GetPostInfo(id);
        } catch (RecordException e) {
            e.printStackTrace();
        }
        if(post!=null)
        {
            type.add(TYPE_ONE);
            tmp1=new Post(post.ID,post.Title,post.Type,post.PostDate,post.LastEditedDate,post.Category,post.AuthorID,post.Likes,post.Reads,post.Comments,post.KeyWords);
            try {
                content=manager.GetPostCotent(tmp1.ID);
            } catch (RecordException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                answer.add(new mix_content(manager.DoILikeThis(tmp1.ID),manager.DoIDisapproveThis(tmp1.ID),content,tmp1));
            } catch (RecordException e) {
                e.printStackTrace();
            }
            //添加问题
            try {
                tmp=manager.GetAnswers(id);
            } catch (RecordException e) {
                e.printStackTrace();
            }
            if(tmp!=null)
            {
                for(int i=0;i<tmp.size();i++)
                {
                    post=tmp.get(i);
                    tmp1=new Post(post.ID,post.Title,post.Type,post.PostDate,post.LastEditedDate,post.Category,post.AuthorID,post.Likes,post.Reads,post.Comments,post.KeyWords);
                    try {
                        content=manager.GetPostCotent(tmp1.ID);
                    } catch (RecordException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    try {
                        answer.add(new mix_content(manager.DoILikeThis(tmp1.ID),manager.DoIDisapproveThis(tmp1.ID),content,tmp1));
                        type.add(TYPE_TWO);
                    } catch (RecordException e) {
                        e.printStackTrace();
                    }
                }
            }//添加答案
            Answer_Adapter adapter=new Answer_Adapter(this,answer,type);
            adapter.setOnInnerItemOnClickListener(this);
        }

    }

    @Override
    public void itemClick(View v, int position) {
        switch (v.getId()){
            case R.id.like_button:

                break;
            case R.id.profile_photo:
                Toast.makeText(Question.this,"profilePhoto is clicked",Toast.LENGTH_SHORT).show();
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
