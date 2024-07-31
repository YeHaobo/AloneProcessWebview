package com.yhb.aloneprocesswebview.server.service;

import android.os.RemoteException;
import android.util.Log;
import com.yhb.aidlhandler.IClientAidlReceive;
import com.yhb.aidlhandler.IServiceAidlResult;
import com.yhb.aidlhandler.service.BaseAidlService;
import com.yhb.aloneprocesswebview.server.action.ApWebviewActionManager;
import com.yhb.aloneprocesswebview.server.action.ApWebviewActionResult;

/**webview服务*/
public class ApWebviewService extends BaseAidlService {

    /**TAG*/
    private static final String TAG = "ApWebviewService";

    /**客户端同步调用*/
    @Override
    public void syncPost(String s, String s1, final IServiceAidlResult iServiceAidlResult) {
        ApWebviewActionManager.getInstance().execute(s, s1, new ApWebviewActionResult() {
            @Override
            public void onActionResult(int code, String data) {
                try {
                    iServiceAidlResult.onResult(code, data);
                } catch (RemoteException e) {
                    e.printStackTrace();
                    Log.e(TAG, e.getMessage());
                }
            }
        });
    }

    /**客户端异步调用*/
    @Override
    public void asyncPost(String s, String s1, final IServiceAidlResult iServiceAidlResult) {
        ApWebviewActionManager.getInstance().execute(s, s1, new ApWebviewActionResult() {
            @Override
            public void onActionResult(int code, String data) {
                try {
                    iServiceAidlResult.onResult(code, data);
                } catch (RemoteException e) {
                    e.printStackTrace();
                    Log.e(TAG, e.getMessage());
                }
            }
        });
    }

    /**客户端注册*/
    @Override
    public void registerReceive(IClientAidlReceive iClientAidlReceive) {
        //暂时没用到，后续更新...
    }

    /**客户端解注册*/
    @Override
    public void unregisterReceive(IClientAidlReceive iClientAidlReceive) {
        //暂时没用到，后续更新...
    }

}