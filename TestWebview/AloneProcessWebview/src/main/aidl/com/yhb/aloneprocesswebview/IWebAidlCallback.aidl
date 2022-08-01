// IWebAidlCallback.aidl
package com.yhb.aloneprocesswebview;

//服务端远程回调
interface IWebAidlCallback {

    //functionName：回调js的方法名     data：回调数据
    void actionCallback(String functionName, String data);

}