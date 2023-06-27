package com.yuzule.recipe.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.yuzule.recipe.R;
import com.yuzule.recipe.service.RegisterService;

public class RegisterActivity extends AppCompatActivity {

    private final Handler handler = new Handler();
    MaterialButton register, login;
    EditText usernameText, passwordText, passwordConfirmText, phoneText;
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("com.yzl.broadcast.register".equals(intent.getAction())) {
                String message = intent.getStringExtra("message");
                // 从后端获取的用户电话号码为空
                switch (message) {
                    case "密码输入不一致":
                        new AlertDialog.Builder(RegisterActivity.this)
                                .setTitle("注册失败!")
                                .setMessage("密码输入不一致!")
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        passwordConfirmText.setText("");
                                    }
                                })
                                .show();
                        break;
                    case "该用户名已存在":
                        new AlertDialog.Builder(RegisterActivity.this)
                                .setTitle("注册失败!")
                                .setMessage("该用户名已存在!")
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        usernameText.setText("");
                                    }
                                })
                                .show();
                        break;
                    case "该手机号已注册":
                        new AlertDialog.Builder(RegisterActivity.this)
                                .setTitle("注册失败!")
                                .setMessage("该手机号已存在!")
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        phoneText.setText("");
                                    }
                                })
                                .show();
                        break;
                    case "注册成功":
                        new AlertDialog.Builder(RegisterActivity.this)
                                .setTitle("注册成功!")
                                .setMessage("进入登录页面!")
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent1 = new Intent(RegisterActivity.this, LoginActivity.class);
                                        startActivity(intent1);
                                    }
                                })
                                .show();
                        break;
                    default:
                        new AlertDialog.Builder(RegisterActivity.this)
                                .setTitle("注册失败!")
                                .setMessage("出现异常!")
                                .setPositiveButton(android.R.string.ok, null)
                                .show();
                        break;
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences prefs = getSharedPreferences("Recipe", MODE_PRIVATE);
        int theme = prefs.getInt("theme", 0); // 0为亮色主题，1为暗色主题
        if (theme == 0) {
            setTheme(R.style.Theme_Recipe);
        } else {
            setTheme(R.style.Theme_Recipe_Dark);
        }
        setContentView(R.layout.activity_register);
        register = findViewById(R.id.register);
        login = findViewById(R.id.login);
        usernameText = findViewById(R.id.usernameText);
        passwordText = findViewById(R.id.passwordText);
        passwordConfirmText = findViewById(R.id.password_confirmText);
        phoneText = findViewById(R.id.phoneText);

        phoneText.setImeOptions(EditorInfo.IME_ACTION_DONE);

        // 注册广播接收器
        IntentFilter filter = new IntentFilter("com.yzl.broadcast.register");
        registerReceiver(receiver, filter);


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, RegisterService.class);
                intent.putExtra("user_name", usernameText.getText().toString());
                intent.putExtra("password", passwordText.getText().toString());
                intent.putExtra("passwordConfirm", passwordConfirmText.getText().toString());
                intent.putExtra("phone", phoneText.getText().toString());
                startService(intent);

            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 注销广播接收器
        unregisterReceiver(receiver);
    }

}