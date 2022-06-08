package com.yhb.aloneprocesswebview;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import com.google.gson.Gson;
import com.yhb.aloneprocesswebview.view.ProWebview;
import java.util.Map;

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

    /**Aidl 接口*/
    private IWebAidlInterface iWebAidlInterface;

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
                iWebAidlInterface.handleWebAction(cmd, params, new IWebAidlCallback.Stub() {//回调也需要使用Aidl接口
                    @Override
                    public void onResult(int code, String action, String params) throws RemoteException {
                        handleCallback(code, action, params, proWebview);//回调
                    }
                });
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**回调处理*/
    private void handleCallback(final int code, final String action, final String params, final ProWebview proWebview){
        //webview的使用必须切回主线程执行
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Map map = new Gson().fromJson(params,Map.class);
                map.put("code",code);
                proWebview.handleCallback(action,new Gson().toJson(map));
            }
        });
    }

}