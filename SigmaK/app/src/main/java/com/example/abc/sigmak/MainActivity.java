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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.dinuscxj.refresh.RecyclerRefreshLayout;
import com.example.abc.sigmak.Exceptions.RecordException;
import com.example.abc.sigmak.MyClass.AccountInfo;
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

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

import static java.sql.Types.NULL;

public class MainActivity extends AppCompatActivity
{
    enum status{Question,Article};
    status Type=status.Question;
    private SegmentedView mScv1;
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
    PrimaryDrawerItem save=new PrimaryDrawerItem();
    PrimaryDrawerItem follow=new PrimaryDrawerItem();
    PrimaryDrawerItem follower=new PrimaryDrawerItem();
    PrimaryDrawerItem coins=new PrimaryDrawerItem();
    AccountInfo User;
    AccountHeader headerResult;
    UserInfo userinfo;
    Drawer drawer;
    @Override
    protected void onResume() {
        super.onResume();
        headerResult.updateProfile(new ProfileDrawerItem()
                .withName(User.Name)
                .withEmail(User.Email)
                .withIcon(User.ProfilePhoto)
                .withIdentifier(User.UserID));
        save.withDescription(userinfo.Likes);
        follow.withDescription(userinfo.Follows);
        follower.withDescription(userinfo.Followers);
        coins.withDescription(userinfo.Coins);
        drawer.updateItem(save);
        drawer.updateItem(follow);
        drawer.updateItem(follower);
        drawer.updateItem(coins);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent();
        intent.setClass(MainActivity.this,Enter.class);
        startActivity(intent);

        final AlertDialog.Builder builder= new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Warning");
        builder.setNeutralButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        User=new AccountInfo();
        manager=Manager.getInstance(this.getApplicationContext());
        try {
            User=manager.GetAccountInfo();
        } catch (RecordException e) {
            builder.setMessage(e.getMessage());
            builder.show();
        }
        userinfo=new UserInfo();
        try {
            userinfo=manager.GetUserInfo();
        } catch (ConnectException e) {
            builder.setMessage(e.getMessage());
            builder.show();
        } catch (RecordException e) {
            builder.setMessage(e.getMessage());
            builder.show();
        }

        IProfile profile = new ProfileDrawerItem();
        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withTranslucentStatusBar(false)
                .withHeaderBackground(R.color.my_background)
                .addProfiles(profile)
                .withOnAccountHeaderProfileImageListener(new AccountHeader.OnAccountHeaderProfileImageListener() {
                    @Override
                    public boolean onProfileImageClick(View view, IProfile profile, boolean current) {
                        return true;
                    }
                    @Override
                    public boolean onProfileImageLongClick(View view, IProfile profile, boolean current) {
                        return true;
                    }
                })//换头像
                .withSavedInstance(savedInstanceState)
                .build();
        drawer=new DrawerBuilder()
                .withActivity(this)
                .withAccountHeader(headerResult)
                .addDrawerItems(
                        save,
                        follower,
                        follow,
                        coins,
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

                                    break;
                                case 3:

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
        initData();
        SetPullRefresher();
        InitItem();
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
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, Question.class);
                startActivity(intent);
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
        List<com.example.abc.sigmak.MyClass.Question> tmp;
        tmp=manager.GetRecommandQuestions(10);
        com.example.abc.sigmak.MyClass.Question q1;
        for(int i=0;i<10;i++)
        {
            q1=tmp.get(i);
            list.add(new Preview(
                    q1.Title,
                    q1.KeyWords.toString(),
                    "Questioner:"+q1.AuthorID,
                    q1.Likes+"",
                    q1.ID
            ));
            myAdapter=new My_Adapter(list);
            recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);//纵向线性布局
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(myAdapter);
            myAdapter.setOnItemClickListener(MyItemClickListener);
        }
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
        ArrayList<Preview> newList = new ArrayList<Preview>();
        if(Type==status.Question)
        {
            List<com.example.abc.sigmak.MyClass.Question> tmp;
            tmp=manager.GetRecommandQuestions(10);
            com.example.abc.sigmak.MyClass.Question q1;
            for(int i=0;i<10;i++) {
                q1 = tmp.get(i);
                newList.add(new Preview(
                        q1.Title,
                        q1.KeyWords.toString(),
                        "Questioner:" + q1.AuthorID,
                        q1.Likes + "",
                        q1.ID
                ));
            }
        }
        else if(Type==status.Article)
        {
            List<com.example.abc.sigmak.MyClass.Article>tmp;
            tmp=manager.GetRecommandArticles(10);
            com.example.abc.sigmak.MyClass.Article q1;
            for(int i=0;i<10;i++) {
                q1 = tmp.get(i);
                newList.add(new Preview(
                        q1.Title,
                        q1.KeyWords.toString(),
                        "Questioner:" + q1.AuthorID,
                        q1.Likes + "",
                        q1.ID
                ));
            }
        }
        myAdapter.refresh(newList);
    }
    private void Add()
    {
        ArrayList<Preview>addList=new ArrayList<Preview>();
        if(Type==status.Question)
        {
            List<com.example.abc.sigmak.MyClass.Question> tmp;
            tmp=manager.GetRecommandQuestions(10);
            com.example.abc.sigmak.MyClass.Question q1;
            for(int i=0;i<10;i++) {
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
        else if (Type==status.Article)
        {
            List<com.example.abc.sigmak.MyClass.Article>tmp;
            tmp=manager.GetRecommandArticles(10);
            com.example.abc.sigmak.MyClass.Article q1;
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
        myAdapter.add(addList);
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
                        intent.setClass(MainActivity.this, daily.class);
                        startActivity(intent);
                    }
                    else if(Type==status.Article)
                    {

                    }//启动不同的activity并传id
                    break;

            }

        }
    };

}
