package com.example.abc.sigmak;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityManagerCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.dinuscxj.refresh.RecyclerRefreshLayout;
import com.example.abc.sigmak.Exceptions.RecordException;
import com.example.abc.sigmak.MyClass.AccountInfo;
import com.example.abc.sigmak.MyClass.Post;
import com.example.abc.sigmak.MyClass.UserInfo;
import com.example.abc.sigmak.Utility.Manager;
import com.github.czy1121.view.SegmentedView;
import com.like.LikeButton;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.ExpandableDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.model.utils.BadgeDrawableBuilder;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

import static java.sql.Types.NULL;

public class MainActivity extends AppCompatActivity
{
    enum status{Question,Article};
    status Type=status.Question;
    private SegmentedView mScv1;
    int times;
    Manager manager;
    String name;
    String email;
    Toolbar toolbar;
    String test="Any one of these partners would have disinherited his son on the question of rebuilding Tellson's. In this respect the House was much on a par with the Country; which did very often disinherit its sons for suggesting improvements in laws and customs that had long been highly objectionable, but were only the more respectable.";
    RefreshLayout refreshLayout;
    String numSave="111";
    String numLike="112";
    String numFollow="50";
    String numCoin="12";
    FloatingActionButton mfloatingButton;
    private RecyclerView recyclerView;
    private List<Preview> list;
    private My_Adapter myAdapter;
    PrimaryDrawerItem save=new PrimaryDrawerItem().withName(R.string.Like).withIcon(R.drawable.save);
    PrimaryDrawerItem follow=new PrimaryDrawerItem().withName(R.string.Follows).withIcon(R.drawable.follow);
    PrimaryDrawerItem follower=new PrimaryDrawerItem().withName(R.string.Followers).withIcon(R.drawable.fans);
    PrimaryDrawerItem coins=new PrimaryDrawerItem().withName(R.string.Coins).withIcon(R.drawable.coin);
    AccountInfo User;
    AccountHeader headerResult;
    UserInfo userinfo;
    Drawer drawer;
    boolean loginStatus=false;
    AlertDialog.Builder builder;
    Bundle state;
    AlertDialog warning;
    @Override
    protected void onResume() {
        super.onResume();
        try {
            loginStatus=manager.LoginStatus(this.getApplicationContext());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(loginStatus)
        {
            try {
                userinfo=manager.GetUserInfo();
            } catch (ConnectException e) {
                builder.setMessage(e.getMessage());
                builder.show();
            } catch (RecordException e) {
                builder.setMessage(e.getMessage());
                builder.show();
            }
            try {
                User=manager.GetAccountInfo();
            } catch (RecordException e) {
                builder.setMessage(e.getMessage());
                builder.show();
            }
            IProfile profile = new ProfileDrawerItem().withName(User.Name.toString())
                    .withEmail(User.Email.toString())
                    .withIcon(User.ProfilePhoto)
                    .withIdentifier(User.UserID);
            headerResult = new AccountHeaderBuilder()
                    .withActivity(this)
                    .withTranslucentStatusBar(false)
                    .withHeaderBackground(R.color.my_background)
                    .addProfiles(profile)
                    .withOnAccountHeaderProfileImageListener(new AccountHeader.OnAccountHeaderProfileImageListener() {
                        @Override
                        public boolean onProfileImageClick(View view, IProfile profile, boolean current) {
                            Intent intent=new Intent();
                            intent.setClass(MainActivity.this,Homepage.class);
                            startActivity(intent);
                            return true;
                        }
                        @Override
                        public boolean onProfileImageLongClick(View view, IProfile profile, boolean current) {
                            return true;
                        }
                    })//换头像
                    .withSavedInstance(state)
                    .build();
            save.withDescription(userinfo.Likes+"");
            follow.withDescription(userinfo.Follows+"");
            follower.withDescription(userinfo.Followers+"");
            coins.withDescription(userinfo.Coins+"");
            drawer.updateItem(save);
            drawer.updateItem(follow);
            drawer.updateItem(follower);
            drawer.updateItem(coins);
            drawer.removeHeader();
            drawer.setHeader(headerResult.getView());
        }
        Refresh();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        state=savedInstanceState;
        setContentView(R.layout.activity_main);

        builder= new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Warning");
        builder.setNeutralButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        warning=builder.create();

        manager=Manager.getInstance(this.getApplicationContext());
        try {
            loginStatus=manager.LoginStatus(this.getApplicationContext());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(!loginStatus)
        {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this,Enter.class);
            startActivity(intent);
        }
        drawer=new DrawerBuilder()
                .withActivity(this)
                .withAccountHeader(headerResult)
                .addDrawerItems(
                        save.withIdentifier(1),
                        follower.withIdentifier(2),
                        follow.withIdentifier(3),
                        coins.withIdentifier(4),
                        new PrimaryDrawerItem()
                        .withName(R.string.Shop)
                        .withIcon(R.drawable.shop)
                        .withIdentifier(5),
                        new PrimaryDrawerItem()
                        .withName(R.string.LogOut)
                        .withIcon(R.drawable.logout)
                        .withIdentifier(6)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem != null) {
                            switch ((int)drawerItem.getIdentifier()) {
                                case 1:

                                    break;
                                case 2:
                                    Intent intent2 = new Intent();
                                    intent2.setClass(MainActivity.this,userList.class);
                                    intent2.putExtra("status",0);
                                    startActivity(intent2);
                                    break;
                                case 3:
                                    Intent intent3 = new Intent();
                                    intent3.setClass(MainActivity.this,userList.class);
                                    intent3.putExtra("status",1);
                                    startActivity(intent3);
                                    break;
                                case 4:

                                    break;
                                case 5:

                                    break;
                                case 6:
                                    try {
                                        manager.LogOut(view.getContext());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    try {
                                        manager.LogOut(MainActivity.this.getApplicationContext());
                                    } catch (Exception e) {
                                        warning.setMessage(e.getMessage());
                                        warning.show();
                                        warning.getButton(DialogInterface.BUTTON_NEUTRAL).setTextColor(getResources().getColor(R.color.primary_dark));
                                        e.printStackTrace();
                                        break;
                                    }
                                    Intent intent = new Intent();
                                    intent.setClass(MainActivity.this,Enter.class);
                                    startActivity(intent);
                                    break;
                                default:
                                    break;
                            }
                        }
                        return true;
                    }
                })//侧边栏点击事件
                .build();
        //侧边栏
        refreshLayout = (RefreshLayout)findViewById(R.id.My_refreshLayout);
        InitItem();
        SetPullRefresher();
        initData();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);//指定Toolbar上的视图文件
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case android.R.id.home:
                this.finish();//真正实现回退功能的代码
            default:break;

        }
        return super.onOptionsItemSelected(item);
    }
    private void InitItem()
    {
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //标题栏
        mfloatingButton=(FloatingActionButton)findViewById(R.id.floatingactionbar);
        mfloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent111=new Intent();
                intent111.setClass(MainActivity.this,Write.class);
                startActivity(intent111);
                /*AlertDialog.Builder builder1;
                LayoutInflater inflater = getLayoutInflater();
                View layout=inflater.inflate(R.layout.write_answer,null);
                final EditText Title,Answer111;
                builder1= new AlertDialog.Builder(MainActivity.this);
                Title=(EditText)layout.findViewById(R.id.title123);
                Answer111=(EditText)layout.findViewById(R.id.answer123);
                builder1.setTitle("ANSWER");
                builder1.setView(layout);
                builder1.setNeutralButton("SUBMIT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                AlertDialog dialog=builder1.create();
                dialog.show();
                dialog.getButton(DialogInterface.BUTTON_NEUTRAL).setTextColor(getResources().getColor(R.color.primary_dark));*/
            }
        });//编辑器实现的地方

        mScv1=(SegmentedView)findViewById(R.id.sv1);
        mScv1.setOnItemSelectedListener(new SegmentedView.OnItemSelectedListener() {
            @Override
            public void onSelected(int index, String text) {
                Toast.makeText(MainActivity.this,  index + " : " + text, Toast.LENGTH_SHORT).show();
                if(Type==status.values()[index])
                {
                    return;
                }
                else
                {
                    Type=status.values()[index];
                    Refresh();
                }
            }
        });//加载问题或文章
    }

    private void initData()
    {
        list=new ArrayList<Preview>();
        if(loginStatus)
        {
            List<Post> tmp;
            tmp=manager.GetRecommandQuestions();
            if(tmp!=null&&tmp.size()!=0)
            {
                Post q1;
                for(int i=0;i<10;i++)
                {
                    q1=tmp.get(i);
                    list.add(new Preview(
                            q1.Title+"times:"+times,
                            q1.KeyWords.toString(),
                            "Questioner:"+q1.AuthorID,
                            q1.Likes+"",
                            q1.ID
                    ));
                    times++;
                }
            }
        }
        myAdapter=new My_Adapter(list);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);//纵向线性布局
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(myAdapter);
        myAdapter.setOnItemClickListener(MyItemClickListener);
    }

    private void SetPullRefresher()
    {
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                Refresh();
                //刷新数据
                refreshlayout.finishRefresh(1000/*,false*/);
                //不传时间则立即停止刷新    传入false表示刷新失败
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                Add();
                //在这里执行下拉加载时的具体操作(网络请求、更新UI等)
                refreshlayout.finishLoadmore(1000/*,false*/);//不传时间则立即停止刷新    传入false表示加载失败
            }
        });
    }
    private void Refresh()
    {
        try {
            loginStatus=manager.LoginStatus(this.getApplication());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(loginStatus)
        {
            ArrayList<Preview> newList = new ArrayList<Preview>();
            List<Post> tmp;
            Post q1;
            if(Type==status.Question)
            {
                tmp=manager.GetRecommandQuestions();
                if(tmp!=null&&tmp.size()!=0)
                {
                    for(int i=0;i<tmp.size();i++) {
                        q1 = tmp.get(i);
                        newList.add(new Preview(
                                q1.Title+"times:"+times,
                                q1.KeyWords.toString(),
                                "Questioner:" + q1.AuthorID,
                                q1.Likes + "",
                                q1.ID
                        ));
                    }
                }

            }
            else if(Type==status.Article)
            {
                tmp=manager.GetRecommandArticles();
                if(tmp!=null&&tmp.size()!=0)
                {
                    for(int i=0;i<tmp.size();i++) {
                        q1 = tmp.get(i);
                        newList.add(new Preview(
                                q1.Title+"times:"+times,
                                q1.KeyWords.toString(),
                                "Author:" + q1.AuthorID,
                                q1.Likes + "",
                                q1.ID
                        ));
                    }
                }

            }

            if(newList.size()!=0)
            {
                times++;
                myAdapter.refresh(newList);
            }
        }

    }
    private void Add()
    {
        try {
            loginStatus=manager.LoginStatus(this.getApplication());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(loginStatus)
        {
            ArrayList<Preview>addList=new ArrayList<Preview>();
            List<Post> tmp;
            Post q1;
            if(Type==status.Question)
            {
                tmp=manager.GetRecommandQuestions();
                if(tmp!=null)
                {
                    for(int i=0;i<tmp.size();i++) {
                        q1 = tmp.get(i);
                        addList.add(new Preview(
                                q1.Title,
                                q1.KeyWords.toString(),
                                "Questioner:" + q1.AuthorID,
                                q1.Likes + "",
                                q1.ID
                        ));
                    }
                }

            }
            else if (Type==status.Article)
            {
                tmp=manager.GetRecommandArticles();
                if(tmp!=null)
                {
                    for(int i=0;i<10;i++) {
                        q1 = tmp.get(i);
                        if(q1!=null)
                        {
                            addList.add(new Preview(
                                    q1.Title,
                                    q1.KeyWords.toString(),
                                    "Questioner:" + q1.AuthorID,
                                    q1.Likes + "",
                                    q1.ID));
                        }
                    }
                }

            }
            if(addList.size()!=0)
            {
                myAdapter.add(addList);
            }

        }
    }


    private My_Adapter.OnItemClickListener MyItemClickListener=new My_Adapter.OnItemClickListener()
    {
        @Override
        public void onItemClick(View v, My_Adapter.ViewName viewName, int position) {
            switch (v.getId())
            {
                case R.id.like_button:
                    myAdapter.Fresh(position);
                    //Toast.makeText(MainActivity.this,"你点击了爱心"+(position+1),Toast.LENGTH_SHORT).show();
                    break;
                default:
                    int ID=myAdapter.getId(position);
                    if(Type==status.Question)
                    {
                        Intent intent = new Intent();
                        intent.setClass(MainActivity.this, Question.class);
                        intent.putExtra("QuestionID",ID);
                        startActivity(intent);
                    }
                    else if(Type==status.Article)
                    {
                        Intent intent=new Intent();
                        intent.setClass(MainActivity.this,daily.class);
                        intent.putExtra("ArticleID",ID);
                        startActivity(intent);
                    }//启动不同的activity并传id
                    break;

            }

        }
    };

}
