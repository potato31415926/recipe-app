package com.yuzule.recipe.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.yuzule.recipe.api.Api;
import com.yuzule.recipe.api.ApiClient;
import com.yuzule.recipe.model.Information;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class RegisterService extends Service {


    private static final String TAG = "RegisterService";
    Api api;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String user_name = intent.getStringExtra("user_name");
        String password = intent.getStringExtra("password");
        String passwordConfirm = intent.getStringExtra("passwordConfirm");
        String phone = intent.getStringExtra("phone");
        // 访问接口并获取数据
        register(user_name, password, passwordConfirm, phone);
        return super.onStartCommand(intent, flags, startId);
    }

    private void register(String user_name, String password, String passwordConfirm, String phone) {
        api = ApiClient.getRetrofit().create(Api.class);
        api.register(user_name, password, passwordConfirm, phone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Information>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        // 这里可以添加请求开始时的一些操作，比如显示进度条等
                    }

                    @Override
                    public void onNext(Information message) {
                        // 创建Intent对象
                        Intent broadcastIntent = new Intent();
                        broadcastIntent.setAction("com.yzl.broadcast.register");
                        broadcastIntent.putExtra("message", message.getMessage());
                        // 发送Broadcast
                        sendBroadcast(broadcastIntent);
                    }

                    @Override
                    public void onError(Throwable e) {
                        // 请求失败时的操作
                        Log.e(TAG, "访问注册接口时出现错误:" + e);
                    }

                    @Override
                    public void onComplete() {
                        // 请求完成时的操作

                    }
                });
    }

}
