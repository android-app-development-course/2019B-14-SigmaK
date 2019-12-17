package com.example.abc.sigmak;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.github.rubensousa.raiflatbutton.RaiflatButton;
import com.mikepenz.materialize.color.Material;
import com.rengwuxian.materialedittext.MaterialEditText;

public class Login extends AppCompatActivity {
    int Flag;
    RaiflatButton button1;
    MaterialEditText email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
       Flag=getIntent().getIntExtra("flag",0);//0登陆 1创建
        button1=(RaiflatButton)findViewById(R.id.Login);
        email=(MaterialEditText)findViewById(R.id.Email);
        if(Flag==1)
        {
            button1.setText(R.string.Register);
            email.setVisibility(email.VISIBLE);
        }
        else
        {
            button1.setText(R.string.Login);
        }
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
