package com.example.abc.sigmak;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.nfc.FormatException;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.abc.sigmak.Exceptions.RecordException;
import com.example.abc.sigmak.Utility.Manager;
import com.github.rubensousa.raiflatbutton.RaiflatButton;
import com.mikepenz.materialize.color.Material;
import com.rengwuxian.materialedittext.MaterialEditText;

public class Login extends AppCompatActivity {
    int Flag;
    RaiflatButton button1;
    MaterialEditText email;
    MaterialEditText Username;
    MaterialEditText Password;
    TextView change;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final Manager manager;
        manager=Manager.getInstance(this.getApplicationContext());
        manager.CreateTestData(this.getApplicationContext());
//        new DataOperateTask().execute(this.getApplicationContext());
        Flag=getIntent().getIntExtra("flag",0);//0登陆 1创建

        button1=(RaiflatButton)findViewById(R.id.Login);
        email=(MaterialEditText)findViewById(R.id.Email);
        Username=(MaterialEditText)findViewById(R.id.UserName);
        Password=(MaterialEditText)findViewById(R.id.Password);
        change=(TextView)findViewById(R.id.change);

        if(Flag==1)
        {
            Register();
        }
        else
        {
            Login();
        }

        final Bitmap bitmap=((BitmapDrawable)getResources().getDrawable(R.drawable.user)).getBitmap();

        final AlertDialog.Builder builder= new AlertDialog.Builder(Login.this);
        builder.setTitle("Warning");
        builder.setNeutralButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });//AlertDialog

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Flag==1)
                {
                    Flag=0;
                    Login();
                }
                else
                {
                    Flag=1;
                    Register();
                }
            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Flag==1)
                {
                    try {
                        manager.SignUp(getApplicationContext(), Username.getText().toString(),email.getText().toString(),Password.getText().toString().toCharArray(),bitmap);
                    } catch (FormatException e) {
                        builder.setMessage(e.getMessage());
                        builder.show();
                        return;
                    } catch (RecordException e) {
                        builder.setMessage(e.getMessage()+"SignUp Fail");
                        builder.show();
                        return;
                    }
                }//注册
                else
                {

                    try {
                        manager.LogIn(view.getContext(),Username.getText().toString(),Password.getText().toString().toCharArray());
                    } catch (Exception e) {
                        builder.setMessage("Account doesn't exist");
                        builder.show();
                        return;
                    }

                }//是否注册或登陆成功
                finish();
            }
        });
    }
    void Login()
    {
        button1.setText(R.string.Login);
        Username.setHint("Username/Email");
        change.setText(R.string.Register);
        email.setVisibility(email.GONE);
        Username.setText("");
        Password.setText("");
    }
    void Register()
    {
        Username.setHint("Username");
        button1.setText(R.string.Register);
        email.setVisibility(email.VISIBLE);
        change.setText(R.string.Login);
        Username.setText("");
        Password.setText("");
    }
}
