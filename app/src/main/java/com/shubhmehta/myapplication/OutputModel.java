package com.shubhmehta.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.VideoView;

public class OutputModel extends AppCompatActivity {

    VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_output_model);
        videoView = (VideoView) findViewById(R.id.videoView2);  //casting to VideoView is not Strictly required above API level 26
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                videoView.start(); //need to make transition seamless.
            }
        });
        videoView.setVideoPath("android.resource://" + getPackageName() + "/" + R.raw.logo1); //set the path of the video that we need to use in our VideoView
        videoView.start();
    }
}