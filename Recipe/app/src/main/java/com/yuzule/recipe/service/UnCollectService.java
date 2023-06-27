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

public class UnCollectService extends Service {


    private static final String TAG = "UnCollectService";
    Api api;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int user_id = intent.getIntExtra("user_id", 0);
        int content_id = intent.getIntExtra("content_id", 0);
        // 访问接口并获取数据
        uncollect(user_id, content_id);
        return super.onStartCommand(intent, flags, startId);
    }

    private void uncollect(int user_id, int content_id) {
        api = ApiClient.getRetrofit().create(Api.class);
        api.uncollect(user_id, content_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Information>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        // 这里可以添加请求开始时的一些操作，比如显示进度条等
                    }

                    @Override
                    public void onNext(Information information) {
                        // 创建Intent对象
                        Intent broadcastIntent = new Intent();
                        broadcastIntent.setAction("com.yzl.broadcast.uncollect");
                        broadcastIntent.putExtra("message", information.getMessage());
                        // 发送Broadcast
                        sendBroadcast(broadcastIntent);
                    }

                    @Override
                    public void onError(Throwable e) {
                        // 请求失败时的操作
                        Log.e(TAG, "访问取消收藏接口时出现错误:" + e);
                    }

                    @Override
                    public void onComplete() {
                        // 请求完成时的操作
                    }
                });
    }

}
