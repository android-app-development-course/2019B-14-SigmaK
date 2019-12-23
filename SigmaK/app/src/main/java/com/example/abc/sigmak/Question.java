package com.example.abc.sigmak;

import android.content.DialogInterface;
import android.media.Image;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    ListView listView;
    Answer_Adapter adapter;
    Manager manager;
    AlertDialog.Builder builder;
    AlertDialog.Builder builder1;
    String comment;
    String answer;
    String answer_title;
    final int TYPE_ONE=0,TYPE_TWO=1,TYPE_THREE=2,TYPE_COUNT=3;
    int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        manager=Manager.getInstance(this.getApplicationContext());
        id=getIntent().getIntExtra("QuestionID",0);

        final EditText edit = new EditText(this);
        builder= new AlertDialog.Builder(Question.this);
        builder.setTitle("COMMENT");
        builder.setView(edit);
        builder.setNeutralButton("SUBMIT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                comment=edit.getText().toString();
            }
        });

        LayoutInflater inflater = getLayoutInflater();
        View layout=inflater.inflate(R.layout.write_answer,(ViewGroup)findViewById(R.id.dialog));
        final EditText Title,Answer111;
        builder1= new AlertDialog.Builder(Question.this);
        Title=(EditText)layout.findViewById(R.id.title123);
        Answer111=(EditText)layout.findViewById(R.id.answer123);
        builder1.setTitle("ANSWER");
        builder1.setView(R.layout.user);
        builder1.setNeutralButton("SUBMIT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                answer=Answer111.getText().toString();
                answer_title=Title.getText().toString();
            }
        });

        listView=(ListView)findViewById(R.id.mix_listview);
        Post post=null;
        List<Post> tmp=null;
        List<mix_content> answer=new ArrayList<mix_content>();
        List<Integer> type=new ArrayList<Integer>();
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
            adapter=new Answer_Adapter(this,answer,type);
            adapter.setOnInnerItemOnClickListener(this);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(this);
        }

    }

    @Override
    public void itemClick(View v, int position) {
        mix_content tmp;
        tmp=(mix_content)adapter.mlist.get(position);
        switch (v.getId()){
            case R.id.like:
                adapter.Fresh_like(position);
                if(tmp.Like)
                {
                    try {
                        manager.Disapprove(tmp.m_post.ID);
                    } catch (RecordException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    try {
                        manager.Like(tmp.m_post.ID);
                    } catch (RecordException e) {
                        e.printStackTrace();
                    }
                }
                break;//问题的收藏
            case R.id.answer_que:
                builder1.show();
                try {
                    TextContent content=new TextContent();
                    content.Text=answer.toCharArray();
                    manager.Answer(answer_title,id,content);
                } catch (RecordException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.Against:
                adapter.Dislike(position);
                try {
                    manager.Disapprove(tmp.m_post.ID);
                } catch (RecordException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.Agree:
                adapter.Fresh_like(position);
                try {
                    manager.Like(tmp.m_post.ID);
                } catch (RecordException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.commentsNum:
                if(tmp.is_spread)
                {
                    adapter.remove(position,tmp.m_post.Comments);
                }
                else{
                    try {
                        adapter.add(position,manager.GetPostComment(tmp.m_post.ID),TYPE_THREE);
                    } catch (RecordException e) {
                        e.printStackTrace();
                    }
                }

                break;
            case R.id.comments:
                builder.show();
                try {
                    manager.PostComment(tmp.m_post.ID,comment);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
