package com.yhb.aloneprocesswebview.client.view;

/**JS调用命令接口*/
public interface JavascriptCommand {
    /**
     * 调用命令
     * @param cmd       命令名称
     * @param params        执行参数
     */
    void exec(String cmd, String params);
}