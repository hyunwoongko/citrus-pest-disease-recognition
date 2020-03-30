package com.hyunwoong.pestsaver.util.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * @author : Hyunwoong
 * @when : 2019-11-15 오후 7:31
 * @homepage : https://github.com/gusdnd852
 */
@SuppressLint("AppCompatCustomView")
public class TypeWriter extends TextView {
//글씨를 타자기처럼 한글자 한글자 순서대로 적어주는 커스텀뷰

    private CharSequence mText;
    private int mIndex;
    private long mDelay = 100; // ms
    private Handler mHandler = new Handler();
    private Runnable characterAdder = new Runnable() {
        @Override
        public void run() {
            setText(mText.subSequence(0, mIndex++));
            if (mIndex <= mText.length()) {
                mHandler.postDelayed(characterAdder, mDelay);
            }
        }
    };

    public TypeWriter(Context context) {
        super(context);
    }

    public TypeWriter(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void animateText(CharSequence txt) {
        mText = txt;
        mIndex = 0;
        mHandler.removeCallbacks(characterAdder);
        mHandler.postDelayed(characterAdder, mDelay);
    }

    private void setCharacterDelay(long m) {
        mDelay = m;
    }

    public void write(String text, int speed) {
        setText(""); // 글자를 지워줌
        setCharacterDelay(speed); // 딜레이값 설정
        animateText(text); // 글씨를 한글자씩 적어주는 메소드
    }
}