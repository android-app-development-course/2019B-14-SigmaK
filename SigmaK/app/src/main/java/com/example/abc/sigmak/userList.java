package com.example.abc.sigmak;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.example.abc.sigmak.R;

import java.util.ArrayList;
import java.util.List;

public class userList extends AppCompatActivity implements User_Adapter.InnerItemOnclickListener,OnItemClickListener {
    private ListView mListview;
    User_Adapter adapter;
    int status;
    boolean flag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        mListview=(ListView)findViewById(R.id.member);
        status=getIntent().getIntExtra("status",0);//0 未关注 1 已关注
        if(status==0)
        {
            flag=false;
        }
        else
        {
            flag=true;
        }
        List<User> mlist=new ArrayList<User>();
        final Bitmap bitmap=((BitmapDrawable)getResources().getDrawable(R.drawable.lena)).getBitmap();
        User user=new User(111,bitmap,"test",flag);
        for(int i=0;i<20;i++)
        {
            mlist.add(user);
        }
        adapter=new User_Adapter(this,mlist);
        adapter.setOnInnerItemOnClickListener(this);
        mListview.setAdapter(adapter);
        mListview.setOnItemClickListener(this);
    }

    @Override
    public void itemClick(View v,int position) {
        switch (v.getId()){
            case R.id.follow_status:
                adapter.freshButton(position);
                Toast.makeText(userList.this,"点击关注",Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Toast.makeText(userList.this,"进入用户界面",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        intent.setClass(userList.this,Homepage.class);
        startActivity(intent);
    }
}
