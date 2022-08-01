package com.yhb.aloneprocesswebview.server.command;

import java.util.Map;

/**命令执行后的回调接口*/
public interface CommandResult {

    /**
     * 回调
     * @param functionName  调用js的方法名
     * @param data    返回数据
     */
    void onResult(String functionName, Map data);

}