# AloneProcessWebview

Android独立进程的Webview解决方案。使用JS传入命令以及参数后即可执行java层代码，执行完毕后会再次回调至JS中。适用于App的多种模块化开发、web混合开发、多人协作等。Webview的独立进程大幅减少了OOM和进程崩溃的安全性问题。内部核心使用的是AIDL接口分发，可并发的进行通讯和传输。	   

|方案流程图|
|:----|
|![](/IMG.png "方案流程图")|

***

### 依赖
（1）在Project的build.gradle文件中添加
```java
  allprojects {
    repositories {
      ... ...
      maven { url "https://jitpack.io" }
      ... ...
    }
  }
```
（2）在app的build.gradle文件中添加
```java
  dependencies {
    ... ...
    implementation 'com.android.support:appcompat-v7:28.0.0'//support-v7
    implementation 'com.google.code.gson:gson:2.8.5'//GSON
    implementation 'com.github.YeHaobo:AloneProcessWebview:1.0'
    ... ...
  }
```

### 创建承载Webview的WebviewFragment
（1）WebviewFragment.java
```java
public class WebviewFragment extends BaseWebviewFragment {
    @Override
    public int getLayoutRes() {
        return R.layout.fragment_webview;//传入布局
    }

    @Override
    public int getWebviewId() {
        return R.id.webview;//传入布局中webview的Id
    }

    @Override
    public String getStartUrl() {
        return "file:///android_asset/aidl.html";//打开起始网页
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public boolean initWebview(final ProWebview webview) {
        /**其他初始化操作都在此做，这里只设置了开启的JS**/
        webview.setWebViewClient(new WebViewClient());
        webview.setWebChromeClient(new WebChromeClient());
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        ... ...
        return true;//初始化完成后返回ture (默认返回false,只启用JavaScript)
    }

/***************************以下生命周期回调的JS方法名需要与前端确认统一****************************************/
    @Override
    public String getOnResumeActionName() {
        return "onWebResume";//fragment onResume时回调JS的function方法名
    }

    @Override
    public String getOnPauseActionName() {
        return "onWebPause";//fragment onPause时回调JS的function方法名
    }

    @Override
    public String getOnStopActionName() {
        return "onWebStop";//fragment onStop时回调JS的function方法名
    }

    @Override
    public String getOnDestroyActionName() {
        return "onWebDestroy";//fragment onDestroy时回调JS的function方法名
    }

}
```
（2）fragment_webview.xml
```java
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:orientation="vertical" android:layout_width="match_parent"
    android:layout_height="match_parent">
    <com.yhb.aloneprocesswebview.view.ProWebview
        android:id="@+id/webview"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>
</LinearLayout>
```

### 创建展示WebviewFragment的WebviewActivity
（1）WebviewActivity.java
```java
public class WebviewActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.fl, new WebviewFragment()).commit();
    }
}
```
（2）activity_webview.xml
```java
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:orientation="vertical" android:layout_width="match_parent"
    android:layout_height="match_parent">
    <FrameLayout
        android:id="@+id/fl"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>
</LinearLayout>
```

### 注册Activity
```java
<!--android:hardwareAccelerated="true"开启硬件加速-->
<!--android:process=":remoteweb"需要在子进程，子线程名字自定义-->
<activity android:name=".WebviewActivity" android:hardwareAccelerated="true" android:process=":remoteweb"/>  
```

### 自定义命令
```java
public class ToastCommand implements Command {

    private Context context;

    public ToastCommand(Context context) {
        this.context = context;
    }

    @Override
    public String name() {
        return "toast";     //JS调用：window.webview.post('toast',JSON.stringify(params));
    }

    @Override
    public void exec(Map params, CommandResultBack commandResultBack) {
    	//执行命令
        Toast.makeText(context, String.valueOf(params.get("msg")),Toast.LENGTH_SHORT ).show();

        Map map = new HashMap();
        map.put("msg","吐司成功！");

        commandResultBack.onResult(200,"callback",map);//回调
    }

}
```

### 注册命令
注意：需要在主进程（命令执行进程）注册，且要在webview使用以前注册。
```java   
        CommandManager.getInstance().registerCommand(new ToastCommand(this));	
```

### 前端使用
（1）调用命令执行
```java  
        window.webview.post('toast',JSON.stringify(params));//需要转换成字符串传输	
```
（2）命令执行回调
```java    
    function callback(data){
        var obj = JSON.parse(data);//回调传入的是Json需要转换成对象
		var code = obj.code;//返回码
		var msg = obj.msg;
		... ...
    }		
```
若需要指定JS中的Callback回调接口，请参考assets中的aidl.html文件

### 问题及其他

（1）依赖过程中若编译不通过请重新Rebuild一下。

（2）网页加载失败时请检查WebviewFragment中的initWebview()方法，分析初始化是否支持该网页配置/动作。	

