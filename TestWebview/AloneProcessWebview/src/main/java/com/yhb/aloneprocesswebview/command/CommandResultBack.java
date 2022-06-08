package com.yhb.aloneprocesswebview.command;

import java.util.Map;

/**命令执行后的回调接口*/
public interface CommandResultBack {

    /***
     *
     * @param code  返回码
     * @param action    调用JS的function的方法名 （需要与前端确认统一）
     * @param params    回调参数
     */
    void onResult(int code, String action, Map params);

}