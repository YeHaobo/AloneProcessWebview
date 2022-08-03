# AloneProcessWebview

Android独立进程Webview解决方案。  
适用于Android多模块化开发、web混合开发、多人协作等。  
独立进程Webview大幅减少OOM和进程崩溃的安全性问题。  
内部核心使用的是AIDL接口分发，可并发的进行通讯和数据传输。   

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
    implementation 'com.android.support:appcompat-v7:28.0.0'//v7 AndroidX项目不用添加
    implementation 'com.google.code.gson:gson:2.8.5'//GSON
    implementation 'com.github.YeHaobo:AloneProcessWebview:2.3'
  }
```

## 基本使用

### 创建承载Webview的Fragment
（1）WebviewFragment.java
```java
public class WebviewFragment extends BaseWebviewFragment {

    @Override
    public String getStartUrl() {
        return "file:///android_asset/aidl.html";
    }

    @Override
    public boolean initWebview(ProWebview webview) {
//        webview.getSettings().setJavaScriptEnabled(true);
        return false;//默认返回false，只开启JS支持
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

### 创建展示Fragment的Activity
（1）WebviewActivity.java
```java
public class WebviewActivity extends AppCompatActivity {

    private WebviewFragment webviewFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        webviewFragment = new WebviewFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.fl, webviewFragment).commit();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //网页返回拦截虚拟返回键
        return webviewFragment.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }

    private void reload(){
        webviewFragment.getProWebview().reload();//网页刷新
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.exit(0);//记得结束进程释放内存
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
注意：这里另开了子进程和硬件加速，进程名需自定义。
```java
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
    public void exec(final Map params, final CommandResult commandResult) {
	//当前在binder线程，若更新UI需要切换线程
        Log.e("toastcommand", Thread.currentThread().getName() + " " + Thread.currentThread().getId());

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, String.valueOf(params.get("msg")),Toast.LENGTH_SHORT ).show();
                Map map = new HashMap();
                map.put("code",200);
                map.put("uuid",String.valueOf(params.get("uuid")));
                map.put("msg","操作成功");
                commandResult.onResult("callback",map);//回调：callback为前端JS中的回调方法名
            }
        });
    }

}
```

### 命令管理
注意：需要在客户端的主进程中操作，比如application中，切勿在子进程（WebviewActivity.class）中管理命令。
```java   
        ToastCommand toastCommand = new ToastCommand(this);
        CommandManager.getInstance().registerCommand(toastCommand);//注册命令
        CommandManager.getInstance().unregisterCommand(toastCommand);//解注册命令
        CommandManager.getInstance().findCommand("toast");//查找命令
        CommandManager.getInstance().clearCommand();//清除所有命令
        ConcurrentHashMap<String, Command> map = CommandManager.getInstance().allCommand();//获取所有命令
```

### 前端使用
（1）调用命令执行
```java  
        var params = {
            "uuid":(new Date().getTime()).toString(),//唯一键
            "msg":"执行吐司",//消息
        };
	window.webview.post('toast',JSON.stringify(params));//参数一：自定义命令的name, 参数二：需要转换成Json字符串传输	
```
（2）命令执行回调
```java    
    function callback(data){
        var obj = JSON.parse(data);//回调的是Json字符串，需要转换成对象
	var code = obj.code;
	var msg = obj.msg;
	var uuid = obj.uuid;
	...
    }		
```
注意：若JS需要鉴别回调动作，实现单次调用对应单个回调，请参考assets中的aidl.html文件

### 问题及其他

（1）无法调用命令时：  
	1、请确认命令是否在主进程中注册  
	2、JS中的调用命令名称是否与Command命令的name一致  
	3、JS传入的是否是json字符串  

（2）JS无法收到回调时：  
	1、Command命令实现中的回调是否调用  
	2、Command命令实现中回调的JS方法名是否正确  

（3）网页加载失败时：  
	1、请检查WebviewFragment中的initWebview()方法，分析初始化是否支持该网页配置/动作。  

（4）依赖的项目经过编译丢失注释，查看详细注释请下载demo查阅


