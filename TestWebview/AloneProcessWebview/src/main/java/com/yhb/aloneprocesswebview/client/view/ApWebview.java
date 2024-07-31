package com.yhb.aloneprocesswebview.client.view;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.webkit.WebView;
import com.yhb.aidlhandler.IServiceAidlResult;
import com.yhb.aidlhandler.client.ClientAidlConnector;
import com.yhb.aidlhandler.client.ClientAidlPoster;
import com.yhb.aidlhandler.client.ConnectResult;
import com.yhb.aloneprocesswebview.client.javascript.ApJavascriptAction;
import com.yhb.aloneprocesswebview.client.javascript.ApJavascriptInterface;
import com.yhb.aloneprocesswebview.server.service.ApWebviewService;

/**子线程webview封装*/
public class ApWebview extends WebView implements ConnectResult, ApJavascriptAction {

    /**JS挂载空间命名*/
    private static final String jsName = "ApWebview";
    /**主线程*/
    private Handler mainHandler;
    /**远程连接器*/
    private ClientAidlConnector aidlConnector;
    /**远程消息发送者*/
    private ClientAidlPoster aidlPoster;
    /**准备监听回调*/
    private ApWebviewPrepareListener prepareListener;

    /**构造*/
    public ApWebview(Context context) {
        super(context);
    }
    public ApWebview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public ApWebview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**准备（远程连接、JS注入）*/
    public void prepare(ApWebviewPrepareListener prepareListener){
        this.mainHandler = new Handler(Looper.getMainLooper());
        this.prepareListener = prepareListener;
        this.aidlConnector = new ClientAidlConnector
                .Builder()
                .context(getContext().getApplicationContext())
                .packageName(getContext().getPackageName())
                .serviceName(ApWebviewService.class.getName())
                .connectResult(this)
                .build();
        this.aidlConnector.connect();
    }

    /**已连接*/
    @Override
    public void connected(ClientAidlPoster clientAidlPoster) {
        aidlPoster = clientAidlPoster;//客户端消息发送器
        removeJavascriptInterface(jsName);//先移除JS接口
        addJavascriptInterface(new ApJavascriptInterface(ApWebview.this), jsName);//添加JS接口
        if(prepareListener != null) {
            prepareListener.onPrepared();//准备完成回调
        }
    }

    /**断开连接*/
    @Override
    public boolean disconnected() {
        return true;//true:重连 false:不重连
    }

    /**同步执行*/
    @Override
    public void syncExecute(final String uuid, String action, String params, final String callFunction) {
        if(aidlPoster == null) return;
        //JavaBridge线程
        aidlPoster.syncPost(action, params, new IServiceAidlResult.Stub() {
            @Override
            public void onResult(final int i, final String s) throws RemoteException {
                //JavaBridge线程
                mainHandler.post(new Runnable() {//需要主线程回调JS
                    @Override
                    public void run() {
                        String script = "javascript:" + callFunction + "('" + uuid + "'," + i + ",'" + s + "')";
                        evaluateJavascript(script, null);
                    }
                });
            }
        });
    }

    /**异步执行*/
    @Override
    public void asyncExecute(final String uuid, String action, String params, final String callFunction) {
        if(aidlPoster == null) return;
        //JavaBridge线程
        aidlPoster.asyncPost(action, params, new IServiceAidlResult.Stub() {
            @Override
            public void onResult(final int i, final String s) throws RemoteException {
                //Binder线程
                mainHandler.post(new Runnable() {//需要主线程回调JS
                    @Override
                    public void run() {
                        String script = "javascript:" + callFunction + "('" + uuid + "'," + i + ",'" + s + "')";
                        evaluateJavascript(script, null);
                    }
                });
            }
        });
    }

    /**销毁*/
    @Override
    public void destroy() {
        if(aidlConnector != null){
            aidlConnector.disconnect();
        }
        stopLoading();
        if(getHandler() != null){
            getHandler().removeCallbacksAndMessages(null);
        }
        removeAllViews();
        ((ViewGroup) getParent()).removeView(this);
        setWebChromeClient(null);
        setWebViewClient(null);
        setTag(null);
        clearHistory();
        super.destroy();
    }

}