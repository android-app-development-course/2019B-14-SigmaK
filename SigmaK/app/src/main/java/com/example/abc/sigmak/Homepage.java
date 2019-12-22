package com.example.abc.sigmak;

import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class Homepage extends AppCompatActivity implements Homepage_Adapter.InnerItemOnclickListener,OnItemClickListener{
    private ListView mListview;
    Homepage_Adapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        mListview=(ListView)findViewById(R.id.post);
        List<Preview> mlist=new ArrayList<Preview>();
        User user=new User(0,((BitmapDrawable)getResources().getDrawable(R.drawable.profileback)).getBitmap(),"User");
        Preview preview=new Preview("Title","text","writer","111");
        for(int i=0;i<20;i++)
        {
            mlist.add(preview);
        }
        adapter=new Homepage_Adapter(this,mlist,user);
        adapter.setOnInnerItemOnClickListener(this);
        mListview.setAdapter(adapter);
        mListview.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Toast.makeText(Homepage.this,"进入用户界面",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void itemClick(View v, int position) {
        switch (v.getId()){
            case R.id.like_button:
                Toast.makeText(Homepage.this,"like is clicked",Toast.LENGTH_SHORT).show();
                adapter.Fresh(position);
                break;
            case R.id.profile_photo:
                Toast.makeText(Homepage.this,"profilePhoto is clicked",Toast.LENGTH_SHORT).show();
            default:
                break;
        }
    }
}
