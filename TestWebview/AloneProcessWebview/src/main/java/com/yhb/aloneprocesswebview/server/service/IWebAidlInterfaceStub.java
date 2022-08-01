package com.yhb.aloneprocesswebview.server.service;

import android.os.RemoteException;
import com.google.gson.Gson;
import com.yhb.aloneprocesswebview.IWebAidlCallback;
import com.yhb.aloneprocesswebview.IWebAidlInterface;
import com.yhb.aloneprocesswebview.server.command.CommandManager;
import com.yhb.aloneprocesswebview.server.command.CommandResult;
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
    public void actionExec(String cmd, String params, final IWebAidlCallback callback) throws RemoteException {
        CommandManager.getInstance().execCommand(cmd, new Gson().fromJson(params, Map.class), new CommandResult() {
            @Override
            public void onResult(String functionName, Map data) {
                try {
                    callback.actionCallback(functionName, new Gson().toJson(data));
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }


}