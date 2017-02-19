package com.gyh.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gyh.login.db.User;
import com.gyh.login.util.MD5Util;

import org.litepal.crud.DataSupport;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private ImageView bgPicImg;
    private LinearLayout registPart;

    private String username;
    private String password;

    private List<User> users = DataSupport.findAll(User.class);

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

        // 已登录，自动登陆逻辑
        SharedPreferences sharedPreferences = getSharedPreferences("config", 0);
        String name = sharedPreferences.getString("username", "");
        String pass = sharedPreferences.getString("password", "");
        Log.d("Test", "onCreate: +" + name + " + " + pass);
        if (!name.equals("") && !pass.equals("")) {
            Log.d("Test", "onCreate: !");
            User user = findUser(name, pass);
            if (user != null) {
                logIn(user);
            }
        }

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        bgPicImg = (ImageView) findViewById(R.id.bg_img);
        String bgPic = prefs.getString("bg_pic", null);
        if (bgPic != null) {
            Glide.with(this).load(bgPic).into(bgPicImg);
        } else {
            loadBgPic();
        }

        registPart = (LinearLayout) findViewById(R.id.regist_part);

        final Button login = (Button) findViewById(R.id.login);
        Button regist = (Button) findViewById(R.id.regist);
        final EditText usernameRInput = (EditText) findViewById(R.id.username_r_input);
        final EditText passwordRInput = (EditText) findViewById(R.id.password_r_input);
        final EditText usernameInput = (EditText) findViewById(R.id.username_input);
        final EditText passwordInput = (EditText) findViewById(R.id.password_input);
        // 登录检查逻辑
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = usernameInput.getText().toString();
                password = passwordInput.getText().toString();
                if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
                    User user = findUser(username, MD5Util.getMD5(password));
                    if (user != null) {
                        Toast.makeText(MainActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                        saveInfo(user);
                        logIn(user);
                    } else {
                        Toast.makeText(MainActivity.this, "Username and Password may be wrong!", Toast.LENGTH_SHORT).show();
                        usernameInput.setText("");
                        passwordInput.setText("");
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Username and Password can't be empty!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // 注册逻辑
        regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User();
                username = usernameRInput.getText().toString();
                password = passwordRInput.getText().toString();
                if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
                    String regEx="[`~!@#$%^&*()+=|{}':;,\\[\\].<>/?！￥…（）—【】‘；：”“’。，、？]";
                    Pattern p = Pattern.compile(regEx);
                    Matcher m = p.matcher(username);
                    Matcher n = p.matcher(password);
                    if( m.find() || n.find()){
                        Toast.makeText(MainActivity.this, "Username and Password can't have special characters!", Toast.LENGTH_LONG).show();
                        return;
                    }
                    Log.d("Test", "Regist: +" + username + " + " + MD5Util.getMD5(password));
                    user.setPassword(MD5Util.getMD5(password));
                    user.setUsername(username);
                    user.setName(username);
                    user.setIntro("一句话介绍一下你自己");
                    user.save();
                    saveInfo(user);
                    Toast.makeText(MainActivity.this, "Register Successfully", Toast.LENGTH_SHORT).show();
                    logIn(user);
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

    private void saveInfo(User user) {
        SharedPreferences sharedPreferences = getSharedPreferences("config", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", user.getUsername());
        editor.putString("password", user.getPassword());
        editor.apply();
    }

    private User findUser(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                if (user.getPassword().equals(password)) {
                    return user;
                }
            }
        }
        return null;
    }

    private void logIn(User user) {
        Intent intent = new Intent(MainActivity.this, Index.class);
        intent.putExtra("name", user.getName());
        intent.putExtra("intro", user.getIntro());
        startActivity(intent);
        finish();
    }
}
