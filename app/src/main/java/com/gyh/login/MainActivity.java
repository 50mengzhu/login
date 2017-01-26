package com.gyh.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gyh.login.db.User;

import org.litepal.crud.DataSupport;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private ImageView bgPicImg;
    private LinearLayout registPart;

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

        Button login = (Button) findViewById(R.id.login);
        Button regist = (Button) findViewById(R.id.regist);
        final EditText usernameRInput = (EditText) findViewById(R.id.username_r_input);
        final EditText passwordRInput = (EditText) findViewById(R.id.password_r_input);
        final EditText usernameInput = (EditText) findViewById(R.id.username_input);
        final EditText passwordInput = (EditText) findViewById(R.id.password_input);
        // 登录检查逻辑
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameInput.getText().toString();
                String password = passwordInput.getText().toString();
                if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
                    List<User> users = DataSupport.findAll(User.class);
                    for (User user : users) {
                        if (user.getUsername().equals(username)) {
                            if (user.getPassword().equals(password)) {
                                Toast.makeText(MainActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MainActivity.this, Index.class);
                                intent.putExtra("username", username);
                                intent.putExtra("password", password);
                                startActivity(intent);
                                finish();
                            }
                            return;
                        }
                    }
                    Toast.makeText(MainActivity.this, "Username and Password may be wrong!", Toast.LENGTH_SHORT).show();
                    usernameInput.setText("");
                } else {
                    Toast.makeText(MainActivity.this, "Username and Password can be empty!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // 注册逻辑
        regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User();
                String username = usernameRInput.getText().toString();
                String password = passwordRInput.getText().toString();
                if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
                    String regEx="[`~!@#$%^&*()+=|{}':;,\\[\\].<>/?！￥…（）—【】‘；：”“’。，、？]";
                    Pattern p = Pattern.compile(regEx);
                    Matcher m = p.matcher(username);
                    Matcher n = p.matcher(password);
                    if( m.find() || n.find()){
                        Toast.makeText(MainActivity.this, "Username and Password can't have special characters!", Toast.LENGTH_LONG).show();
                        return;
                    }
                    user.setPassword(password);
                    user.setUsername(username);
                    user.save();
                    Toast.makeText(MainActivity.this, "Register Successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Username and Password can be empty!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 为以后网络接收图片做接口
     */
    private void loadBgPic() {
        Glide.with(this).load(R.drawable.bg).into(bgPicImg);
    }

    /**
     * 切换成登陆界面
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

    /**
     * 切换成注册界面
     */
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
