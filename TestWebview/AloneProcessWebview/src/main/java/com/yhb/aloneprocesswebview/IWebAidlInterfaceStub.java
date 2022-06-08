package com.yhb.aloneprocesswebview;

import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import com.google.gson.Gson;
import com.yhb.aloneprocesswebview.command.CommandManager;
import com.yhb.aloneprocesswebview.command.CommandResultBack;
import java.util.Map;

/**Aidl接口实现类，运行在主进程*/
public class IWebAidlInterfaceStub extends IWebAidlInterface.Stub {

    /**
     * |--------------------------------------|            |--------------------------------------------|
     * |         子进程调用Aidl接口方法        |    ——>     |            主进程中执行命令                 |
     * |IWebAidlInterfaceStub.handleWebAction |            | CommandManager.getInstance().execCommand   |
     * |--------------------------------------|            |--------------------------------------------|
     *                                                                          ↓
     *  |--------------------------------------|            | ------------------------------------------|
     *  |       调用Aidl接口回调子进程          |   <——      |            命令执行完成后回调              |
     *  |       iWebAidlCallback.onResult      |            |               CommandResultBack           |
     *  |--------------------------------------|            |-------------------------------------------|
     *
     */
    @Override
    public void handleWebAction(final String cmd, final String params, final IWebAidlCallback iWebAidlCallback) throws RemoteException {
        //根据需求切换线程，这里直接切换回主线程执行
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                CommandManager.getInstance().execCommand(cmd, new Gson().fromJson(params, Map.class), new CommandResultBack() {
                    @Override
                    public void onResult(int code, String action, Map params) {
                        try {
                            iWebAidlCallback.onResult(code,action, new Gson().toJson(params));
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }


}