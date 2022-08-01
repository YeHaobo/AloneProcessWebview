package com.yhb.testwebview;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;
import com.yhb.aloneprocesswebview.server.command.Command;
import com.yhb.aloneprocesswebview.server.command.CommandResult;
import java.util.HashMap;
import java.util.Map;


public class ToastCommand implements Command {

    private Context context;

    public ToastCommand(Context context) {
        this.context = context;
    }

    @Override
    public String name() {
        return "toast";     //JS调用：window.webview.post('toast',JSON.stringify(params));
    }

    @Override
    public void exec(final Map params, final CommandResult commandResult) {

        Log.e("toastcommand", Thread.currentThread().getName() + " " + Thread.currentThread().getId());

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, String.valueOf(params.get("msg")),Toast.LENGTH_SHORT ).show();
                Map map = new HashMap();
                map.put("code",200);
                map.put("uuid",String.valueOf(params.get("uuid")));//需要与前端统一
                map.put("msg","操作成功");
                commandResult.onResult("callback",map);
            }
        });
    }

}