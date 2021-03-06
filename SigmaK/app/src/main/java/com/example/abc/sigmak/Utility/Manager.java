package com.example.abc.sigmak.Utility;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.nfc.FormatException;
import android.util.Log;

import com.example.abc.sigmak.Exceptions.AccountException;
import com.example.abc.sigmak.Exceptions.RecordException;
import com.example.abc.sigmak.MyClass.AccountInfo;
import com.example.abc.sigmak.MyClass.Answer;
import com.example.abc.sigmak.MyClass.Article;
import com.example.abc.sigmak.MyClass.Comment;
import com.example.abc.sigmak.MyClass.ConstantValue;
import com.example.abc.sigmak.MyClass.Post;
import com.example.abc.sigmak.MyClass.Question;
import com.example.abc.sigmak.MyClass.TextContent;
import com.example.abc.sigmak.MyClass.UserInfo;
import com.example.abc.sigmak.R;

import java.io.IOException;
import java.net.ConnectException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

//import com.example.abc.sigmak.Utility.Tools;

public class Manager {


    private AccountInfo _accountInfo;
    private UserInfo _userInfo;
    private boolean _loginStatus = false;
    private boolean _connection = true;//为以后预留
    private String accountPreferenceName = "Account";
    private String accountInfoKey = "AccountInfo";
    private String loginStatusKey = "LoginStatus";
    private String userInfoKey = "UserInfo";

    private static Context _context;

    private static String TAG = "Manager";
    private static SQLiteTools sqlite = null;

    private static Manager instance = null;

    public static Manager getInstance(Context context) {
        if (null == instance) {
            synchronized (Manager.class) {
                if (null == instance) {
                    sqlite = SQLiteTools.getInstance(context);
                    instance = new Manager();
                    _context = context;
                }
            }
        }
        return instance;
    }

    private Manager() {
    }

    private void getSqlite(Context c){
        if(sqlite==null)
            sqlite=SQLiteTools.getInstance(c);
    }

    /**
     * 检查是否登陆了
     * @throws RecordException
     */
    private void checkStatus() throws RecordException {

        if(!_loginStatus)
            throw new RecordException("No valid account has logged in.");
    }

