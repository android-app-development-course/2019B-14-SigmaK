package com.example.abc.sigmak.Utility;

import android.content.Context;
import android.graphics.Bitmap;
import android.nfc.FormatException;

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

import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.example.abc.sigmak.Utility.Tools;

public class Manager {


    private AccountInfo _accountInfo;
    private UserInfo _userInfo;
    private boolean _loginStatus = false;
    private boolean _connection = true;//为以后预留
    private String accountPreferenceName = "Account";
    private String accountInfoKey = "AccountInfo";
    private String loginStatusKey = "LoginStatus";
    private String userInfoKey = "UserInfo";


    private static SQLiteTools sqlite = null;

    private static final Manager ourInstance = new Manager();

    public static Manager getInstance(Context context) {
        sqlite = SQLiteTools.getInstance(context);
        return ourInstance;
    }

    private Manager() {
    }

    private void checkStatus() throws RecordException {
        if(!_loginStatus)
            throw new RecordException("No valid account has logged in.");
    }

    /**
     * 初始化本机记录的登录信息
     * @param context
     * @throws IOException
     */
    private void loadLoginInfo(Context context) throws IOException {
        try {
            _loginStatus = (boolean)Tools.ReadFromPreference(context,accountPreferenceName,loginStatusKey,new Boolean(false));
            _accountInfo = (AccountInfo) Tools.ReadObjectFromPreference(context,accountPreferenceName, accountInfoKey);
            _userInfo = (UserInfo) Tools.ReadObjectFromPreference(context,accountPreferenceName,userInfoKey);
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        List<Object> list = sqlite.rawQuery(command);
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
        List<Object> list = sqlite.rawQuery(command);
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
    public void SignUp(String Name, String Email, char[] password, Bitmap DefaultPhoto) throws FormatException, RecordException {
        if(!Tools.CheckEmailValid(Email))
            throw new FormatException("Email is invalid.");
        //TODO:应该来自服务器
        String command = String.format("INSERT INTO Account(UserName,Email,Password) VALUES('%s','%s','%s','%s')",
                Name,Email,new String(password), Tools.ChangBitmapToString(DefaultPhoto));
        sqlite.ExecuteSql(command);
        List<Object> temp = sqlite.rawQuery("SELECT ID FROM Account Where User='"+Name+"'");
        if(temp==null)
            throw new RecordException("Unsuccessful register.");
        int id = (int)temp.get(ConstantValue.TableAccount.ID.ordinal());
        command = String.format("INSERT INTO UserInfo(AccountID,Follows,Followers,Coins,Likes) VALUES(%d,%d,%d,%d,%d)",
                id,0,0,0,0);
        sqlite.ExecuteSql(command);
    }

    //登陆
    //ConnectException:No connection.
    //AccountException:No such UserID/Email.
    //AccountException:Password is wrong.
    //TODO:AccountException:Try logging in with wrong password more than 7 times, please try after 2 hours.
    /**
     * 登陆
     * @param context
     * @param NameorEmail
     * @param password
     * @throws Exception
     */
    public void LogIn(Context context, String NameorEmail, char[] password) throws Exception {
        boolean accountValid = false;
        boolean passwordValid = false;
        AccountInfo accountInfo;
        UserInfo userInfo;
        List<Object> temp;//ID,UserName,Email,Password
        if(Tools.CheckEmailValid((NameorEmail))){
            temp = sqlite.rawQuery("SELECT * FROM Account WHERE Email='"+NameorEmail+"'");
            if(temp!=null)
                accountValid=true;
            else{
                temp = sqlite.rawQuery("SELECT * FROM Account WHERE UserName='"+NameorEmail+"'");
                if(temp!=null)
                    accountValid=true;
            }
        }else{
            temp = sqlite.rawQuery("SELECT * FROM Account WHERE UserName='"+NameorEmail+"'");
            if(temp!=null)
                accountValid=true;
            else{
                temp = sqlite.rawQuery("SELECT * FROM Account WHERE Email='"+NameorEmail+"'");
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
        accountInfo = new AccountInfo((int)temp.get(ConstantValue.TableAccount.Password.ordinal()),
                (String)temp.get(ConstantValue.TableAccount.UserName.ordinal()),
                (String)temp.get(ConstantValue.TableAccount.Email.ordinal()),
                Tools.ChangeStringToBitmap((String)temp.get(ConstantValue.TableAccount.ProfilePhoto.ordinal())));
        temp = sqlite.rawQuery("SELECT * FROM UserInfo WHERE AccountID="+(int)temp.get(0));
        if(temp==null)
            throw new RecordException("UserInfo is empty.");
        userInfo = new UserInfo((int)temp.get(ConstantValue.TableUserInfo.Follows.ordinal()),
                (int)temp.get(ConstantValue.TableUserInfo.Followers.ordinal()),
                (int)temp.get(ConstantValue.TableUserInfo.Coins.ordinal()),
                (int)temp.get(ConstantValue.TableUserInfo.Likes.ordinal()));
        //记录登陆状态
        Tools.SaveObjectToPreference(context,accountPreferenceName,accountInfoKey,accountInfo);
        Tools.SaveObjectToPreference(context,accountPreferenceName,userInfoKey,userInfo);
        Tools.SaveToPreference(context,accountPreferenceName,loginStatusKey,new Boolean((true)));
        //修改当前状态
        _loginStatus = true;
        _userInfo = userInfo;
        _accountInfo = accountInfo;
    }

    //退出登陆
    //RecordException:No valid account has logged in.
    /**
     * 退出登陆，消除本地的登陆状态
     * @param context
     * @throws Exception
     */
    public void LogOut(Context context) throws Exception {
        checkStatus();
        //清除登陆状态
        AccountInfo accountInfo = new AccountInfo();
        UserInfo userInfo = new UserInfo();
        Tools.SaveObjectToPreference(context,accountPreferenceName,accountInfoKey,accountInfo);
        Tools.SaveObjectToPreference(context,accountPreferenceName,userInfoKey,userInfo);
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
        Tools.SaveObjectToPreference(context,accountPreferenceName,accountInfoKey,_accountInfo);
        //TODO:存到服务器
        sqlite.ExecuteSql("UPDATE Account SET ProfilePhoto='"+Tools.ChangBitmapToString(NewPhoto)+"" +
                "' WHERE ID="+_accountInfo.UserID);
    }

    /**
     * 获取用户关注的人
     * @return LinkedList<AccountInfo>用户关注的人的账户信息对象列表
     * @throws RecordException "Follow is empty."
     */
    public List<AccountInfo> GetFollows() throws RecordException {
        checkStatus();
        List<Object> users = sqlite.rawQuery("SELECT UserID FROM Follow WHERE FollowerID="+_accountInfo.UserID);
        if(users==null)
            throw new RecordException("Follow is empty.");
        List<Object> temp;
        List<AccountInfo> accounts = new LinkedList<AccountInfo>();
        int id = -1;
        for(int i=0;i<users.size();++i){
            id = (int)users.get(i);
            temp = sqlite.rawQuery("SELECT * FROM Account WHERE ID="+id);
            if(temp==null)
                throw new RecordException("Can't find Follow of AccountID="+id);
            accounts.add(new AccountInfo((int)temp.get(ConstantValue.TableAccount.ID.ordinal()),
                    (String)temp.get(ConstantValue.TableAccount.UserName.ordinal()),
                    (String)temp.get(ConstantValue.TableAccount.Email.ordinal()),
                    Tools.ChangeStringToBitmap((String)temp.get(ConstantValue.TableAccount.ProfilePhoto.ordinal()))));
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
        List<Object> users = sqlite.rawQuery("SELECT FollowerID FROM Follow WHERE UserID="+_accountInfo.UserID);
        if(users==null)
            throw new RecordException("Follower is empty.");
        List<Object> temp;
        List<AccountInfo> accounts = new LinkedList<AccountInfo>();
        int id = -1;
        for(int i=0;i<users.size();++i){
            id = (int)users.get(i);
            temp = sqlite.rawQuery("SELECT * FROM Account WHERE ID="+id);
            if(temp==null)
                throw new RecordException("Can't find follower of AccountID="+id);
            accounts.add(new AccountInfo((int)temp.get(ConstantValue.TableAccount.ID.ordinal()),
                    (String)temp.get(ConstantValue.TableAccount.UserName.ordinal()),
                    (String)temp.get(ConstantValue.TableAccount.Email.ordinal()),
                    Tools.ChangeStringToBitmap((String)temp.get(ConstantValue.TableAccount.ProfilePhoto.ordinal()))));
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
        List<Post> list = new LinkedList<Post>();
        List<Object> posts;
        List<Object> temp;
        if(Type== Post.PostType.Mix){
            posts = sqlite.rawQuery(String.format("SELECT PostID FROM Favourites WHERE UserID='%s'",_accountInfo.UserID));
        }else{
            posts = sqlite.rawQuery(String.format("SELECT PostID FROM Favourites WHERE UserID='%s' AND " +
                    "Type='%s'",_accountInfo.UserID,Type.name()));
        }
        if(posts==null)
            throw new RecordException("Favourites is empty.");
        for(int i=0;i<posts.size();++i){
            temp = sqlite.rawQuery("SELECT * FROM Post WHERE PostID="+(int)posts.get(i));
            if(temp==null)
                throw new RecordException("Can't find postInfo of PostID="+(int)posts.get(i));
            list.add(new Post((int)temp.get(ConstantValue.TablePostInfo.PostID.ordinal()),
                    (String)temp.get(ConstantValue.TablePostInfo.Title.ordinal()),
                    (Date)temp.get(ConstantValue.TablePostInfo.PostDate.ordinal()),
                    (int)temp.get(ConstantValue.TablePostInfo.AuthorID.ordinal()),
                    (int)temp.get(ConstantValue.TablePostInfo.Likes.ordinal()),
                    (int)temp.get(ConstantValue.TablePostInfo.Reads.ordinal()),
                    (int)temp.get(ConstantValue.TablePostInfo.Comments.ordinal())
            ));
        }

        return list;
    }

    /**
     * 关注用户
     * @param context
     * @param UserID 要关注的用户的ID
     */
    public void Follow(Context context, int UserID){
        _userInfo.Follows += 1;
        Tools.SaveToPreference(context,accountPreferenceName,userInfoKey,_userInfo);
        sqlite.ExecuteSql("UPDATE UserInfo SET Follows="+_userInfo.Follows +
                " WHERE AccountID="+_accountInfo.UserID);
        //CHECK:Date由SQLite插入默认值
        sqlite.ExecuteSql(String.format("INSERT INTO Follow(UserID,FollowerID) VALUES(%d,%d)",UserID,_accountInfo.UserID));
    }

    /**
     * 收藏该内容
     * @param PostID 收藏的内容编号
     * @param Type 内容的类型(Post.PostType)
     */
    public void Favourite(int PostID, Post.PostType Type){
        sqlite.ExecuteSql(String.format("INSERT INTO Favourites(UserID,PostID,Type) VALUES(%d,%d,'%s')",
                _accountInfo.UserID,PostID,Type.name()));
        //FavouritesRecordID,UserID,PostID,Type
    }

    /**
     * 取消收藏
     * @param PostID 内容编号
     */
    public void RemoveFromFavourites(int PostID){
        sqlite.ExecuteSql(String.format("DELETE FROM Favourites WHERE UserID=%d AND PostID=%d",
                _accountInfo.UserID,PostID));
    }

    //获取新的NumOfPosts篇推荐内容
    //ConnectException:No connection.
    public static List<Post> GetRecommandPosts(int NumOfPosts){
        //TODO:Implement
        return null;
    }

    //获取新的NumOfQuestions篇推荐问题
    //ConnectException:No connection.
    public static List<Question> GetRecommandQuestions(int NumOfQuestions){return null;}

    //获取新的NumOfArticles篇推荐博文
    //ConnectException:No connection.
    public static List<Article> GetRecommandArticles(int NumOfArticles){return null;}

    //搜索获取博文或问题或者两者兼有
    //ConnectException:No connection.
    public static List<Post> Search(String SearchCommand, Post.PostType Type, int NumOfPosts){
        return null;
    }

    //获取上一次搜索获取博文或问题或者两者兼有剩下的结果
    //ConnectException:No connection.
    //ExecutionException: No search executed.
    //RecordException:No more qualified post.
    public static List<Post> GetMoreSearchResult(int NumOfPosts){return null;}

    //获取PostID对应的内容
    //RecordException:No such PostID.
    public static TextContent GetPostCotent(int PostID) throws RecordException, IOException, ClassNotFoundException {
        List<Object> temp = sqlite.rawQuery("SELECT TextContent FROM PostContent WHERE PostID="+PostID);
        if(temp==null)
            throw new RecordException("No such PostID.");

        TextContent context = (TextContent)Tools.ChangeStringToObject((String)temp.get(0));
        return context;
    }
    /下了 晚安w

    //获取PostID对应的评论
    //RecordException:No such PostID.
    public static List<Comment> GetPostComment(int PostID){return null;}

    //发表PostID内容的评论
    //RecordException:No valid login.
    //ExecutionException: Comment too long.
    public static void PostComment(int CommentOfPostID, char[] CommentText){}

    //发表博文
    //RecordException:No valid login.
    //ExecutionException: .
    public static void PostComment(String Title, TextContent Content,
                                   Article.BlogCategory Category, String[] KeyWords){}

    //发表问题
    //RecordException:No valid login.
    //ExecutionException: .
    public static void PostQuestion(String Title, TextContent Content,
                                    Article.BlogCategory Category, String[] KeyWords){}

    //关闭问题
    public static void CloseQuestion(int QuestionID){}

    //接受问题
    public static void AcceptQuestion(int QuestionID, int[] SatisfiedAnswerID){}

    //获取问题对应的答案
    public static List<Answer> GetAnswers(int QuestionID){return null;}

}
