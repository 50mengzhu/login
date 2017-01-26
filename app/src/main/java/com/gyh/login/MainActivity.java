package com.gyh.login;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

public class MainActivity extends AppCompatActivity {

    private ImageView bgPicImg;
    private LinearLayout registPart;
    private LinearLayout loginPart;

    private int height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_main);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        bgPicImg = (ImageView) findViewById(R.id.bg_img);
        String bgPic = prefs.getString("bg_pic", null);
        if (bgPic != null) {
            Glide.with(this).load(bgPic).into(bgPicImg);
        } else {
            loadBgPic();
        }

        registPart = (LinearLayout) findViewById(R.id.regist_part);
        loginPart = (LinearLayout) findViewById(R.id.login_part);
        height = registPart.getHeight();
    }

    /**
     * 为以后网络接收图片做接口
     */
    private void loadBgPic() {
        Glide.with(this).load(R.drawable.bg).into(bgPicImg);
    }

    /**
     *
     * @param view
     */
    public void logIn(View view) {
        if (Build.VERSION.SDK_INT >= 16) {
            registPart.animate().alpha(0).translationY(1000).setDuration(500)
                    .withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            registPart.setVisibility(View.GONE);
                        }
                    }).start();
        } else {
            registPart.setVisibility(View.GONE);
        }
    }

    public void regist(View view) {
        if (Build.VERSION.SDK_INT >= 16) {
            registPart.animate().withStartAction(new Runnable() {
                @Override
                public void run() {
                    registPart.setVisibility(View.VISIBLE);
                }
            }).alpha(1).translationY(0).setDuration(800).start();
        } else {
            registPart.setVisibility(View.VISIBLE);
        }
    }

}
