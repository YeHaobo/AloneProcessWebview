package com.yhb.testwebview;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import com.yhb.aloneprocesswebview.command.CommandManager;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /******命令的注册需要在主进程（执行进程）********/
        CommandManager.getInstance().registerCommand(new ToastCommand(this));
        /***********************************************/

        findViewById(R.id.tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,WebviewActivity.class);
                startActivity(intent);
            }
        });
    }

}