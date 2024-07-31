package com.yhb.aloneprocesswebview.server.action;

/**动作回调*/
public interface ApWebviewActionResult {

    /**
     * 回调
     * @param code 状态码
     * @param data 数据
     */
    void onActionResult(int code, String data);

}