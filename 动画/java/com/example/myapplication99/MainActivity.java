package com.example.myapplication99;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageView iv_walk;
    private Button btn_start;
    private AnimationDrawable animation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv_walk = (ImageView) findViewById(R.id.iv_walk);
        btn_start = (Button) findViewById(R.id.btn_play);
        btn_start.setOnClickListener(this);
        //获取AnimationDrawable对象
        animation = (AnimationDrawable) iv_walk.getBackground();
    }
    @Override
    public void onClick(View v){
        //当前动画无播放
        if (!animation.isRunning()){
            animation.start();  //播放动画
            btn_start.setBackgroundResource(android.R.drawable.ic_media_pause);
        } else  {
            animation.stop();   //停止动画
            btn_start.setBackgroundResource(android.R.drawable.ic_media_play);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (animation.isRunning()) {
            animation.stop();
        }
        iv_walk.clearAnimation();
    }
}