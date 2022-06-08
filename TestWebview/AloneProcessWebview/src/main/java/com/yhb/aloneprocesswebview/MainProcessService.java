package com.yhb.aloneprocesswebview;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**主进程中的web服务*/
public class MainProcessService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new IWebAidlInterfaceStub();
    }

}