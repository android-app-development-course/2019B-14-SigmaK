package com.example.abc.sigmak;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.rubensousa.raiflatbutton.RaiflatButton;
import com.romainpiel.titanic.library.Titanic;
import com.romainpiel.titanic.library.TitanicTextView;

public class Enter extends AppCompatActivity {
    private TitanicTextView textView;
    private TitanicTextView title;
    private RaiflatButton Create;
    private TextView Sign;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter);
        textView=(TitanicTextView)findViewById(R.id.text);
        textView.setTypeface(Typefaces.get(this,"Satisfy-Regular.ttf"));
        textView.setText("SigmaK");
        Titanic titanic=new Titanic();
        titanic.start(textView);
        Create=(RaiflatButton)findViewById(R.id.create);
        Create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(Enter.this,Login.class);//this前面为当前activty名称，class前面为要跳转到得activity名称
                intent.putExtra("flag",1);//为1时创建账号
                startActivity(intent);
                finish();
            }
        });
        Sign=(TextView)findViewById(R.id.sign);
        Sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(Enter.this,Login.class);//this前面为当前activty名称，class前面为要跳转到得activity名称
                intent.putExtra("flag",0);//为0时登陆账号
                startActivity(intent);
                finish();
            }
        });
    }

   /* @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_UP:
                finish();
                Intent intent = new Intent();
                intent.setClass(Enter.this,Login.class);//this前面为当前activty名称，class前面为要跳转到得activity名称
                startActivity(intent);
                break;
        }
        return super.dispatchTouchEvent(ev);
    }*/
}
