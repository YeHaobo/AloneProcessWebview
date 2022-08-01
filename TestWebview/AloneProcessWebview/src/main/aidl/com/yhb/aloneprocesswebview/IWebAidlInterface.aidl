// IWebAidlInterface.aidl
package com.yhb.aloneprocesswebview;

import com.yhb.aloneprocesswebview.IWebAidlCallback;

//客户端远程调用
interface IWebAidlInterface {

      //cmd：命令       params：参数         callback：回调
      void actionExec(String cmd, String params, in IWebAidlCallback callback);

}