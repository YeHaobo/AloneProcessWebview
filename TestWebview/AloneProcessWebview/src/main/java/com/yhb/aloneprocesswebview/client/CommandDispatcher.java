package com.yhb.aloneprocesswebview.client;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import com.yhb.aloneprocesswebview.IWebAidlCallback;
import com.yhb.aloneprocesswebview.IWebAidlInterface;
import com.yhb.aloneprocesswebview.client.view.ProWebview;

/**命令分发器，运行在子进程*/
public class CommandDispatcher {
    /**单例*/
    private static CommandDispatcher instance;
    public static CommandDispatcher getInstance() {
        if (instance == null) {
            synchronized (CommandDispatcher.class) {
                if (instance == null) {
                    instance = new CommandDispatcher();
                }
            }
        }
        return instance;
    }

    /**主线程handler*/
    private Handler mainHandler;
    /**Aidl 接口*/
    private IWebAidlInterface iWebAidlInterface;

    /**私有构造*/
    private CommandDispatcher(){
        mainHandler = new Handler(Looper.getMainLooper());
    }

    /**初始化Aidl连接*/
    public void initAidlConnect(final Context context){
        if(iWebAidlInterface == null){
            //内部阻塞，需要开线程异步
            new Thread(new Runnable() {
                @Override
                public void run() {
                    iWebAidlInterface = IWebAidlInterface.Stub.asInterface(WebToMainConnector.getInstance(context).getIWebAidlInterface());
                }
            }).start();
        }
    }

    /**执行命令*/
    public void exec(String cmd, String params, final ProWebview proWebview){
        if(iWebAidlInterface != null){
            try {
                //调用Aidl接口方法
                iWebAidlInterface.actionExec(cmd, params, new IWebAidlCallback.Stub() {//回调也需要使用Aidl接口
                    @Override
                    public void actionCallback(String functionName, String data) throws RemoteException {
                        callback(functionName, data, proWebview);//回调
                    }
                });
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**回调处理*/
    private void callback(final String functionName, final String data, final ProWebview proWebview){
        //webview的使用必须切回主线程执行
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                proWebview.loadJsFunction(functionName,data);
            }
        });
    }


}