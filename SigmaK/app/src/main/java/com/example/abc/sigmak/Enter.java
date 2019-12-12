package com.example.abc.sigmak;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;

import com.romainpiel.titanic.library.Titanic;
import com.romainpiel.titanic.library.TitanicTextView;

public class Enter extends AppCompatActivity {
    TitanicTextView textView;
    TitanicTextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter);
        textView=(TitanicTextView)findViewById(R.id.text);
        textView.setTypeface(Typefaces.get(this,"Satisfy-Regular.ttf"));
        textView.setText("SigmaK");
        Titanic titanic=new Titanic();
        titanic.start(textView);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_UP:
                finish();
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}
