// IWebAidlInterface.aidl
package com.yhb.aloneprocesswebview;

import com.yhb.aloneprocesswebview.IWebAidlCallback;

interface IWebAidlInterface {

      void handleWebAction(String cmd, String params, in IWebAidlCallback iWebAidlCallback);

}