package com.yhb.testwebview;

import android.content.Context;
import android.widget.Toast;
import com.yhb.aloneprocesswebview.command.Command;
import com.yhb.aloneprocesswebview.command.CommandResultBack;
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
    public void exec(Map params, CommandResultBack commandResultBack) {
        Toast.makeText(context, String.valueOf(params.get("msg")),Toast.LENGTH_SHORT ).show();

        Map map = new HashMap();
        map.put("msg","wocao niupi");
        map.put("action",String.valueOf(params.get("action")));//需要与前端统一

        commandResultBack.onResult(200,"callback",map);
    }

}