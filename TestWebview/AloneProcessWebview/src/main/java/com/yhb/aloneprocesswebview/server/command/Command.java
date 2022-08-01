package com.yhb.aloneprocesswebview.server.command;

import java.util.Map;

/**所有命令必须实现该接口*/
public interface Command {

    /**命令名称*/
    String name();

    /**执行*/
    void exec(Map params, CommandResult result);

}