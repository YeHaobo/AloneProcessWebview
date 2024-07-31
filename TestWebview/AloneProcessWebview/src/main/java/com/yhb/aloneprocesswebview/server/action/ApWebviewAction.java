package com.yhb.aloneprocesswebview.server.action;

/**动作*/
public interface ApWebviewAction {

    /**名称*/
    String name();

    /**执行*/
    void execute(String params, ApWebviewActionResult result);

}