package com.example.abc.sigmak;
import android.content.Intent;
import android.os.Handler;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityManagerCompat;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.like.LikeButton;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.ExpandableDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.model.utils.BadgeDrawableBuilder;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent();
        intent.setClass(MainActivity.this,Enter.class);//this前面为当前activty名称，class前面为要跳转到得activity名称
        startActivity(intent);
        IProfile profile = new ProfileDrawerItem()
                .withName("soma5431")
                .withEmail("sosowolf0125@gmail.com")
                .withIcon(R.drawable.user)
                .withIdentifier(100);
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withTranslucentStatusBar(false)
                .withHeaderBackground(R.color.my_background)
                .addProfiles(profile)
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean current) {
                        switch ((int)profile.getIdentifier()) {
                            case 100:
                                //Toast.makeText(MainActivity.this,"the icon is clicked",Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                break;
                        }
                        return true;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .build();
        new DrawerBuilder()
                .withActivity(this)
                .withAccountHeader(headerResult)
                .addDrawerItems(
                        new PrimaryDrawerItem()
                        .withName("我的收藏")
                        .withDescription(numSave)
                        .withIdentifier(1)
                        .withIcon(R.drawable.save),
                        new PrimaryDrawerItem()
                        .withName("我的喜欢")
                        .withDescription(numLike)
                        .withIdentifier(2)
                        .withIcon(R.drawable.like),
                        new PrimaryDrawerItem()
                        .withName("我的关注")
                        .withDescription(numFollow)
                        .withIdentifier(3)
                        .withIcon(R.drawable.follow),
                        new PrimaryDrawerItem()
                        .withName("我的积分")
                        .withIcon(R.drawable.coin)
                        .withDescription(numCoin)
                        .withIdentifier(4),
                        new PrimaryDrawerItem()
                        .withName("积分商城")
                        .withIcon(R.drawable.shop)
                        .withIdentifier(5)
                )
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
                //Toast.makeText(MainActivity.this,"悬浮isClicked",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, Question.class);//this前面为当前activty名称，class前面为要跳转到得activity名称
                startActivity(intent);
            }
        });
    }

    private void initData()
    {
        list=new ArrayList<Preview>();
        for(int i=0;i<20;i++)
        {
            list.add(new Preview(
                    "initTitle"+i,
                    System.currentTimeMillis()+test,
                    "ABC",
                    "555"
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
                //刷新数据
                ArrayList<Preview> newList = new ArrayList<Preview>();
                for (int i=0;i<20;i++){
                    newList.add(new Preview(
                            "newTitle"+i,
                            System.currentTimeMillis()+test,
                            "ABC",
                            "555"
                    ));
                }
                myAdapter.refresh(newList);
                refreshlayout.finishRefresh(2000/*,false*/);
                //不传时间则立即停止刷新    传入false表示刷新失败
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                ArrayList<Preview>addList=new ArrayList<Preview>();
                for (int i=0;i<10;i++){
                    addList.add(new Preview(
                            "addTitle"+i,
                            System.currentTimeMillis()+test,
                            "ABC",
                            "555"
                    ));
                }
                myAdapter.add(addList);
                //在这里执行下拉加载时的具体操作(网络请求、更新UI等)
                refreshlayout.finishLoadmore(2000/*,false*/);//不传时间则立即停止刷新    传入false表示加载失败
            }
        });
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
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, daily.class);//this前面为当前activty名称，class前面为要跳转到得activity名称
                    startActivity(intent);
                    break;

            }

        }

    };

}
