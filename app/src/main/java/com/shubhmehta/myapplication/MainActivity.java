package com.shubhmehta.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;


public class MainActivity extends AppCompatActivity {


    static final int REQUEST_VIDEO_CAPTURE = 1;
    private String videoPath = "";
    public String param = "";

    VideoView result_video;
    VideoView videoView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button click = (Button)findViewById(R.id.button);
        videoView = (VideoView) findViewById(R.id.videoView);  //casting to VideoView is not Strictly required above API level 26
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                videoView.start(); //need to make transition seamless.
            }
        });
        videoView.setVideoPath("android.resource://" + getPackageName() + "/" + R.raw.logo1); //set the path of the video that we need to use in our VideoView
        videoView.start();  //start() method of the VideoView class will start the video to play

    }
    //code for opening camera
    public void capturePhoto(View view) {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
        Intent intent = new Intent(this,output.class);

    }

    public class MyClass {
        public void printvariableToFile (String fileName, String myVariable) {
            File myFile;
            myFile=new File(fileName);
            if(!myFile.exists()){
                try {
                    myFile.createNewFile();
                    FileWriter fw = new FileWriter(fileName);
                    PrintWriter pw = new PrintWriter(fw);
                    // Write variable to file
                    pw.print("Writing variable to file");
                    pw.println("The varible is below: ");
                    pw.println(myVariable);
                    // Close
                    pw.close();
                } catch (Exception ex){
                   // Logger.getLogger(NewJFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    //trying to run python file
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            Uri videoUri = data.getData();
            result_video.setVideoURI(videoUri);
            videoPath = getUrlFromUri(videoUri);
            param = getFileName(videoPath);
            String command = "python /c start python C:\\Users\\LENOVO\\AndroidStudioProjects\\MyApplication\\app\\src\\main\\assets\\SocialDistancingDetector.py";
            try {
                Process p = Runtime.getRuntime().exec("C:\\Users\\LENOVO\\AndroidStudioProjects\\MyApplication\\app\\src\\main\\assets\\SocialDistancingDetector.py" + param );
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //method to get url of recorded video
    public String getUrlFromUri(Uri contentUri){
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(contentUri,proj,null,null,null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);

    }

    //method to get name of recorded video
    public String getFileName(String path){
        String[] pathArray = path.split("/");
        return pathArray[pathArray.length-1];
    }
}