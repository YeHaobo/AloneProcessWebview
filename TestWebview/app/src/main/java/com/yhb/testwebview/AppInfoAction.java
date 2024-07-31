package com.yhb.testwebview;

import android.util.Log;
import com.google.gson.Gson;
import com.yhb.aloneprocesswebview.server.action.ApWebviewAction;
import com.yhb.aloneprocesswebview.server.action.ApWebviewActionResult;
import java.util.HashMap;
import java.util.Map;

/**app信息获取*/
public class AppInfoAction implements ApWebviewAction {

    @Override
    public String name() {
        return "appInfo";//JS调用示例：window.ApWebview.asyncPost(uuid, 'appInfo', 'json params', callbackFunctionName);
    }

    @Override
    public void execute(String params, ApWebviewActionResult result) {
        Log.e("AppInfoAction", params);
        Log.e("AppInfoAction", Thread.currentThread().getName() + " " + Thread.currentThread().getId());
        Map<String, Object> infoMap = new HashMap<>();
        infoMap.put("version_name", BuildConfig.VERSION_NAME);
        infoMap.put("version_code", BuildConfig.VERSION_CODE);
        infoMap.put("package", BuildConfig.APPLICATION_ID);
        result.onActionResult(200, new Gson().toJson(infoMap));
    }

}