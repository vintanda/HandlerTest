package com.example.lzd.handlertest;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created By LZD On 2016/11/14
 */

public class MainActivity extends AppCompatActivity {

    private boolean isCount = false;
    private static int timeCount = 0;
    private CountThread cThread = null;

    //控件声明
    private Button StartStop;
    private Button ToBeZero;
    private TextView Count;
    private TextView Time;
    private TextView Seconds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        StartStop.setOnClickListener(new MyOnClickListener());
        ToBeZero.setOnClickListener(new MyOnClickListener());

    }

    //监听事件
    class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.StartStop:
                    if(isCount == false) {
                        isCount = true;
                        start();
                    }else {
                        isCount = false;
                        stop();
                    }
                    break;
                case R.id.be_zero:
                    isCount = false;
                    over();
                    break;
            }
        }
    }

    //子线程更新界面
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Log.d("Handler","update");
                    upDate();
                    Log.d("timecount",timeCount+"");
                    break;
            }
        }
    };

    //更新
    private void upDate() {
        Log.d("upDate","setText");
        Time.setText(timeCount + "");
    }

    //开始计时并启动线程
    private void start() {
        Log.d("Thread","START");
        cThread = new CountThread();
        cThread.start();
    }

    //计时结束终止线程
    private void stop() {
        if(cThread != null) {
            Log.d("Thread","STOP");
//            Message messageStop = new Message();
//            messageStop.what = -5;
//            handler.sendMessage(messageStop);
            cThread.interrupt();
            cThread = null;
        }
    }

    //清零
    private void over() {
        Log.d("Thread","toBeZero");
        timeCount = 0;
        upDate();
    }

    //初始化控件
    void init() {
        StartStop = (Button) findViewById(R.id.StartStop);
        ToBeZero = (Button) findViewById(R.id.be_zero);
        Count = (TextView) findViewById(R.id.count);
        Time = (TextView) findViewById(R.id.time);
        Seconds = (TextView) findViewById(R.id.seconds);
    }

    //Thread sleep 实现一秒一次计时并发送信息到Handler
    class CountThread extends Thread {

        @Override
        public  void run() {

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            while(isCount) {

                synchronized(Object.class)
                { timeCount++;
                    Log.d("timecount2",timeCount+"");
                    Message message = new Message();
                    message.what = 1;
                    try {

                        handler.sendMessage(message);
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }

//            handler.sendEmptyMessageAtTime(1,1000);
            }
        }
    }

}
