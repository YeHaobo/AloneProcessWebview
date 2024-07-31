# AloneProcessWebview

Android独立进程Webview解决方案  
基于com.github.YeHaobo:AidlHandler（进程通讯）项目构建  
支持独立进程webveiw、以及多进程aidl通讯，适用于Android多模块化开发、web混合开发、多人协作...   
***

### 依赖
（1）在Project的build.gradle文件中添加
```java
  allprojects {
    repositories {
      maven { url "https://jitpack.io" }
    }
  }
```
（2）在app的build.gradle文件中添加
```java
  dependencies {
	implementation 'com.github.YeHaobo:AidlHandler:1.2'
      	implementation 'com.github.YeHaobo:AloneProcessWebview:2.5'
}
```

## 基本使用

### 创建webviewFragment，继承并实现BaseApWebviewFragment
（1）ApWebviewFragment.java
```java
public class ApWebviewFragment extends BaseApWebviewFragment {
    @Override
    public String getStartUrl() {
        return "file:///android_asset/aidl.html";
    }
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void initWebview(ApWebview webview) {
        webview.getSettings().setJavaScriptEnabled(true);
    }
    @Override
    public String getOnResumeFunctionName() {
        return "onWebResume";//当fragment进入OnResume时会调用JS中onWebResume方法
    }
    @Override
    public String getOnPauseFunctionName() {
        return "onWebPause";//当fragment进入OnPause时会调用JS中onWebPause方法
    }
    @Override
    public String getOnStopFunctionName() {
        return "onWebStop";//当fragment进入OnStop时会调用JS中onWebStop方法
    }
    @Override
    public String getOnDestroyFunctionName() {
        return "onWebDestroy";//当fragment进入OnDestroy时会调用JS中onWebDestroy方法
    }
}
```

### 创建WebviewActivity展示webview
（1）WebviewActivity.java
```java
public class WebviewActivity extends AppCompatActivity {
    /**webFragment*/
    private ApWebviewFragment webviewFragment;
    /**创建*/
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        webviewFragment = new ApWebviewFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.fl, webviewFragment).commit();
    }
    /**网页返回拦截虚拟返回键*/
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return webviewFragment.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }
    /**网页刷新*/
    private void reload(){
        webviewFragment.apWebview().reload();
    }
    /**释放*/
    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.exit(0);
    }
}
```
（2）activity_webview.xml
```java
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical">
    <FrameLayout
        android:id="@+id/fl"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>
</LinearLayout>
```

### 注册WebviewActivity
_注意：这里另开了子进程和硬件加速_
```java
        <activity android:name=".WebviewActivity" android:hardwareAccelerated="true" android:process=":remoteweb"/>
```

### 自定义动作
```java
/**app信息获取*/
public class AppInfoAction implements ApWebviewAction {
    @Override
    public String name() {
        return "appInfo";//JS调用示例：window.ApWebview.asyncPost(uuid, 'appInfo', 'json params', callbackFunctionName);
    }
    @Override
    public void execute(String params, ApWebviewActionResult result) {
        Log.e("AppInfoAction", params);
        Log.e("AppInfoAction", Thread.currentThread().getName() + " " + Thread.currentThread().getId());
        Map<String, Object> infoMap = new HashMap<>();
        infoMap.put("version_name", BuildConfig.VERSION_NAME);
        infoMap.put("version_code", BuildConfig.VERSION_CODE);
        infoMap.put("package", BuildConfig.APPLICATION_ID);
        result.onActionResult(200, new Gson().toJson(infoMap));
    }
}
```

### 动作管理
_注意：需要在主进程中操作，切勿在子进程中管理命令。_
```java   
        AppInfoAction action = new AppInfoAction();
        ApWebviewActionManager.getInstance().register(action);//注册命令
        ApWebviewActionManager.getInstance().unregister(action.name());//解注册命令
        ApWebviewActionManager.getInstance().find("toast");//查找命令
        ApWebviewActionManager.getInstance().clear();//清除所有命令
        ConcurrentHashMap<String, ApWebviewAction> map = ApWebviewActionManager.getInstance().all();//获取所有命令
```

### 前端使用
（1）syncPost同步调用
```java
    function syncPost(){
        var uuid = (new Date().getTime()).toString();//测试时直接使用时间戳，生产时应使用UUID防止重复id
        var action = "appInfo";
        var params = {
            type: "syncPost",
            msg:"web send test msg",
        }
        var paramsJson = JSON.stringify(params);
        var callbackFunction = "callback";//回调方法名
        window.ApWebview.syncPost(uuid, action, paramsJson, callbackFunction);
    }
```
（2）syncPost异步调用
```java
    function asyncPost(){
        var uuid = (new Date().getTime()).toString();
        var action = "appInfo";
        var params = {
            type: "asyncPost",
            msg:"web send test msg",
        }
        var paramsJson = JSON.stringify(params);
        var callbackFunction = "callback";
        window.ApWebview.asyncPost(uuid, action, paramsJson, callbackFunction);
    }
```
（3）回调
```java    
    function callback(uuid, code, params){
        var txt = document.getElementById('callbackMsg');
        txt.innerHTML = txt.innerHTML + "<br/>uuid:" + uuid + "<br/>code:" + code + "<br/>params:" + params + "<br/>";
    }	
```
_注意：调用与回调的uuid匹配；调用时的callbackFunction为回调方法名，回调方法名可改但参数不可变更。_

### 问题及其他

**（1）无法调用时：**  
	<1>请确认动作是否在主进程中注册  
	<2>JS中调用的动作名称是否与ApWebviewAction的name一致   

**（2）无法收到回调时：**  
	<1>动作实现的execute方法内是否使用ApWebviewActionResult.onActionResult()进行回调  
	<2>JS调用syncPost/asyncPost时传入的回调方法名是否正确  
 	<3>回调的params参数是否为字符串类型
  	<4>若iframe内无法收到回调，调用时的方法名需要包含子页面DOM路径（例： $("iframe")[0].contentWindow.callback）

**（3）网页加载失败时：**  
	<1>请检查AndroidManifest.xml文件中是否包含所需权限。  
 	<2>请检查WebviewFragment中的initWebview()方法，分析初始化正确。  

