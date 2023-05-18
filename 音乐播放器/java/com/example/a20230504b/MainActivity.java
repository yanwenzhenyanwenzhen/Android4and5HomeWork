package com.example.a20230504b;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static SeekBar seekBar;
    private static TextView tv_progress, tv_total;
    private MusicService.MusicControl musicControl;
    private MyServiceConn myConn;
    private Intent intent;
    private ObjectAnimator animator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (myConn != null) {
            unbindService(myConn);
            myConn = null;
        }

    }


    private void init() {
        tv_progress = (TextView) findViewById(R.id.tv_time);
        tv_total = (TextView) findViewById(R.id.tv_totaltime);
        seekBar = (SeekBar) findViewById(R.id.seekbar);
        findViewById(R.id.btn_play).setOnClickListener(this);
        findViewById(R.id.btn_continue).setOnClickListener(this);
        findViewById(R.id.btn_pause).setOnClickListener(this);
        findViewById(R.id.btn_exit).setOnClickListener(this);
        intent = new Intent(this, MusicService.class);
        myConn = new MyServiceConn();
        bindService(intent, myConn, BIND_AUTO_CREATE);
        ImageView iv_music = (ImageView) findViewById(R.id.iv_record);
        animator = ObjectAnimator.ofFloat(iv_music, "rotation", 0f, 360.0f);
        animator.setDuration(10000);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(-1);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_play) {
            //播放音乐
            musicControl.play();
            animator.start();
        }
        else if (view.getId() == R.id.btn_pause) {
            //暂停音乐
            musicControl.pause();
            animator.pause();
        }
        else if (view.getId() == R.id.btn_continue) {
            //继续播放音乐
            musicControl.continuePlayer();
            animator.start();
        }
        else if (view.getId() == R.id.btn_exit) {
            //退出
            if (myConn != null) {
                unbindService(myConn);
                myConn = null;
            }
            finish();
        }
    }

    public static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //接受消息，设置到进度条的对应位置
            Bundle bundle = msg.getData();
            int duration = bundle.getInt("duration");
            int currentPosition = bundle.getInt("currentPosition");
            seekBar.setMax(duration);
            seekBar.setProgress(currentPosition);
            int minute = duration / 1000 / 60;
            int second = duration / 1000 % 60;
            String strMin = minute < 10 ? "0" + minute : minute + "";
            String strSec = second < 10 ? "0" + second : second + "";
            tv_total.setText(strMin + ":" + strSec);

            minute = currentPosition / 1000 / 60;
            second = currentPosition / 1000 % 60;
            strMin = minute < 10 ? "0" + minute : minute + "";
            strSec = second < 10 ? "0" + second : second + "";
            tv_progress.setText(strMin + ":" + strSec);
        }
    };


    class MyServiceConn implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            musicControl = (MusicService.MusicControl) service;
        }
        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    }

}