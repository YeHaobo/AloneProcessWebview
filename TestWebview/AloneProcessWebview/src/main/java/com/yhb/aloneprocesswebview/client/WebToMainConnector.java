package com.yhb.aloneprocesswebview.client;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import com.yhb.aloneprocesswebview.IWebAidlInterface;
import com.yhb.aloneprocesswebview.server.service.MainProcessWebService;
import java.util.concurrent.CountDownLatch;

/**远程链接*/
public class WebToMainConnector {

    /**单例*/
    private static volatile WebToMainConnector sInstance;
    public static WebToMainConnector getInstance(Context context){
        if(sInstance == null){
            synchronized (WebToMainConnector.class){
                if(sInstance == null){
                    sInstance = new WebToMainConnector(context);
                }
            }
        }
        return sInstance;
    }

    /**上下文*/
    private Context mContext;
    /**Aidl接口*/
    private IWebAidlInterface iWebAidlInterface;
    /**锁*/
    private CountDownLatch mCountDownLatch;

    /**构造*/
    private WebToMainConnector(Context context){
        mContext = context.getApplicationContext();//使用主进程上下文，防止服务断连
        connectToMainProcessService();//开始远程连接
    }

    /**远程链接主进程服务*/
    private synchronized void connectToMainProcessService(){
        mCountDownLatch = new CountDownLatch(1);
        Intent intent = new Intent(mContext, MainProcessWebService.class);
        mContext.bindService(intent,connection,Context.BIND_AUTO_CREATE);//绑定服务
        try {
            mCountDownLatch.await();//执行连接，加锁
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**获取ADIL接口的binder对象*/
    public IBinder getIWebAidlInterface(){
        return iWebAidlInterface.asBinder();
    }

    /**链接回调*/
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iWebAidlInterface = IWebAidlInterface.Stub.asInterface(service);//获取Aidl接口
            try {
                iWebAidlInterface.asBinder().linkToDeath(deathRecipient,0);//设置连接监测
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            mCountDownLatch.countDown();//链接完成，开锁
        }
    };

    /**断开连接回调*/
    private IBinder.DeathRecipient deathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            iWebAidlInterface.asBinder().unlinkToDeath(deathRecipient,0);//解除连接监测
            iWebAidlInterface = null;
            connectToMainProcessService();//重新连接
        }
    };

}