    /**
     * 将PostIDs对应的PostInfo读取出来
     * @param PostIDs Integer//CHECK
     * @return
     * @throws RecordException
     */
    private static List<Post> fromPostIDtoPostList(List<Integer> PostIDs) throws RecordException {
        if(PostIDs == null||PostIDs.size() == 0)
            return null;
        List<Post> list = new LinkedList<Post>();
        List<String> temp,tmpq;
        Question tmpQuestion;
        Answer tmpAnswer;
        try {
            for (int i = 0; i < PostIDs.size(); ++i) {
                temp = sqlite.QueryString("SELECT * FROM PostInfo WHERE PostID=" + PostIDs.get(i));
                if (temp == null||temp.size() == 0)
                    throw new RecordException("Can't find postInfo of PostID=" + PostIDs.get(i));
                if (Post.PostType.valueOf(((String) temp.get(ConstantValue.TablePostInfo.Type.ordinal())).trim()) == Post.PostType.Blog) {

                    list.add(new Article(
                            Integer.parseInt((String) temp.get(ConstantValue.TablePostInfo.PostID.ordinal())),
                            (String) temp.get(ConstantValue.TablePostInfo.Title.ordinal()),
                            Post.PostType.valueOf(((String) temp.get(ConstantValue.TablePostInfo.Type.ordinal())).trim()),
                            Tools.StringToDate((String) temp.get(ConstantValue.TablePostInfo.PostDate.ordinal())),
                            Tools.StringToDate((String) temp.get(ConstantValue.TablePostInfo.LastEditedDate.ordinal())),
                            Post.PostCategory.valueOf(((String) temp.get(ConstantValue.TablePostInfo.Category.ordinal())).trim()),
                            Integer.parseInt((String) temp.get(ConstantValue.TablePostInfo.AuthorID.ordinal())),
                            Integer.parseInt((String) temp.get(ConstantValue.TablePostInfo.Likes.ordinal())),
                            Integer.parseInt((String) temp.get(ConstantValue.TablePostInfo.Reads.ordinal())),
                            Integer.parseInt((String) temp.get(ConstantValue.TablePostInfo.Comments.ordinal())),
                            Tools.CombinedStringToStringArray(
                                    (String) temp.get(ConstantValue.TablePostInfo.KeyWords.ordinal()))
                    ));
                } else if (Post.PostType.valueOf(((String) temp.get(ConstantValue.TablePostInfo.Type.ordinal())).trim()) == Post.PostType.Question) {
                    tmpQuestion = new Question(
                            Integer.parseInt((String) temp.get(ConstantValue.TablePostInfo.PostID.ordinal())),
                            (String) temp.get(ConstantValue.TablePostInfo.Title.ordinal()),
                            Post.PostType.valueOf(((String) temp.get(ConstantValue.TablePostInfo.Type.ordinal())).trim()),
                            Tools.StringToDate((String) temp.get(ConstantValue.TablePostInfo.PostDate.ordinal())),
                            Tools.StringToDate((String) temp.get(ConstantValue.TablePostInfo.LastEditedDate.ordinal())),
                            Post.PostCategory.valueOf(((String) temp.get(ConstantValue.TablePostInfo.Category.ordinal())).trim()),
                            Integer.parseInt((String) temp.get(ConstantValue.TablePostInfo.AuthorID.ordinal())),
                            Integer.parseInt((String) temp.get(ConstantValue.TablePostInfo.Likes.ordinal())),
                            Integer.parseInt((String) temp.get(ConstantValue.TablePostInfo.Reads.ordinal())),
                            Integer.parseInt((String) temp.get(ConstantValue.TablePostInfo.Comments.ordinal())),
                            Tools.CombinedStringToStringArray(
                                    (String) temp.get(ConstantValue.TablePostInfo.KeyWords.ordinal()))
                    );
                    //QuestionInfo:ID,PostID,Answers,Status.
                    tmpq = sqlite.QueryString(String.format("SELECT * FROM QuestionInfo WHERE PostID=%d",
                            Integer.parseInt((String) temp.get(ConstantValue.TablePostInfo.PostID.ordinal()))));
                    if (tmpq == null||tmpq.size() == 0)
                        throw new RecordException("No QuestionInfo which PostID=" +
                                Integer.parseInt((String)temp.get(ConstantValue.TablePostInfo.PostID.ordinal())));
                    tmpQuestion.Answers = Integer.parseInt((String)tmpq.get(ConstantValue.TableQuestionInfo.Answers.ordinal()));
                    tmpQuestion.Status = (Question.QuestionStatus.valueOf(((String) tmpq.get(ConstantValue.TableQuestionInfo.Status.ordinal())).trim()));
                    tmpQuestion.StatisfiedAnswerIDs = Tools.CombinedStringToIntegerArray(
                            (String) tmpq.get(ConstantValue.TableQuestionInfo.StatisfiedAnswerIDs.ordinal()));
                    list.add(tmpQuestion);
                } else {
                    tmpAnswer = new Answer(
                            Integer.parseInt(temp.get(ConstantValue.TablePostInfo.PostID.ordinal())),
                            temp.get(ConstantValue.TablePostInfo.Title.ordinal()),
                            Post.PostType.valueOf(temp.get(ConstantValue.TablePostInfo.Type.ordinal())),
                            Tools.StringToDate(temp.get(ConstantValue.TablePostInfo.PostDate.ordinal())),
                            Tools.StringToDate(temp.get(ConstantValue.TablePostInfo.LastEditedDate.ordinal())),
                            Post.PostCategory.valueOf(temp.get(ConstantValue.TablePostInfo.Category.ordinal())),
                            Integer.parseInt(temp.get(ConstantValue.TablePostInfo.AuthorID.ordinal())),
                            Integer.parseInt(temp.get(ConstantValue.TablePostInfo.Likes.ordinal())),
                            Integer.parseInt(temp.get(ConstantValue.TablePostInfo.Reads.ordinal())),
                            Integer.parseInt(temp.get(ConstantValue.TablePostInfo.Comments.ordinal())),
                            Tools.CombinedStringToStringArray(
                                    (String) temp.get(ConstantValue.TablePostInfo.KeyWords.ordinal()))
                    );
                    //AnswerInfo:ID,QuestionID,AnswerID
                    tmpq = sqlite.QueryString(String.format("SELECT QuestionID FROM AnswerInfo WHERE AnswerID=%d",
                            Integer.parseInt(temp.get(ConstantValue.TablePostInfo.PostID.ordinal()))));
                    if (tmpq == null || tmpq.size() == 0)
                        throw new RecordException("No QuestionInfo which PostID=" +
                                Integer.parseInt(temp.get(ConstantValue.TablePostInfo.PostID.ordinal())));
                    tmpAnswer.QuestionID = Integer.parseInt(tmpq.get(0));
                    list.add(tmpAnswer);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 初始化本机记录的登录信息
     * @param context
     * @throws IOException
     */
    private void loadLoginInfo(Context context) throws IOException {
        if(_accountInfo == null){
            try {
                _loginStatus = (boolean)Tools.ReadFromPreference(context,accountPreferenceName,loginStatusKey,new Boolean(false));
                _accountInfo = new AccountInfo(Tools.ReadFromPreference(context,accountPreferenceName, accountInfoKey));
                _userInfo = new UserInfo(Tools.ReadFromPreference(context,accountPreferenceName,userInfoKey));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    /**
     * 加载
     * @param context
     */
    public static void CreateTestData(Context context){
//        char []pass={'1','2','3','4'};
//        try {
//            SignUp(context, "Admin","Colin@qq.com",pass,
//                    BitmapFactory.decodeResource(context.getResources(),R.drawable.default_photo));
//
//            SignUp(context, "Colin","ColinDowney@126.com",pass,
//                    BitmapFactory.decodeResource(context.getResources(),R.drawable.default_photo));
//
//            SignUp(context, "Paul","Paul@163.com",pass,
//                    BitmapFactory.decodeResource(context.getResources(),R.drawable.default_photo));
//
//            SignUp(context, "Duo","Duo@foxmail.com",pass,
//                    BitmapFactory.decodeResource(context.getResources(),R.drawable.default_photo));
//////            public enum PostType{Mix,Question,Blog,Answer,Comment}
//////            public enum PostCategory{计算机科学,数学科学,文学批评,英语}
////            ContentValues cv = new ContentValues();
////            List<Integer> id = sqlite.QueryInt("SELECT ID FROM Account WHERE UserName='Admin'");
////            cv.put("UserID",id.get(0));
////            cv.put("Category","计算机科学");
////            sqlite.insert("UserInterest",cv);
////            //sqlite
//////info
////            cv.clear();
////            id = sqlite.QueryInt("SELECT ID FROM Account WHERE UserName='Colin'");
////            cv.put("UserID",id.get(0));
////            cv.put("Category","数学科学");
////            sqlite.insert("UserInterest",cv);
////
////            cv.clear();
////            id = sqlite.QueryInt("SELECT ID FROM Account WHERE UserName='Paul'");
////            cv.put("UserID",id.get(0));
////            cv.put("Category","文学批评");
////            sqlite.insert("UserInterest",cv);
////
////            cv.clear();
////            id = sqlite.QueryInt("SELECT ID FROM Account WHERE UserName='Duo'");
////            cv.put("UserID",id.get(0));
////            cv.put("Category","英语");
////            sqlite.insert("UserInterest",cv);
////
////
////
////
//        } catch (FormatException e) {
//            e.printStackTrace();
//        } catch (RecordException e) {
//            e.printStackTrace();
//        }
//////
////////
////////        //Duo和Paul关注Colin,Duo 和Paul互相关注
////////        Follow(context,2,3);
////////        Follow(context,2,4);
////////        Follow(context,4,3);
////////        Follow(context,3,4);
//        int postid = -1;
//        int aid = -1;
//        try {
//
//                postid = PostArticle(context,3,"唔 你们是怎样学好数学基础的",
//                        new TextContent("感觉自己数学（微积分、线代、概率论）学得很糟糕，准备趁还比较闲的时候补一下。\n想问问各位大佬是怎么学习相关科目的？"),
//                                Post.PostCategory.计算机科学,new String[]{"线性代数","微积分","概率论","学习"},Post.PostType.Question);
//
//            Answer(context,4,"要用起来",postid,new TextContent(
//                    "要用起来，不用很快就会忘了。\n" +
//                            "\t用的方式有：做题(课后习题之类)或者多读好书。多深入读一些好书(论文也可以)就会用到数学知识了，不仅可以学习新知识，还能巩固数学知识，一举两得。",
//                    null,null));
//            aid = Answer(context,2,"目标驱动",postid,new TextContent(
//                    "学数学是一个长期循环反馈过程。讲到基础，就更加没人敢说自己基础好了。往往是走到更高一个层面，就发现自己各种不足。所以呢，怎么学怎么补还是需要目标驱动。就好比说，整本算法导论用到的概率无非就是条件概率、随机变量、期望等很少的概率论知识，如果够用，就没必要说自己概率不好，大量去补习概率论。说真的，花一整块时间去补一门数学课真是很奢侈，而且往往效率还不高。\n" +
//                            "简单来说，目标驱动很重要。如果要考研，就专门复习微积分吧。如果因此希望微积分能在以后工作帮多大忙也不大现实。不要在概念上拖后腿就好了。",
//                    null,null));
//
//                PostComment(3,aid,new String("BinTou说的很有道理！"));
//            PostComment(4,aid,new String("是这样！"));
//            PostComment(3,aid,new String("谢谢老师~！"));
//
//            postid = PostArticle(context,3,"大家推荐一下看过的极好的计算机书籍呗(中英文都行)",
//                    new TextContent("我看完的书不多…还在各种填坑……\n" +
//                            "操作系统我推荐一本吧\n" +
//                            "operating system：three easy pieces\n" +
//                            "以前知乎上看到轮子哥推荐的，真的不错…\n" +
//                            "不过还是要多实践自己才能真的理解吧⊙ω⊙\n" +
//                            "所以有人回复吗(●'◡'●)ﾉ\n" +
//                            "老哥们看过来！"),
//                    Post.PostCategory.计算机科学,new String[]{"学习"},Post.PostType.Question);
//
//            Answer(context,4,"入门Linux",postid,new TextContent(
//                    "如果需要入门Linux的Shell操作的话，推荐这本：\n" +
//                            "The Linux Command Line by William E. Shotts, Jr.\n" +
//                            "http://linuxcommand.org/tlcl.php\n" +
//                            "对应的中文翻译是《快乐的Linux命令行》\n" +
//                            "https://billie66.github.io/TLCL/\n" +
//                            "（可以直接在线阅读哦）\n" +
//                            "可以说是：“ 正如有位老师坐在你身边，指导你学习一样 “\n" +
//                            "这是中文翻译作者的推荐：正在翻译一本我认为最好的 Linux 入门书籍－The Linux Command Line · Ruby China",
//                    null,null));
//
//            aid = Answer(context,2,"bintou 老师五核（zhuan）心（tou）",postid,new TextContent(
//                    "bintou 老师五核（zhuan）心（tou）\n" +
//                            "你值得拥有x\n" +
//                            "（趁着聚聚们潜水来卖一波x\n" +
//                            "GSLA Introduction to Linear Algebra\n" +
//                            "线性代数\n" +
//                            "CLRS Introduction to Algorithm\n" +
//                            "基本算法，数据结构，图论数论blabla以及证明\n" +
//                            "CSAPP Computer Science a programmer's perspective\n" +
//                            "计算机组成，汇编，操作系统blabla\n" +
//                            "SICP Structure and Interpretation of Computer Programs\n" +
//                            "程序的构造，抽象…\n" +
//                            "ITOC Introduction to the Theory of Computation\n" +
//                            "可计算，图灵机，复杂性…\n" +
//                            "好像书架上除了这些就没了（逃\n" +
//                            "其实觉得能把这些的细节都弄好就很强很强了，太急躁果然还是不行的orz",
//                    null,null));
//
//            }
//
//         catch (RecordException e) {
//            e.printStackTrace();
//    }catch (Exception e) {
//        e.printStackTrace();
//    }


    }

    /**
     * 获取登陆状态
     * @param context
     * @return true为本机有登录信息
     * @throws IOException
     */
    public boolean LoginStatus(Context context) throws IOException {
        loadLoginInfo(context);
        return _loginStatus;
    }

    /**
     * 如果已有本地记录的登陆状态，获取已登陆的账号信息
     * @return
     * @throws RecordException No local login record.
     */
    public AccountInfo GetAccountInfo() throws RecordException {
        if(_loginStatus==false)
            throw new RecordException("No local login record.");
        return _accountInfo;
    }

    /**
     * 获取用户的状态信息
     * @return
     * @throws ConnectException No connection.
     * @throws RecordException No valid account.
     */
    public UserInfo GetUserInfo() throws ConnectException, RecordException {
        if(_loginStatus==false)
            throw new RecordException("No valid account.");
        if(_connection==false)
            throw new ConnectException("No connection.");
        return _userInfo;
    }

    /**
     * 检查ID是否已经注册过
     * @param ID
     * @return
     */
    public boolean CheckID_Unique(String ID){
        String command = "SELECT * FROM Account WHERE ID ="+ID;
        List<String> list = sqlite.QueryString(command);
        if(list==null)
            return true;
        else
            return false;
    }

    /**
     * 检查电子邮箱是否已经注册过
     * @param Email
     * @return
     */
    public boolean CheckEmail_Unique(String Email){
        String command = "SELECT * FROM Account WHERE Email ="+Email;
        List<String> list = sqlite.QueryString(command);
        if(list==null)
            return true;
        else
            return false;
    }


    //发出注册请求（现阶段是直接就注册了，没有服务器）
    //ConnectException:No connection.
    /**
     * 注册
     * @param Name
     * @param Email
     * @param password
     * @throws FormatException,RecordException
     */
    public static void SignUp(Context context, String Name, String Email, char[] password, Bitmap DefaultPhoto) throws FormatException, RecordException {
        if(!Tools.CheckEmailValid(Email))
            throw new FormatException("Email is invalid.");
        sqlite = SQLiteTools.getInstance(context);
        //TODO:应该来自服务器
//        String command = String.format("INSERT INTO Account(UserName,Email,Password) VALUES('%s','%s','%s','%s')",
//                Name,Email,new String(password), Tools.BitmapToString(DefaultPhoto));
//        sqlite.ExecuteSql(command);

        sqlite.insert("Account",Tools.formContentValuesString("UserName","Email","Password","ProfilePhoto"
        ,Name,Email,new String(password), Tools.BitmapToString(DefaultPhoto)));

//        sqlite.insert("Account",Tools.formContentValuesString("UserName","Email","Password"
//        ,Name,Email,new String(password)));

        List<Integer> temp = sqlite.QueryInt("SELECT ID FROM Account Where UserName='"+Name+"'");
        if(temp==null)
            throw new RecordException("Unsuccessful register.");
        int id = temp.get(0);
//        command = String.format("INSERT INTO UserInfo(AccountID,Follows,Followers,Coins,Likes) VALUES(%d,%d,%d,%d,%d)",
//                id,0,0,0,0);
//        sqlite.ExecuteSql(command);
        sqlite.insert("UserInfo",Tools.formContentValuesInt("AccountID","Follows","Followers","Coins","Likes",
                Integer.toString(id),"0","0","0","0"));

//        //TODO:添加增加兴趣的
        ContentValues cv = new ContentValues();
        cv.put("UserID", id);
        cv.put("Category","计算机科学");
        sqlite.insert("UserInterest",cv);

    }


    //ConnectException:No connection.
    //TODO:AccountException:Try logging in with wrong password more than 7 times, please try after 2 hours.
    /**
     * 登陆
     * @param context
     * @param NameorEmail
     * @param password
     * @throws Exception AccountException:No such UserID/Email./AccountException:Password is wrong.
     */
    public void LogIn(Context context, String NameorEmail, char[] password) throws Exception {
        boolean accountValid = false;
        boolean passwordValid = false;
        AccountInfo accountInfo;
        UserInfo userInfo;
        List<String> temp;//ID,UserName,Email,Password
        if(Tools.CheckEmailValid((NameorEmail))){
            temp = sqlite.QueryString("SELECT * FROM Account WHERE Email='"+NameorEmail+"'");
            if(temp!=null)
                accountValid=true;
            else{
                temp = sqlite.QueryString("SELECT * FROM Account WHERE UserName='"+NameorEmail+"'");
                if(temp!=null)
                    accountValid=true;
            }
        }else{
            temp = sqlite.QueryString("SELECT * FROM Account WHERE UserName='"+NameorEmail+"'");
            if(temp!=null)
                accountValid=true;
            else{
                temp = sqlite.QueryString("SELECT * FROM Account WHERE Email='"+NameorEmail+"'");
                if(temp!=null)
                    accountValid=true;
            }
        }
        if(!accountValid)
            throw new AccountException("No such UserID/Email.");
        String pw = new String(password);
        if(!pw.equals((String)temp.get(ConstantValue.TableAccount.Password.ordinal()))){
            throw new AccountException("Password is wrong.");
        }
        accountInfo = new AccountInfo(Integer.parseInt(temp.get(ConstantValue.TableAccount.ID.ordinal())),
                (String)temp.get(ConstantValue.TableAccount.UserName.ordinal()),
                (String)temp.get(ConstantValue.TableAccount.Email.ordinal()),
                Tools.StringToBitmap((String)temp.get(ConstantValue.TableAccount.ProfilePhoto.ordinal())));
        temp = sqlite.QueryString("SELECT * FROM UserInfo WHERE AccountID="+Integer.parseInt(temp.get(0)));
        if(temp==null)
            throw new RecordException("UserInfo is empty.");
        userInfo = new UserInfo(Integer.parseInt(temp.get(ConstantValue.TableUserInfo.Follows.ordinal())),
                Integer.parseInt(temp.get(ConstantValue.TableUserInfo.Followers.ordinal())),
                Integer.parseInt(temp.get(ConstantValue.TableUserInfo.Coins.ordinal())),
                Integer.parseInt(temp.get(ConstantValue.TableUserInfo.Likes.ordinal())));
        //记录登陆状态
        Tools.SaveToPreference(context,accountPreferenceName,accountInfoKey,accountInfo.toJsonString());
        Tools.SaveToPreference(context,accountPreferenceName,userInfoKey,userInfo.toJsonString());
        Tools.SaveToPreference(context,accountPreferenceName,loginStatusKey,new Boolean((true)));
        //修改当前状态
        _loginStatus = true;
        _userInfo = userInfo;
        _accountInfo = accountInfo;
    }

    /**
     * 退出登陆，消除本地的登陆状态
     * @param context
     * @throws Exception RecordException:No valid account has logged in.
     */
    public void LogOut(Context context) throws Exception {
        checkStatus();
        //清除登陆状态
        AccountInfo accountInfo = new AccountInfo();
        UserInfo userInfo = new UserInfo();
        Tools.SaveToPreference(context,accountPreferenceName,accountInfoKey,accountInfo.toJsonString());
        Tools.SaveToPreference(context,accountPreferenceName,userInfoKey,userInfo.toJsonString());
        Tools.SaveToPreference(context,accountPreferenceName,loginStatusKey,new Boolean((false)));
        //修改当前状态
        _loginStatus = false;
        _userInfo = null;
        _accountInfo = null;
    }

    /**
     * 修改用户头像
     * @param context
     * @param NewPhoto
     * @throws Exception
     */
    public void ChangeProfilePhoto(Context context, Bitmap NewPhoto) throws Exception {
        checkStatus();
        _accountInfo.ProfilePhoto = NewPhoto;
        Tools.SaveToPreference(context,accountPreferenceName,accountInfoKey,_accountInfo.toJsonString());
        //TODO:存到服务器
        sqlite.ExecuteSql("UPDATE Account SET ProfilePhoto='"+Tools.BitmapToString(NewPhoto)+"" +
                "' WHERE ID="+_accountInfo.UserID);
    }

    /**
     * 获取用户关注的人
     * @return LinkedList<AccountInfo>用户关注的人的账户信息对象列表
     * @throws RecordException "Follow is empty."
     */
    public List<AccountInfo> GetFollows() throws RecordException {
        checkStatus();
        return GetFollows(_accountInfo.UserID);
    }

    /**
     * 获取用户关注的人
     * @param UserID
     * @return LinkedList<AccountInfo>用户关注的人的账户信息对象列表
     * @throws RecordException "Follow is empty."
     */
    public static List<AccountInfo> GetFollows(int UserID) throws RecordException {
        List<Integer> users = sqlite.QueryInt("SELECT UserID FROM Follow WHERE FollowerID="+UserID);
        if(users==null)
            throw new RecordException("Follow is empty.");
        List<String> temp;
        List<AccountInfo> accounts = new LinkedList<AccountInfo>();
        int id = -1;
        for(int i=0;i<users.size();++i){
            id = users.get(i);
            temp = sqlite.QueryString("SELECT * FROM Account WHERE ID="+id);
            if(temp==null)
                throw new RecordException("Can't find Follow of AccountID="+id);
            accounts.add(new AccountInfo(Integer.parseInt(temp.get(ConstantValue.TableAccount.ID.ordinal())),
                    (String)temp.get(ConstantValue.TableAccount.UserName.ordinal()),
                    (String)temp.get(ConstantValue.TableAccount.Email.ordinal()),
                    Tools.StringToBitmap((String)temp.get(ConstantValue.TableAccount.ProfilePhoto.ordinal()))));
        }
        return accounts;
    }

    /**
     * 获取用户的粉丝
     * @return LinkedList<AccountInfo>用户的粉丝的账户信息对象列表
     * @throws RecordException "Follower is empty."
     */
    public List<AccountInfo> GetFollowers() throws RecordException {
        checkStatus();
        return GetFollowers(_accountInfo.UserID);
    }

    /**
     * 获取用户的粉丝
     * @param UserID
     * @return LinkedList<AccountInfo>用户的粉丝的账户信息对象列表
     * @throws RecordException "Follower is empty."
     */
    public static List<AccountInfo> GetFollowers(int UserID) throws RecordException {
        List<Integer> users = sqlite.QueryInt("SELECT FollowerID FROM Follow WHERE UserID="+UserID);
        if(users==null)
            throw new RecordException("Follower is empty.");
        List<String> temp;
        List<AccountInfo> accounts = new LinkedList<AccountInfo>();
        int id = -1;
        for(int i=0;i<users.size();++i){
            id = users.get(i);
            temp = sqlite.QueryString("SELECT * FROM Account WHERE ID="+id);
            if(temp==null)
                throw new RecordException("Can't find follower of AccountID="+id);
            accounts.add(new AccountInfo(Integer.parseInt(temp.get(ConstantValue.TableAccount.ID.ordinal())),
                    (String)temp.get(ConstantValue.TableAccount.UserName.ordinal()),
                    (String)temp.get(ConstantValue.TableAccount.Email.ordinal()),
                    Tools.StringToBitmap((String)temp.get(ConstantValue.TableAccount.ProfilePhoto.ordinal()))));
        }
        return accounts;
    }

    /**
     * 获取收藏的博文或者是问题
     * @param Type 要获取的内容类型
     * @return LinkedList的Post信息列表
     * @throws RecordException "Favourites is empty."
     */
    public List<Post> GetFavourites(Post.PostType Type) throws RecordException {
        List<Integer> posts;
        if(Type== Post.PostType.Mix){
            posts = sqlite.QueryInt(String.format("SELECT PostID FROM Favourites WHERE UserID='%s'",_accountInfo.UserID));
        }else{
            posts = sqlite.QueryInt(String.format("SELECT PostID FROM Favourites WHERE UserID='%s' AND " +
                    "Type='%s'",_accountInfo.UserID,Type.name()));
        }
        if(posts==null)
            throw new RecordException("Favourites is empty.");

        return fromPostIDtoPostList(posts);
    }

    /**
     * 关注用户
     * @param context
     * @param toFollowID 要关注的用户的ID
     */
    public void Follow(Context context, int toFollowID) throws RecordException {
        checkStatus();
        _userInfo.Follows += 1;
        Tools.SaveToPreference(context,accountPreferenceName,userInfoKey,_userInfo);
        Follow(context,_accountInfo.UserID,toFollowID);
    }

    /**
     * 关注用户(不检查用户是否存在，如果不存在会直接有Sqlite的错误)
     * @param toFollowID
     * @param context
     * @param FollowerID 要关注的用户的ID
     */
    private static void Follow(Context context, int toFollowID, int FollowerID){
        sqlite = SQLiteTools.getInstance(context);
        sqlite.ExecuteSql("UPDATE UserInfo SET Follows=Follows+1 WHERE AccountID="+FollowerID);
        sqlite.ExecuteSql("UPDATE UserInfo SET Followers=Followers+1 WHERE AccountID="+toFollowID);
        //CHECK:Date由SQLite插入默认值
//        sqlite.ExecuteSql(String.format("INSERT INTO Follow(UserID,FollowerID) VALUES(%d,%d)",UserID,_accountInfo.UserID));
        try {
            sqlite.insert("Follow",Tools.formContentValuesInt(
                    "UserID","FollowerID",Integer.toString(FollowerID),Integer.toString(FollowerID)));
        } catch (FormatException e) {
            e.printStackTrace();
        }
    }

    /**
     * 收藏该内容
     * @param PostID 收藏的内容编号
     * @param Type 内容的类型(Post.PostType)
     */
    public void Favourite(int PostID, Post.PostType Type) throws RecordException {
        checkStatus();
        Favourite(_accountInfo.UserID,PostID,Type);
    }

    /**
     * 收藏该内容
     * @param UserID
     * @param PostID 收藏的内容编号
     * @param Type 内容的类型(Post.PostType)
     */
    private static void Favourite(int UserID, int PostID, Post.PostType Type)
    {
//        sqlite.ExecuteSql(String.format("INSERT INTO Favourites(UserID,PostID,Type) VALUES(%d,%d,'%s')",
//                _accountInfo.UserID,PostID,Type.name()));
        ContentValues cv = new ContentValues();
        cv.put("UserID",new Integer(UserID));
        cv.put("PostID",new Integer(PostID));
        cv.put("Type",new Integer(Type.name()));
        sqlite.insert("Favourites",cv);
        //FavouritesRecordID,UserID,PostID,Type
    }

    /**
     * 取消收藏
     * @param PostID 内容编号
     */
    public void RemoveFromFavourites(int PostID) throws RecordException {
        checkStatus();
        RemoveFromFavourites(_accountInfo.UserID,PostID);
    }

    /**
     * 取消收藏
     * @param UserID
     * @param PostID 内容编号
     */
    private static void RemoveFromFavourites(int UserID, int PostID){
        sqlite.ExecuteSql(String.format("DELETE FROM Favourites WHERE UserID=%d AND PostID=%d",
                UserID,PostID));
    }

    /**
     * 获取推荐内容
     * @return
     */
    public List<Post> GetRecommandPosts() throws RecordException {
        checkStatus();
        List<Post> posts = null;
        List<Integer> postids = sqlite.QueryInt("SELECT PostInfo.PostID FROM PostInfo INNER JOIN UserInterest" +
                " ON PostInfo.Category = UserInterest.Category WHERE UserID="+_accountInfo.UserID+" " +
                "ORDER BY PostInfo.Likes,PostInfo.PostID DESC");
        try {
            posts = fromPostIDtoPostList(postids);
        } catch (RecordException e) {
            //不应该发生
            e.printStackTrace();
        }
        return posts;
    }

    //获取新的NumOfQuestions篇推荐问题
    //ConnectException:No connection.

    /**
     * 获取推荐问题
     * @return
     */
    public List<Post> GetRecommandQuestions() throws RecordException {
        checkStatus();
        getSqlite(_context);
        List<Post> posts = null;
        List<Integer> postids = sqlite.QueryInt("SELECT PostID FROM PostInfo INNER JOIN UserInterest" +
                " ON PostInfo.Category = UserInterest.Category WHERE UserID="+_accountInfo.UserID +
                " AND PostInfo.Type='"+ Post.PostType.Question.name() +
                "' ORDER BY PostInfo.Likes,PostInfo.PostID DESC");
        try {
            if(postids == null || postids.size() == 0)
                throw new RecordException("No post valid.");
            posts = fromPostIDtoPostList(postids);
        } catch (RecordException e) {
            //不应该发生
            Log.i(TAG, "GetRecommandQuestions: "+"ERROR??????????????");
            e.printStackTrace();
        }finally {
            return posts;
        }
    }

    /**
     * 获取推荐博文
     * @return
     */
    public List<Post> GetRecommandArticles() throws RecordException {
        checkStatus();
        List<Post> posts = null;
        List<Integer> postids = sqlite.QueryInt("SELECT PostInfo.PostID FROM PostInfo INNER JOIN UserInterest" +
                " ON PostInfo.Category = UserInterest.Category WHERE UserID="+_accountInfo.UserID +
                " AND PostInfo.Type='"+ Post.PostType.Blog.name() +
                "' ORDER BY PostInfo.Likes,PostInfo.PostID DESC");
        try {
            posts = fromPostIDtoPostList(postids);
        } catch (RecordException e) {
            //不应该发生
            e.printStackTrace();
        }
        return posts;
    }

    /**
     * 搜索获取博文或问题或回答或者三者兼有
     * @param SearchCommand
     * @param Type
     * @param NumOfPosts
     * @return
     */
    public static List<Post> Search(String SearchCommand, Post.PostType Type, int NumOfPosts) {
        List<Post> posts = null;
        List<Integer> postids = null;
        if(Type == Post.PostType.Mix){
            postids = sqlite.QueryInt("SELECT PostID FROM PostInfo WHERE Type='" +
                    ""+ Type.name()+"' AND Title LIKE '%"+SearchCommand+"%'");
        }else{
            postids = sqlite.QueryInt("SELECT PostID FROM PostInfo WHERE (Type='" +
                    Post.PostType.Blog.name()+"' OR Type='" +
                    Post.PostType.Question.name()+"' OR Type='" +
                    Post.PostType.Answer.name()+"' OR Type='" +
                    "')" +
                    " AND Title LIKE '%"+SearchCommand+"%'");
        }

        try {
            posts = fromPostIDtoPostList(postids);
        } catch (RecordException e) {
            e.printStackTrace();
        }
        return posts;
    }

    /**
     * 获取PostID对应的内容
     * @param PostID
     * @return
     * @throws RecordException No such PostID.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static TextContent GetPostCotent(int PostID) throws RecordException, IOException, ClassNotFoundException {
        List<String> temp = sqlite.QueryString("SELECT TextContent FROM PostContent WHERE PostID="+PostID);
        if(temp==null)
            throw new RecordException("No such PostID.");

        TextContent context = new TextContent(temp.get(0));
        return context;
    }

    /**
     * 返回Post
     * @param PostID
     * @return
     * @throws RecordException
     */
    public static Post GetPostInfo(int PostID) throws RecordException {
        List<String> temp = sqlite.QueryString("SELECT * FROM PostInfo WHERE PostID="+PostID);
        Post posts = null;
        if(temp==null)
            throw new RecordException("No such PostID.");
        try {
            posts = new Post(
                    Integer.parseInt(temp.get(ConstantValue.TablePostInfo.PostID.ordinal())),
                    temp.get(ConstantValue.TablePostInfo.Title.ordinal()),
                    Post.PostType.valueOf(temp.get(ConstantValue.TablePostInfo.Type.ordinal())),
                    Tools.StringToDate(temp.get(ConstantValue.TablePostInfo.PostDate.ordinal())),
                    Tools.StringToDate(temp.get(ConstantValue.TablePostInfo.LastEditedDate.ordinal())),
                    Post.PostCategory.valueOf(temp.get(ConstantValue.TablePostInfo.Category.ordinal())),
                    Integer.parseInt(temp.get(ConstantValue.TablePostInfo.AuthorID.ordinal())),
                    Integer.parseInt(temp.get(ConstantValue.TablePostInfo.Likes.ordinal())),
                    Integer.parseInt(temp.get(ConstantValue.TablePostInfo.Reads.ordinal())),
                    Integer.parseInt(temp.get(ConstantValue.TablePostInfo.Comments.ordinal())),
                    Tools.CombinedStringToStringArray(temp.get(ConstantValue.TablePostInfo.KeyWords.ordinal()))
            );
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return posts;
    }

    /**
     * 获取PostID对应的评论
     * @param PostID
     * @return
     * @throws RecordException No such PostID.
     */
    public static List<Comment> GetPostComment(int PostID) throws RecordException {
        List<String> temp = sqlite.QueryString("SELECT * FROM Comment WHERE PostID="+PostID);
        if(temp==null)
            throw new RecordException("No comment of post which PostID="+PostID);
        List<Comment> comments = new LinkedList<Comment>();
        for(int i=0;i<temp.size();++i){
            try {
                    comments.add(new Comment(
                            Integer.parseInt(temp.get(i + ConstantValue.TableComment.CommentID.ordinal())),
                            Integer.parseInt(temp.get(i + ConstantValue.TableComment.PostID.ordinal())),
                            Integer.parseInt(temp.get(i + ConstantValue.TableComment.UserID.ordinal())),
                            Tools.StringToDate(temp.get(i + ConstantValue.TableComment.PostDate.ordinal())),
                            Integer.parseInt(temp.get(i + ConstantValue.TableComment.Likes.ordinal())),
                            temp.get(i + ConstantValue.TableComment.Content.ordinal())
                    ));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            i += ConstantValue.TableComment.values().length;
        }
        return comments;
    }

    /**
     * 发表文章或问题id为PostID的内容的评论
     * @param PostID
     * @param CommentText
     * @throws Exception Comment too long. No valid login.
     */
    public void PostComment(int PostID, String CommentText) throws Exception {
        checkStatus();
        PostComment(_accountInfo.UserID,PostID,CommentText);
    }

    /**
     * 发表文章或问题id为PostID的内容的评论
     * @param UserID
     * @param PostID
     * @param CommentText
     * @throws Exception Comment too long. No valid login.
     */
    private static void PostComment(int UserID, int PostID, String CommentText) throws Exception {
        if(CommentText.length()>=400)
            throw new Exception("Comment length exceed 400.");
        //CHECK:这里是否能够自动生成日期和likes
        //CommentID,PostID,UserID,PostDate,Likes,Content
//        sqlite.ExecuteSql(String.format("INSERT INTO Comment(PostID,UserID,Content) VALUES (%d,%d,%s"
//                ,PostID,_accountInfo.UserID,CommentText));
        ContentValues cv = new ContentValues();
        cv.put("PostID",PostID);
        cv.put("UserID",UserID);
        cv.put("Content",CommentText);
        sqlite.insert("Comment",cv);
    }

    /**
     * 删除评论
     * @param CommentID
     */
    public void DeleteComment(int CommentID) throws RecordException {
        checkStatus();
        sqlite.ExecuteSql(String.format("DELETE FROM Comment WHERE CommentID=%d AND UserID=%d",
                CommentID,_accountInfo.UserID));
    }

    /**
     * 删除内容//CHECK:是否能级联删除
     * @param PostID
     * @throws RecordException
     */
    public void DeletePost(int PostID) throws RecordException {
        checkStatus();
        sqlite.ExecuteSql(String.format("DELETE FROM PostInfo WHERE PostID=%d AND AuthorID=%d"
                ,PostID,_accountInfo.UserID));
    }

    /**
     * 发表博文或者问题.
     * @param Title 标题
     * @param _Content 内容
     * @param _Category 分类
     * @param KeyWords 关键词
     * @param _PostType 博文还是提问
     * @return 返回刚刚插入的Post的编号
     * @throws RecordException No valid login.
     * @throws FormatException Invalid PostType.
     */
    public int PostArticle(Context context, String Title, TextContent _Content,
                            Post.PostCategory _Category, String[] KeyWords, Post.PostType _PostType) throws RecordException, FormatException {
        checkStatus();
        return PostArticle(context,_accountInfo.UserID,Title,_Content,_Category,KeyWords,_PostType);
    }

    /**
     * 发表博文或者问题.
     * @param UserID 发表的作者ID
     * @param Title 标题
     * @param _Content 内容
     * @param _Category 分类
     * @param KeyWords 关键词
     * @param _PostType 博文还是提问
     * @return 返回刚刚插入的Post的编号
     * @throws RecordException No valid login.
     * @throws FormatException Invalid PostType.
     */
    private static int PostArticle(Context context, int UserID,String Title, TextContent _Content,
                            Post.PostCategory _Category, String[] KeyWords, Post.PostType _PostType) throws RecordException, FormatException {

        if(_PostType == Post.PostType.Mix)
            throw new FormatException("Invalid PostType.");
        sqlite = SQLiteTools.getInstance(context);

        //创建新的PostInfo
        //PostID,Title,Type,PostDate,LastEditedDate,Category,KeyWords,AuthorID,Likes,Reads,Comments
        //CHECK:这里是否能够自动生成PostID/PostDate/LastEditedDate/Likes/Reads/Comments
//        sqlite.ExecuteSql(String.format("INSERT INTO PostInfo(Title,Type,Category,KeyWords,AuthorID) " +
//                "VALUES(%s,%s,%s,%s,%d)",
//                Title,_PostType.name(), _Category.name(),Tools.StringArrayToString(KeyWords),
//                _accountInfo.UserID));
        ContentValues cv = new ContentValues();
        cv.put("Title",Title);
        cv.put("Type",_PostType.name());
        cv.put("Category",_Category.name());
        cv.put("KeyWords",Tools.StringArrayToString(KeyWords));
        cv.put("AuthorID",UserID);
        sqlite.insert("PostInfo",cv);

        List<Integer> ltmp = sqlite.QueryInt(String.format("SELECT MAX(PostID) FROM PostInfo" +
                " WHERE Title='%s' ORDER BY PostDate DESC",Title));
        //CHECK:ID是否正确
        int postID = ltmp.get(0);
        //创建新的文章内容
        //ContentID,PostID,TextContent
        try {
//            sqlite.ExecuteSql(String.format("INSERT INTO PostContent(PostID,TextContent) " +
//                    "VALUES(%d,%s)",postID,Tools.ObjectToString(_Content)));
            ContentValues cv1 = new ContentValues();
            cv1.put("PostID",postID);
            cv1.put("TextContent",_Content.toJsonString());
            sqlite.insert("PostContent",cv1);

            if(_PostType == Post.PostType.Question){
                //ID,PostID,Answers,Status
                //CHECK:Answers,Status,StatisfiedAnswerID能否由默认值填入
//                sqlite.ExecuteSql(String.format("INSERT INTO QuestionInfo(PostID) " +
//                        "VALUES(%d)",postID));
                sqlite.insert("QuestionInfo",Tools.formContentValuesInt("PostID",Integer.toString(postID)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return postID;
    }

    /**
     * 关闭问题
     * @param QuestionID->PostID
     * @throws RecordException No valid account has logged in.
     */
    public void CloseQuestion(int QuestionID) throws RecordException {
        checkStatus();
        sqlite.ExecuteSql(String.format("UPDATE QuestionInfo SET Status='%s' WHERE PostID=%d",
                Question.QuestionStatus.Closed.name(),_accountInfo.UserID));
    }

    /**
     * 接受问题
     * @param QuestionID
     * @param SatisfiedAnswerID
     * @throws RecordException No valid account has logged in.
     */
    public void AcceptQuestion(int QuestionID, Integer[] SatisfiedAnswerID) throws RecordException {
        checkStatus();
        AcceptQuestion(_accountInfo.UserID,QuestionID,SatisfiedAnswerID);
    }

    /**
     * 接受问题
     * @param userID
     * @param QuestionID
     * @param SatisfiedAnswerID
     * @throws RecordException No valid account has logged in.
     */
    private static void AcceptQuestion(int userID, int QuestionID, Integer[] SatisfiedAnswerID) throws RecordException {
        List<Integer> temp = sqlite.QueryInt("SELECT PostID FROM PostInfo WHERE AuthorID="+userID+" AND PostID="+QuestionID);
        if(temp!=null||temp.size()!=0)
            throw new RecordException("No valid question.");
        sqlite.ExecuteSql(String.format("UPDATE QuestionInfo SET Status='%s',StatisfiedAnswerIDs='%s' WHERE PostID=%d",
                Question.QuestionStatus.Accepted.name(),Tools.IntArrayToString(SatisfiedAnswerID),QuestionID));
    }

    /**
     * 获取问题对应的答案.
     * @param QuestionID
     * @return
     * @throws RecordException Can't find xxx.
     */
    public static List<Post> GetAnswers(int QuestionID) throws RecordException {
        List<Integer> PostIDs = sqlite.QueryInt("SELECT DISTINCT AnswerID FROM AnswerInfo WHERE QuestionID="+QuestionID);
        return fromPostIDtoPostList(PostIDs);
    }

    /**
     * 回答问题
     * @param AnswerTitle 回答也要起一个标题
     * @param QuestionID 回答的问题的ID
     * @param _Content 回答的内容
     * @return postid
     * @throws RecordException
     */
    public int Answer(Context context,String AnswerTitle, int QuestionID, TextContent _Content) throws RecordException{
        checkStatus();
        return Answer(context,_accountInfo.UserID,AnswerTitle,QuestionID,_Content);
    }

    /**
     * 回答问题
     * @param UserID
     * @param AnswerTitle 回答也要起一个标题
     * @param QuestionID 回答的问题的ID
     * @param _Content 回答的内容
     * @return postid
     * @throws RecordException
     */
    private static int Answer(Context context,int UserID, String AnswerTitle, int QuestionID, TextContent _Content) throws RecordException{
        sqlite = SQLiteTools.getInstance(context);
        List<Integer> tmpL = new LinkedList<Integer>();
        tmpL.add(QuestionID);
        Question tmpq = (Question)fromPostIDtoPostList(tmpL).get(0);
        int postID = -1;
        try {
            postID = PostArticle(context,UserID, AnswerTitle,_Content, tmpq.Category,tmpq.KeyWords, Post.PostType.Answer);

            //ID,QuestionID,AnswerID
//        sqlite.ExecuteSql(String.format("INSERT INTO AnswerInfo(QuestionID,AnswerID) " +
//                "VALUES(%d,%d)", tmpq.ID,postID));
            sqlite.insert("AnswerInfo",Tools.formContentValuesInt("QuestionID","AnswerID",
                    Integer.toString(tmpq.ID),Integer.toString(postID)));
            sqlite.ExecuteSql(String.format("UPDATE QuestionInfo SET Answers=%d WHERE PostID=%d",
                    tmpq.Answers+1,tmpq.ID));

            //PostArticle(String Title, TextContent _Content,
            //       Post.PostCategory _Category, String[] KeyWords, Post.PostType _PostType)
        } catch (FormatException e) {
            e.printStackTrace();
        }
        return postID;
    }

    /**
     * 赞同内容
     * @param PostID
     * @throws RecordException
     */
    public void Like(int PostID) throws RecordException {
        checkStatus();
        Like(_accountInfo.UserID,PostID);
    }

    /**
     * 赞同内容
     * @param UserID
     * @param PostID
     * @throws RecordException
     */
    private static void Like(int UserID, int PostID) throws RecordException {
        if(DoILikeThis(UserID,PostID))
            return;
        try {
//        sqlite.ExecuteSql(String.format("INSERT INTO Likes(PostID,UserID) VALUES(%d,d)",
//                PostID,_accountInfo.UserID));
            sqlite.insert("Likes",Tools.formContentValuesInt("PostID","UserID",
                    Integer.toString(PostID),Integer.toString(UserID)));

            sqlite.ExecuteSql(String.format("UPDATE PostInfo SET Likes=Likes+1 WHERE PostID=%d",PostID));
        } catch (FormatException e) {
            e.printStackTrace();
        }

    }

    /**
     * 反对内容
     * @param PostID
     * @throws RecordException
     */
    public void Disapprove(int PostID) throws RecordException {
        checkStatus();
        Disapprove(_accountInfo.UserID,PostID);
    }

    /**
     * 反对内容
     * @param UserID
     * @param PostID
     * @throws RecordException
     */
    private static void Disapprove(int UserID, int PostID) throws RecordException {
        if(DoIDisapproveThis(UserID,PostID))
            return;

        try {
//        sqlite.ExecuteSql(String.format("INSERT INTO Disapproves(PostID,UserID) VALUES(%d,d)",
//                PostID,_accountInfo.UserID));
            sqlite.insert("Disapproves",Tools.formContentValuesInt("PostID","UserID",
                    Integer.toString(PostID),Integer.toString(UserID)));
        } catch (FormatException e) {
            e.printStackTrace();
        }
        sqlite.ExecuteSql(String.format("UPDATE PostInfo SET Likes=Likes-1 WHERE PostID=%d",PostID));
    }

    /**
     * 查看我赞成这个内容吗
     * @param PostID
     * @return
     * @throws RecordException
     */
    public boolean DoILikeThis(int PostID) throws RecordException {
        checkStatus();
        return DoILikeThis(_accountInfo.UserID,PostID);
    }

    /**
     * 查看我赞成这个内容吗
     * @param UserID
     * @param PostID
     * @return
     * @throws RecordException
     */
    private static boolean DoILikeThis(int UserID, int PostID) throws RecordException {
        List<String> list = sqlite.QueryString(String.format("SELECT * FROM Likes WHERE PostID=%d AND UserID=%d",
                PostID,UserID));
        if(list==null)
            return false;
        else
            return true;
    }

    /**
     * 我反对这个内容吗
     * @param PostID
     * @return
     * @throws RecordException
     */
    public boolean DoIDisapproveThis(int PostID) throws RecordException {
        checkStatus();
        return DoIDisapproveThis(_accountInfo.UserID,PostID);
    }

    /**
     * 我反对这个内容吗
     * @param UserID
     * @param PostID
     * @return
     * @throws RecordException
     */
    private static boolean DoIDisapproveThis(int UserID, int PostID) throws RecordException {
        List<String> list = sqlite.QueryString(String.format("SELECT * FROM Disapproves WHERE PostID=%d AND UserID=%d",
                PostID,UserID));
        if(list==null)
            return false;
        else
            return true;
    }

    /**
     * 获取我发表的内容
     * @return
     * @throws RecordException No login./Post is empty.
     */
    public List<Post> MyPost() throws RecordException {
        checkStatus();
        List<Integer> postIDs = sqlite.QueryInt(String.format("SELECT PostID FROM PostInfo WHERE AuthorID=%d",
                _accountInfo.UserID));
        if(postIDs == null||postIDs.size() == 0)
            throw new RecordException("Post is empty.");
        return fromPostIDtoPostList(postIDs);
    }

    /**
     * 获取用户信息
     * @param ID
     * @return
     * @throws RecordException "No such user empty."
     */
    public static AccountInfo getAccountInfo(int ID) throws RecordException {
        List<String> info = sqlite.QueryString("SELECT ID,Email,UserName,ProfilePhoto FROM Account WHERE ID="+ID);
        if(info == null||info.size() == 0)
            throw new RecordException("No such user empty.");
        return new AccountInfo(Integer.parseInt(info.get(0)),
                info.get(1),
                info.get(2),
                Tools.StringToBitmap(info.get(3)));
    }


}
